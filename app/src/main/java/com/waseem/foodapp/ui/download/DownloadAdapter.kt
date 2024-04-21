package com.hmbl.battery.charginganimation.ui.dashboard.download

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.waseem.foodapp.R
import java.io.File
import kotlin.math.roundToInt


class DownloadAdapter(private val callback: (File) -> Unit) :
    ListAdapter<File, DownloadAdapter.ViewHolder>(VideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_customize_video_layout, parent, false)
            )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            Glide.with(holder.itemView.context).load(item.absoluteFile)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false // Return false to allow Glide to handle the failure
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).into(holder.imageView)

            holder.itemView.setOnClickListener {
                callback(item)
            }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)

    }



    override fun submitList(list: MutableList<File>?) {
        super.submitList(list)
    }



}

class VideoDiffCallback : DiffUtil.ItemCallback<File>() {
    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: File,
        newItem: File
    ): Boolean {
        return oldItem == newItem
    }
}
