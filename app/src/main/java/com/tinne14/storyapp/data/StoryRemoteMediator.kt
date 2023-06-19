package com.tinne14.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tinne14.storyapp.data.database.StoryDatabase
import com.tinne14.storyapp.data.remote.api.ApiService
import com.tinne14.storyapp.data.remote.response.ListStoryItem

@OptIn(ExperimentalPagingApi::class)

class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    val token: String
) : RemoteMediator<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val storyKeys = getStoryKeyClosestToCurrentPosition(state)
                storyKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val storyKeys = getStoryKeyForFirstItem(state)
                val prevKey = storyKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = storyKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val storyKeys = getStoryKeyForLastItem(state)
                val nextKey = storyKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = storyKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getStories(token, page, state.config.pageSize).listStory
            val endOfPaginationReached = responseData.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyKeysDao().deleteStoryKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.map {
                    StoryKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.storyKeysDao().insertAll(keys)
                database.storyDao().insertStory(responseData)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getStoryKeyForLastItem(state: PagingState<Int, ListStoryItem>): StoryKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.storyKeysDao().getStoryKeysId(data.id)
        }
    }
    private suspend fun getStoryKeyForFirstItem(state: PagingState<Int, ListStoryItem>): StoryKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.storyKeysDao().getStoryKeysId(data.id)
        }
    }
    private suspend fun getStoryKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): StoryKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.storyKeysDao().getStoryKeysId(id)
            }
        }
    }

}