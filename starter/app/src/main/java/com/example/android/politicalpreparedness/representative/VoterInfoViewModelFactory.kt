package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.repository.ElectionRepository

//Create Factory to generate VoterInfoViewModel with provided election datasource
class RepresentativeViewModelFactory(
    val electionRepository: ElectionRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModel(electionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}