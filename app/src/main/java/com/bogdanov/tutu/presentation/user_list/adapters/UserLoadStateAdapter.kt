package com.bogdanov.tutu.presentation.user_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanov.tutu.R
import com.bogdanov.tutu.databinding.LoadStateBinding

class UserLoadStateAdapter(
    private val tryAgainAction: () -> Unit
): LoadStateAdapter<UserLoadStateAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: LoadStateBinding,
        private val tryAgainAction: () -> Unit
    ): RecyclerView.ViewHolder(binding.root){

        init {
            binding.tryAgainButton.setOnClickListener {
                tryAgainAction()
            }
        }

        fun bind(loadState: LoadState)= with(binding){
            messageTextView.isVisible = loadState is LoadState.Error
            tryAgainButton.isVisible = loadState is LoadState.Error
            progressBar.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LoadStateBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, tryAgainAction)
    }

    interface LoadSateListener{
        fun onLoadState()
    }
}