package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.repository.Result
import kotlinx.coroutines.launch

/*
Hold all of the data needed for the UI and prepare it for display
ViewModel - abstract class that holds your app's UI data. Survives configuration changes
* */
//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
    private val electionRepository: ElectionRepository
) : ViewModel() {

    /*LiveData is an observable data holder class. Unlike a regular  observable, LiveData is
    lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities,
    fragments, or services.  This awareness ensures LiveData only updates app component observers
    that are in an active lifecycle state.*/

    //Create live data eval for upcoming elections
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val election: LiveData<List<Election>>
        get() = _upcomingElections

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    //TODO: Create live data val for saved elections
    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElection: LiveData<List<Election>>
        get() = _savedElections

    private val _navigate = MutableLiveData<Election?>()
    val navigate: LiveData<Election?>
        get() = _navigate

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    fun getUpcomingElections() = viewModelScope.launch {
        _dataLoading.value = true
        when (val response = electionRepository.refreshUpcomingElections()) {
            is Result.Error ->
                Log.e("ElectionsViewModel", response.exception.message.toString())
            is Result.Success -> {
                _upcomingElections.value = response.data.elections
            }
        }
        _dataLoading.value = false
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun navigateTo(election: Election) {
        _navigate.value = election
    }

    fun navigated() {
        _navigate.value = null
    }
}