package com.tinne14.storyapp.ui.viewmodel


import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tinne14.storyapp.data.remote.response.ListStoryItem
import com.tinne14.storyapp.data.direpo.Repository
import kotlinx.coroutines.launch


class HomeViewModel(private val mRepository: Repository) : ViewModel() {

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): LiveData<String> {
        return mRepository.getToken()
    }

    fun getSesi(): LiveData<Boolean> {
        return mRepository.getSesi()
    }

    fun logout() {
        viewModelScope.launch {
            mRepository.logout()
        }
    }

    fun story(token: String): LiveData<PagingData<ListStoryItem>> =
        mRepository.getStory(token).cachedIn(viewModelScope)

    companion object {
        private const val TAG = "getStories"
    }

}

