package com.bogdanov.tutu.presentation.user_list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bogdanov.tutu.R
import com.bogdanov.tutu.databinding.UserItemBinding
import com.bogdanov.tutu.domain.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

interface OnItemClickListener{
    fun onItemClick(item: User)
}

class UserPagingAdapter(private val itemClickListener: OnItemClickListener): PagingDataAdapter<User, UserPagingAdapter.ViewHolder>(
    UsersDiffCallback()
), View.OnClickListener {

    class ViewHolder(
        private val binding: UserItemBinding
        ): RecyclerView.ViewHolder(binding.root){

            fun bind(item: User){
                binding.usernameTextView.text = item.username
                binding.publicRepos.text = item.id.toString()

                itemView.tag = item

                Glide.with(itemView)
                    .load(item.avatar)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(binding.avatarImageView)
            }


        }

    class UsersDiffCallback: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position) ?: return
        holder.bind(user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return ViewHolder(binding)
    }

    override fun onClick(v: View) {
        val user = v.tag as User
        itemClickListener.onItemClick(user)
    }

}