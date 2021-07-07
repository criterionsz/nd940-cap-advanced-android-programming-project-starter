package com.example.android.politicalpreparedness.network

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/*
Network layer
The APi that the viewmodel uses to communicate with our web service
retrofit is a library that creates a network api for out app based on the content from our web service
it fetches data from our web service and route it through a separate converter library
that knows how to decode the data and return it in a form of useful objects

What Retrofit needs to build network api:
base url of our web service
convertor factory that allows retrofit to return the server response in a useful format
* */
private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

// Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
    .add(ElectionAdapter())
    .add(Date::class.java, Rfc3339DateJsonAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
        //we add it for convert to objects
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(CivicsHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    //TODO: Add elections API Call
    @GET("elections")
    suspend fun getElections(): ElectionResponse

    //TODO: Add voterinfo API Call
    @GET("voterinfo")
    suspend fun getVoterInfoQuery(
        @Query("address") address: String,
        @Query("electionId") electionId: Long
    ): VoterInfoResponse

    //TODO: Add representatives API Call
    @GET("representatives")
    suspend fun getRepresentativeInfoByAddress(
        @Query("address") address: String
    ): RepresentativeResponse
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}