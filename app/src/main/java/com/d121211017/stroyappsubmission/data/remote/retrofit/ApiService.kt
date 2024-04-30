package com.d121211017.stroyappsubmission.data.remote.retrofit

import android.provider.ContactsContract.CommonDataKinds.Email
import com.d121211017.stroyappsubmission.data.remote.entity.SimpleResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun registerAccount(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
        ) : Call<SimpleResponse>
}