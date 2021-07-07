package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionDefaultRepository
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.repository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(
    private val dataSource: ElectionRepository,
    private val id: Int
) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _electionName = MutableLiveData<String>()
    val electionName: LiveData<String>
        get() = _electionName


    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse> = _voterInfo


    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    fun initState() = viewModelScope.launch {
        val res = dataSource.getElection(id).value
        if (res == null) {
            _state.value = State.NOT_SAVED
        } else {
            _state.value = State.SAVED
        }
    }

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


    fun getRemote() = viewModelScope.launch {
        when (val res = dataSource.refreshVoterInfoQuery("fl", id.toLong())) {
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
