package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.repository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(
    private val dataSource: ElectionRepository,
    private val id: Int,
    private val division: Division
) : ViewModel() {


    private val _electionName = MutableLiveData<String>()
    val electionName: LiveData<String>
        get() = _electionName


    private val _state = MutableLiveData<State?>()
    val state: LiveData<State?>
        get() = _state

    //cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    init {
        _state.value = State.NOT_SAVED
    }

    //Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse> = _voterInfo

    private val _openInfo = MutableLiveData<String>()
    val openInfo: LiveData<String> = _openInfo

    fun initState() = dataSource.getElection(id)

    fun setState(state: State) {
        _state.postValue(state)
    }

    // Add var and methods to support loading URLs
    fun openInfo(uri: String) {
        _openInfo.value = uri
    }

    // Add var and methods to save and remove elections to local database
    fun click() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            if (state.value == State.SAVED) {
                dataSource.deleteElection(id)
                _state.postValue(State.NOT_SAVED)
            } else {
                voterInfo.value?.election?.let {
                    dataSource.saveElection(it)
                    _state.postValue(State.SAVED)
                }
            }
        }
    }

    // Add var and methods to populate voter info
    fun getRemote() = viewModelScope.launch {
        val address = "${division.country} ${division.state}".trim()
        when (val res = dataSource.refreshVoterInfoQuery(address, id.toLong())) {
            is Result.Error -> {
                Log.e("VoterInfoViewModel", res.exception.message.toString())
            }
            is Result.Success -> {
                _voterInfo.value = res.data
            }
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
}

enum class State {
    SAVED, NOT_SAVED
}
