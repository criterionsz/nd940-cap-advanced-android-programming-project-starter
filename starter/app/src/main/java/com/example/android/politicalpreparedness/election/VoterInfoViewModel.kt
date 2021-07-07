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
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val dataSource: ElectionRepository) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _electionName = MutableLiveData<String>()
    val electionName: LiveData<String>
        get() = _electionName


    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse> = _voterInfo


    init {
        //  initState()
    }


    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    fun initState(id: Int) {
        val res = dataSource.getElection(id).value
        if (res == null) {
            _state.value = State.NOT_SAVED
        } else {
            _state.value = State.SAVED
        }
    }


    fun getRemote(id: Long, division: Division) = viewModelScope.launch {
       val res =  dataSource.refreshVoterInfoQuery("fl", id)
        when(res) {
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

    enum class State {
        SAVED, NOT_SAVED
    }

}