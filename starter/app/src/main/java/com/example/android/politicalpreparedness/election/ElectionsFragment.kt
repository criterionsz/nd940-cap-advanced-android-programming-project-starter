package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.ElectionApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment : Fragment() {

    //Declare ViewModel,  Add ViewModel values and create ViewModel
    private lateinit var binding: FragmentElectionBinding
    private val viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory((requireContext().applicationContext as ElectionApplication).electionRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_election,
            container,
            false
        )
        //Add binding values
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        //Link elections to voter info
        val adapter = ElectionListAdapter(
            ElectionListener {
                viewModel.navigateTo(it)
            }
        )
        val adapterSaved = ElectionListAdapter(
            ElectionListener {
                viewModel.navigateTo(it)
            }
        )
        //Initiate recycler adapters
        binding.upcomingElectionsList.adapter = adapter
        binding.savedElectionsList.adapter = adapterSaved

        viewModel.navigate.observe(viewLifecycleOwner) { election ->
            election?.let {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        election.id,
                        election.division
                    )
                )
                viewModel.navigated()
            }
        }

        //TODO: Populate recycler adapters
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUpcomingElections()
    }


}