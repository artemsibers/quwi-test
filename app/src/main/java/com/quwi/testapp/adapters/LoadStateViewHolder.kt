package com.quwi.testapp.adapters

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.quwi.testapp.databinding.LoadStateViewHolderBinding

class LoadStateViewHolder(private val binding: LoadStateViewHolderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.progressBar.isVisible = loadState is LoadState.Loading
    }
}