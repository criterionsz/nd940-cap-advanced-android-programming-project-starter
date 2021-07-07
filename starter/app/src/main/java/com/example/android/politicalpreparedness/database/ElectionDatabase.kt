package com.example.android.politicalpreparedness.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.network.models.Election

//entites - list of tabels
@Database(entities = [Election::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ElectionDatabase : RoomDatabase() {

    abstract val electionDao: ElectionDao

    companion object {


        /*volatile helps us make sure the value of INSTANCE is always up to date and the same to all execution threads
        value of volatile variable will never be cached
        and all writes and reads will be done to and from the main memory, it means that changes made
        by one thread INSTANCE are visible to all other threads immediately
        and we don't get situation when 2 threads each update the same entity in a cache and we have
        problem to continuing inside the companion object*/
        @Volatile
        //keep reference to database, avoid repeatedly opening connections to the database (which is expensive)
        private var INSTANCE: ElectionDatabase? = null

        //return reference to database
        fun getInstance(context: Context): ElectionDatabase {
            /*only one thread of execution at time can enter this block of code
            which makes sure the database only gets initialized once
             */
            synchronized(this) {
                //we want to use kotlin smart cast to make sure we return a ElectionDatabase
                //smart cast available only for local variables
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ElectionDatabase::class.java,
                        "election_database"
                    )
                        //for migration if we change schema of database https://developer.android.com/training/data-storage/room/migrating-db-versions
                        //https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }

    }

}