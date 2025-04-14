package com.example.ailawyer.network

import com.example.ailawyer.dataclasses.QueryRequest
import com.example.ailawyer.dataclasses.QueryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LawyerApiService {
    @POST("query")
    fun queryLawyer(@Body request: QueryRequest): Call<QueryResponse>
}
