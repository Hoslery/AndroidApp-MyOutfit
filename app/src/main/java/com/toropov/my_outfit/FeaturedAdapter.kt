package com.toropov.my_outfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeaturedAdapter(_featuredStores: List<FeaturedHelperClass>) : RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder>() {
    private val featuredStores: List<FeaturedHelperClass> = _featuredStores


    class FeaturedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.featured_image)
        val title: TextView = itemView.findViewById<TextView>(R.id.featured_title)
        val description: TextView = itemView.findViewById<TextView>(R.id.featured_desc)
    }

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {
        val featuredHelperClass: FeaturedHelperClass = featuredStores[position]

        holder.image.setImageResource(featuredHelperClass.image)
        holder.title.text = featuredHelperClass.title
        holder.description.text = featuredHelperClass.description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.featured_card_design, parent, false)
        return FeaturedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return featuredStores.size
    }
}