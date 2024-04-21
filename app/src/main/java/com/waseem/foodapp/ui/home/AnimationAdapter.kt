package com.waseem.foodapp.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.waseem.foodapp.models.Results

import kotlin.math.roundToInt


class AnimationAdapter(private val callback: (Results) -> Unit) :
    ListAdapter<Results, AnimationAdapter.ViewHolder>(VideoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_customize_video_layout, parent, false)
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)

           /* Glide.with(holder.itemView.context).load(item.)
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
                        holder.itemView.alpha = 0f  // Initially hide the item
                        holder.itemView.animate().alpha(1f).setDuration(300).start()  // Fade in
                        holder.imageView.setImageDrawable(resource)
                        return false
                    }
                }).into(holder.imageView)
*/
        holder.itemText.text = item.content
        holder.itemAuth.text = item.author
            holder.itemView.setOnClickListener {
                callback(item)
            }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
        val itemText: TextView = itemView.findViewById(R.id.itemText)
        val itemAuth: TextView = itemView.findViewById(R.id.itemAuth)
//        val imgPremium: AppCompatImageView = itemView.findViewById(R.id.imgPremium)

    }



    override fun getItemCount(): Int {
        return currentList.size

    }

    override fun submitList(list: MutableList<Results>?) {
        super.submitList(list)
    }


}

class VideoDiffCallback : DiffUtil.ItemCallback<Results>() {
    override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Results,
        newItem: Results
    ): Boolean {
        return oldItem == newItem
    }
}
