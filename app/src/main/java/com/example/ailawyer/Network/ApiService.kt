package com.example.ailawyer.Network

import com.example.ailawyer.QueryData.QueryRequest
import com.example.ailawyer.QueryData.QueryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LawyerApiService {
    @POST("query")
    fun queryLawyer(@Body request: QueryRequest): Call<QueryResponse>
}
