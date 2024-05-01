package com.d121211017.stroyappsubmission.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

    private val _isEmailValid = MutableLiveData<Boolean>()
    val isEmailValid : LiveData<Boolean> = _isEmailValid

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid : LiveData<Boolean> = _isPasswordValid

    private val _isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(_isEmailValid) { value = it && (_isPasswordValid.value ?: false) }
        addSource(_isPasswordValid) { value = it && (_isEmailValid.value ?: false) }
    }
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private var name = ""
    private var email = ""
    private var password = ""

    fun postRegistration(
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

    fun getData(){
        Log.d(TAG, "$name, $email, $password")
    }

    fun nameValidation(name: String){
        this.name = name
    }
    fun emailValidation(email: String){
        _isEmailValid.postValue(Patterns.EMAIL_ADDRESS.matcher(email).matches())
        this.email = email
    }

    fun passwordValidation(password : String){
        if (password.isNotEmpty() && password.length >= 8) _isPasswordValid.postValue(true)
        else _isPasswordValid.postValue(false)
        this.password = password
    }
}