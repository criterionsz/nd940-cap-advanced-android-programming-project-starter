/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.politicalpreparedness

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.ElectionDefaultRepository
import com.example.android.politicalpreparedness.repository.ElectionRepository

/**
 * A Service Locator for the [TasksRepository]. This is the prod version, with a
 * the "real" [TasksRemoteDataSource].
 */
object ServiceLocator {

    private val lock = Any()
    private var database: ElectionDatabase? = null

    @Volatile
    var tasksRepository: ElectionRepository? = null
        @VisibleForTesting set

    fun provideElectionRepository(context: Context): ElectionRepository {
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): ElectionRepository {
        val database = database ?: createDataBase(context)
        val newRepo = ElectionDefaultRepository(
            database.electionDao,
            CivicsApi.retrofitService
        )
        tasksRepository = newRepo
        return newRepo
    }


    private fun createDataBase(context: Context): ElectionDatabase {
        return ElectionDatabase.getInstance(context)
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            tasksRepository = null
        }
    }
}
