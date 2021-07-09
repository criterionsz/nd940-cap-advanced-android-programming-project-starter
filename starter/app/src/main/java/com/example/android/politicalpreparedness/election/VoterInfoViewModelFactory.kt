package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.repository.ElectionRepository

//Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(
    val electionRepository: ElectionRepository,
    val id: Int,
    val division: Division
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(electionRepository, id, division) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}