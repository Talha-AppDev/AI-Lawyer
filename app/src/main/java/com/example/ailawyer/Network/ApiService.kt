package com.example.ailawyer.Network

import com.example.ailawyer.QueryData.QueryRequest
import com.example.ailawyer.QueryData.QueryResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface ApiService {
    @POST("query")
    fun getQueryResponse(@Body request: QueryRequest): Call<QueryResponse>
}