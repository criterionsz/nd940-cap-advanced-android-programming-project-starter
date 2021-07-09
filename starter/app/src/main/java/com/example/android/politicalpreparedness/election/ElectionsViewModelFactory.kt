package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.repository.ElectionRepository

//A ViewModelFactory instantiates ViewModel objects, with or without constructor parameters.
/*
The factory method pattern is a creational design pattern that uses factory methods to create objects.
A factory method is a method that returns an instance of the same class.
 */
// Create Factory to generate ElectionViewModel with provided election datasource
@Suppress("UNCHECKED_CAST")
class ElectionsViewModelFactory(
    private val electionRepository: ElectionRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            return ElectionsViewModel(electionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}