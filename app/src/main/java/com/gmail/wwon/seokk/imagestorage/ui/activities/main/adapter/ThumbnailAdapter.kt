package com.gmail.wwon.seokk.imagestorage.ui.activities.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.wwon.seokk.imagestorage.data.ImageLoader
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail
import com.gmail.wwon.seokk.imagestorage.databinding.ItemThumbnailBinding
import com.gmail.wwon.seokk.imagestorage.ui.viewmodels.MainViewModel
import com.gmail.wwon.seokk.imagestorage.utils.imageLoader

class ThumbnailAdapter(private val viewModel: MainViewModel) :
    ListAdapter<Thumbnail, ThumbnailAdapter.ViewHolder>(ThumbnailDiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun update(newItemList: List<Thumbnail>) = submitList(newItemList.toMutableList())

    class ViewHolder private constructor(private val binding: ItemThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemThumbnailBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(viewModel: MainViewModel, item: Thumbnail) {
            binding.apply {
                mainViewModel = viewModel
                thumbnail = item
                itemView.context.imageLoader().loadImage(item.url, ivThumbnail)
            }
        }
    }

    object ThumbnailDiffCallback : DiffUtil.ItemCallback<Thumbnail>() {
        override fun areItemsTheSame(oldItem: Thumbnail, newItem: Thumbnail): Boolean {
            return oldItem.url == newItem.url
        }
        override fun areContentsTheSame(oldItem: Thumbnail, newItem: Thumbnail): Boolean {
            return oldItem == newItem
        }
    }
}