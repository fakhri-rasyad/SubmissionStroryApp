package com.d121211017.stroyappsubmission

import com.d121211017.stroyappsubmission.data.remote.entity.SimpleResponse
import com.google.gson.Gson

fun getErrorResponse(response:String): SimpleResponse {
    return Gson().fromJson(response, SimpleResponse::class.java)
}