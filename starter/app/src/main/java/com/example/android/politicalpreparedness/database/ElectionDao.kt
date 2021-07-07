package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.Flow

//this annotation DAO tells compiler that this interface's role is to define how to access data in a Room database
@Dao
interface ElectionDao {

    //insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election)

    //select all election query
    // when user Flow the queries are automatically run asynchronously on a background thread.
    @Query("SELECT * FROM election_table")
    fun getAllElections(): Flow<List<Election>>

    //select single election query
    @Query("SELECT * FROM election_table WHERE id = :key")
    fun get(key: Int): LiveData<Election?>

    //delete query
    @Query("DELETE FROM election_table WHERE id = :key")
    suspend fun delete(key: Int)

    //clear query
    @Query("DELETE FROM election_table")
    suspend fun clear()

}