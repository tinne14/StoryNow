package com.tinne14.storyapp.ui.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinne14.storyapp.data.remote.api.ApiConfig
import com.tinne14.storyapp.data.remote.response.LoginResponse
import com.tinne14.storyapp.data.direpo.Repository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val mRepository: Repository) : ViewModel() {

    private val _loginResponse = MutableLiveData<Boolean?>()
    val loginResponse: LiveData<Boolean?> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val TAG = "Login"

    fun postLogin(email: String, password: String, context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    viewModelScope.launch {
                        mRepository.saveToken(response.body()?.loginResult?.token.toString())
                    }
                    _loginResponse.value = response.body()?.error
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.e(TAG, "Eror: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

}