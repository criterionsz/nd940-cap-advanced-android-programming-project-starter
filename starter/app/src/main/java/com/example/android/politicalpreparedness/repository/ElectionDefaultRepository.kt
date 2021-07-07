package com.example.android.politicalpreparedness.repository


import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ElectionDefaultRepository(
    private val electionDao: ElectionDao,
    private val remoteSource: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionRepository {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val elections: Flow<List<Election>> = electionDao.getAllElections()


    override suspend fun refreshUpcomingElections() = withContext(ioDispatcher) {
        try {
            val response = remoteSource.getElections()
            return@withContext Result.Success(
                response
            )
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun refreshVoterInfoQuery(address: String, id: Long): Result<VoterInfoResponse> =
        withContext(ioDispatcher) {
            try {
                return@withContext Result.Success(
                    remoteSource.getVoterInfoQuery(
                        address,
                        id
                    )
                )
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override suspend fun refreshRepresentativeInfoByAddress(address: String): Result<RepresentativeResponse> =
        withContext(ioDispatcher) {
            try {
                return@withContext Result.Success(
                    remoteSource.getRepresentativeInfoByAddress(
                        address
                    )
                )
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override fun getElection(id: Int): LiveData<Election?> {
       return electionDao.get(id)
    }


    override suspend fun saveElection(election: Election) = withContext(ioDispatcher) {
        electionDao.insert(election)
    }

    override suspend fun deleteAllElections() = withContext(ioDispatcher) {
        electionDao.clear()
    }

    override suspend fun deleteElection(id: Int) = withContext(ioDispatcher) {
        electionDao.delete(id)
    }

}