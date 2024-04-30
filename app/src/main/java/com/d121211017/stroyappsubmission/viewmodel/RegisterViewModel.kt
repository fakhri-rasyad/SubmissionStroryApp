package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d121211017.stroyappsubmission.data.remote.entity.SimpleResponse
import com.d121211017.stroyappsubmission.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(application: Application): ViewModel() {

    companion object {
        private const val TAG = "Register View Model"
    }

    private val _isLoadingRegistration = MutableLiveData<Boolean>()
    val loadingRegistration : LiveData<Boolean> = _isLoadingRegistration

    fun postRegistration(
        name : String,
        email : String,
        password : String
    ){
        _isLoadingRegistration.postValue(true)

        val client = ApiConfig.getApiService().registerAccount(name, email, password)

        client.enqueue(object : Callback<SimpleResponse> {
            override fun onResponse(p0: Call<SimpleResponse>, p1: Response<SimpleResponse>) {
                _isLoadingRegistration.postValue(false)

                val responseBody = p1.body()
                if(p1.isSuccessful && responseBody != null){
                    Log.d(TAG, responseBody.message.toString())
                }
            }

            override fun onFailure(p0: Call<SimpleResponse>, p1: Throwable) {
                Log.e(TAG, p1.message.toString())
            }

        })

    }
}