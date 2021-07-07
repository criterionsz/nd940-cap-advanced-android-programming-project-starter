package com.example.android.politicalpreparedness

import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.State
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("buttonTitle")
fun bindButton(button: Button, state: State) {
    if (state == State.SAVED) {
        button.text = button.context.getString(R.string.unfollow_election)
    } else {
        button.text = button.context.getString(R.string.follow_eleciton)
    }
}


/**
 * Use this binding adapter to show and hide the views using boolean variables
 */
@BindingAdapter("visibilityView")
fun setFadeVisible(view: View, visible: Boolean) {
    if (visible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}
