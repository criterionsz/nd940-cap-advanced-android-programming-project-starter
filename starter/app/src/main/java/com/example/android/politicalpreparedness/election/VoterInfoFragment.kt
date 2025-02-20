package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.ServiceLocator
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {
    private lateinit var binding: FragmentVoterInfoBinding

    // Add ViewModel values and create ViewModel
    private val viewModel by viewModels<VoterInfoViewModel> {
        VoterInfoViewModelFactory(
            ServiceLocator.provideElectionRepository(requireContext()),
            VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId,
            VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Add binding values

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_voter_info,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        //Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        viewModel.initState()
        viewModel.getRemote()
        viewModel.openInfo.observe(viewLifecycleOwner) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }

        // cont'd Handle save button clicks
        // Handle save button UI state
        // Create method to load URL intents
        //Handle loading of URLs
        viewModel.initState().observe(viewLifecycleOwner) {
            if (it == null) {
                viewModel.setState(State.NOT_SAVED)
            } else {
                viewModel.setState(State.SAVED)
            }
        }
        return binding.root
    }
}