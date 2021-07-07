package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    // Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}

//Create ElectionViewHolder
class ElectionViewHolder(val binding: ViewholderElectionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Election, clickListener: ElectionListener) {
        binding.election = item
        binding.clickListener = clickListener
        binding.dateElection.text = item.electionDay.toString()
        binding.titleElection.text = item.name
        binding.executePendingBindings()
    }

    //add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup): ElectionViewHolder {
            return ElectionViewHolder(
                ViewholderElectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }
    }
}


//Create ElectionDiffCallback
class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

}

//Create ElectionListener
class ElectionListener(val clickListener: (Election) -> Unit) {
    fun onClick(election: Election) = clickListener(election)
}