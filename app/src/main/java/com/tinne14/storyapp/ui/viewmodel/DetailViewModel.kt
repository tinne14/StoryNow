package com.tinne14.storyapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinne14.storyapp.data.remote.api.ApiConfig
import com.tinne14.storyapp.data.remote.response.DetailResponse
import com.tinne14.storyapp.data.remote.response.Story
import com.tinne14.storyapp.data.direpo.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailViewModel(private val mRepository: Repository) : ViewModel() {

    private val _listStoriesDetail = MutableLiveData<Story?>()
    val listStoriesDetail: LiveData<Story?> = _listStoriesDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoriesDetail(token: String, id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStoriesDetail(token, id)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStoriesDetail.postValue(response?.body()?.story)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getToken(): LiveData<String> {
        return mRepository.getToken()
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }

}



