package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.data.favorites.Favorite

class FavoritesAdapter(
    private val onItemClickListener: (Favorite) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    var favorites: List<Favorite> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite: Favorite = favorites[position]
        Glide.with(holder.imageView.context)
            .load(favorite.posterUrl)
            .centerCrop()
            .placeholder(android.R.drawable.ic_delete) // TODO: Add a placeholder image
            .into(holder.imageView)

        holder.imageView.setOnClickListener {
            onItemClickListener(favorite)
        }
    }

    override fun getItemCount(): Int = favorites.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.poster)
    }
}