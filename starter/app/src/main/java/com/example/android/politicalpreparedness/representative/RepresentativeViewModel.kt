package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.repository.Result
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    private val electionRepository: ElectionRepository
) : ViewModel() {

    //Establish live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
    get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    //Create function to fetch representatives from API from a provided address
    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
    fun fetchRepresentatives(address: String) = viewModelScope.launch {
        when (val res = electionRepository.refreshRepresentativeInfoByAddress(address)) {
            is Result.Error -> {

            }
            is Result.Success -> {
                val offices = res.data.offices
                val officials = res.data.officials
                _representatives.value = offices.flatMap { office ->
                    office.getRepresentatives(officials)
                }
            }
        }
    }


    // Create function get address from geo location
    fun getAddressFromGeoLocation(address: Address) {
        _address.value = address
        fetchRepresentatives(address.toFormattedString())

    }

    //Create function to get address from individual fields

}
