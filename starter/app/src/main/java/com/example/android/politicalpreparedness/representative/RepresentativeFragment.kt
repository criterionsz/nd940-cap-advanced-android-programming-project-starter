package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.ElectionApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        //Add Constant for Location request
        private const val REQUEST_LOCATION_PERMISSION = 1

    }

    private lateinit var binding: FragmentRepresentativeBinding

    //Declare ViewModel
    private val viewModel by viewModels<RepresentativeViewModel> {
        RepresentativeViewModelFactory((requireContext().applicationContext as ElectionApplication).electionRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Establish bindings
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_representative,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        //Define and assign Representative adapter
        val adapter = RepresentativeListAdapter()
        binding.representativesList.adapter = adapter

        // Populate Representative adapter

        //Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            getLocation()
        }
        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            if (binding.addressLine1.text.trim().isNullOrEmpty() ||
                binding.city.text.trim().isNullOrEmpty() ||
                binding.zip.text.trim().isNullOrEmpty() ||
                binding.state.selectedItem.toString().trim().isNullOrEmpty()
            ) {
                Toast.makeText(requireContext(), "Fill all required gaps", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.getAddressFromGeoLocation(
                    Address(
                        line1 = binding.addressLine1.text.toString(),
                        line2 = binding.addressLine2.text.toString(),
                        city = binding.city.text.toString(),
                        state = binding.state.selectedItem.toString(),
                        zip = binding.zip.text.toString()
                    )
                )
            }

        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            } else {
                // Permission denied.
                Snackbar.make(
                    requireView(),
                    R.string.permission_denied_explanation, Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.settings) {
                        // Displays App settings screen.
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", requireActivity().packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })

                    }.show()
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //Request Location permissions
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    private fun isPermissionGranted(): Boolean {
        // Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //Get location from LocationServices
        //The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        if (checkLocationPermissions()) {
            LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation.addOnSuccessListener {
                if (it == null) {
                    Toast.makeText(requireContext(), "Something went wrong. Can't get location, please fill gaps", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.getAddressFromGeoLocation(geoCodeLocation(it))
                }
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }

    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}