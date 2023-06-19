package com.tinne14.storyapp.ui.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinne14.storyapp.data.remote.api.ApiConfig
import com.tinne14.storyapp.data.remote.response.FileUploadResponse
import com.tinne14.storyapp.data.direpo.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UploadViewModel(private val mRepository: Repository) : ViewModel() {

    private val _uploadResponse = MutableLiveData<Boolean?>()
    val uploadResponse: LiveData<Boolean?> = _uploadResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postStories(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        context: Context
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postStories(token, imageMultipart, description)
        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _uploadResponse.value = response.body()?.error
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(context, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun postStoriesWithLocation(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Float?,
        lon: Float?,
        context: Context
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postStoriesWithLocation(token, imageMultipart, description, lat, lon)
        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _uploadResponse.value = response.body()?.error
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(context, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getToken(): LiveData<String> {
        return mRepository.getToken()
    }
}