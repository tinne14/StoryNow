package com.tinne14.storyapp.data.direpo

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.tinne14.storyapp.data.remote.api.ApiService
import com.tinne14.storyapp.data.remote.response.ListStoryItem
import com.tinne14.storyapp.data.StoryRemoteMediator
import com.tinne14.storyapp.data.database.StoryDatabase
import com.tinne14.storyapp.ui.pref.LoginPreference

class Repository(
    private val preference: LoginPreference,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData

    }

    fun getToken(): LiveData<String> {
        return preference.getToken().asLiveData()
    }

    fun getSesi(): LiveData<Boolean> {
        return preference.getSesi().asLiveData()
    }

    suspend fun saveToken(token: String) {
        preference.saveToken(token)
    }

    suspend fun logout() = preference.logout()
}