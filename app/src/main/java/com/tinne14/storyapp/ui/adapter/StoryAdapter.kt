package com.tinne14.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tinne14.storyapp.data.remote.response.ListStoryItem
import com.tinne14.storyapp.databinding.ItemStoryBinding
import com.tinne14.storyapp.ui.activity.DetailActivity
import com.tinne14.storyapp.ui.activity.DetailActivity.Companion.EXTRA_ID
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        Glide.with(holder.itemView)
            .load(data?.photoUrl)
            .into(holder.binding.imgPhoto)

        holder.binding.apply {
            tvDeskripsi.text = data?.description
            tvUsername.text = data?.name
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val date: Date = isoFormat.parse(data?.createdAt)
            tvDate.text = date.toString()
        }

        holder.itemView.setOnClickListener {
            val bi = holder.binding
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(bi.imgPhoto, "photo"),
                    Pair(bi.tvUsername, "username"),
                    Pair(bi.tvDeskripsi, "description"),
                    Pair(bi.tvDate, "date")
                )

            val moveDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveDetail.putExtra(EXTRA_ID, data?.id)
            holder.itemView.context.startActivity(moveDetail, optionsCompat.toBundle())
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}