package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.flow.Flow


/**
 * Interface to the data layer.
 */
interface ElectionRepository {

    suspend fun refreshUpcomingElections(): Result<ElectionResponse>

    suspend fun refreshVoterInfoQuery(address: String, id: Long): Result<VoterInfoResponse>

    suspend fun refreshRepresentativeInfoByAddress(address: String): Result<RepresentativeResponse>

    fun getElection(id: Int): LiveData<Election?>

    suspend fun saveElection(election: Election)

    suspend fun deleteAllElections()

    suspend fun deleteElection(id: Int)
}