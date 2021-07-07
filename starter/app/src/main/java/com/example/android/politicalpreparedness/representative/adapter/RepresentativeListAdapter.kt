package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ViewholderRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.model.Representative

/* Adapters converts one interface to work wit another
Adapter and ViewHolder classes work together to define how your data is displayed.
ViewHolder is a wrapper around a View that contains the layout for an individual item in the list.
Adapter creates ViewHolder objects as needed, and also sets the data for those views.
The process of associating views to their data is called binding.
1. RecyclerView will ask adapter how many items there are
2. Then adapter ask to create new view for first item
3. then ask adapter draw the item (go to 2nd repeat until items end)
* */
class RepresentativeListAdapter :
    ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()) {

    /* RecyclerView calls this method whenever it needs to create a new ViewHolder
    The method creates and initializes the ViewHolder and its associated View,
    but does not fill in the view's contents—the ViewHolder has not yet been bound to specific data.

parent – view will be added to some viewgroup before it gets dsplayed t the screen
a viewgroup is a type of view that holds a group of view
in reality this will always be the recyclerview

    * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)
    }

    /*RecyclerView calls this method to associate a ViewHolder with data.
     The method fetches the appropriate data and uses the data to fill in the view holder's layout.
      For example, if the RecyclerView displays a list of names, the method might find the
      appropriate name in the list and fill in the view holder's TextView widget.

      */
    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

/*
ViewHolder tells recyclerview where and how an item  should get drawn in the list
Hold views
* store information for recycler view
* recyclerviews main interface */
class RepresentativeViewHolder(val binding: ViewholderRepresentativeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Representative) {
        binding.representative = item
        binding.representativePhoto.setImageResource(R.drawable.ic_profile)
        binding.position.text = item.official.name
        binding.electionName.text = item.office.name
        binding.party.text = item.official.party

        //Show social links ** Hint: Use provided helper methods
        item.official.channels?.let {
            showSocialLinks(it)
        }
        //Show www link ** Hint: Use provided helper methods
        item.official.urls?.let {
            showWWWLinks(it)
        }
        binding.executePendingBindings()
    }

    //add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup): RepresentativeViewHolder {
            return RepresentativeViewHolder(
                ViewholderRepresentativeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }
    }

    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) {
            enableLink(binding.facebookIcon, facebookUrl)
        }

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) {
            enableLink(binding.twitterIcon, twitterUrl)
        }
    }

    private fun showWWWLinks(urls: List<String>) {
        enableLink(binding.wwwIcon, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
            .map { channel -> "https://www.facebook.com/${channel.id}" }
            .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
            .map { channel -> "https://www.twitter.com/${channel.id}" }
            .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }

}

/* DiffUtil
Helper for recyclerview adapters that calculates changes in lists and minimizes modifications
uses myers diff algo
we use diffutil because we want to redraw only elements that changed
and alo we have animation by default ;)

* */
//Create RepresentativeDiffCallback
class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem.official.name == newItem.office.name
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }

}

//todo: Create RepresentativeListener
class RepresentativeListener(val clickListener: (Representative) -> Unit) {
  //  fun onClick(representative: Representative) = cli

}