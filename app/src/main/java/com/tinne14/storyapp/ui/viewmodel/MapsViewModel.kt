package com.tinne14.storyapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinne14.storyapp.data.remote.api.ApiConfig
import com.tinne14.storyapp.data.remote.response.ListStoryItem
import com.tinne14.storyapp.data.remote.response.StoriesResponse
import com.tinne14.storyapp.data.direpo.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val mRepository: Repository) : ViewModel() {

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): LiveData<String> {
        return mRepository.getToken()
    }

    fun getLocationStories(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getLocationStories(token)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStories.postValue(response?.body()?.listStory as List<ListStoryItem>)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "Maps ViewModel"
    }
}