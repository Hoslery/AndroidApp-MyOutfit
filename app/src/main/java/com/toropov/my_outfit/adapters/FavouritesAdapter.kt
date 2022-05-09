package com.toropov.my_outfit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.toropov.my_outfit.R
import com.toropov.my_outfit.helperClasses.FavouriteHelperClass
import com.toropov.my_outfit.helperClasses.ItemHelperClass

class FavouritesAdapter (_items: List<FavouriteHelperClass>, val favClickListener: FavouritesAdapter.OnItemClickListener) : RecyclerView.Adapter<FavouritesAdapter.FavViewHolder>() {

    private val items: List<FavouriteHelperClass> = _items

    class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image: ImageView = itemView.findViewById<ImageView>(R.id.item_photo)
        private val itemShop: TextView = itemView.findViewById<TextView>(R.id.item_shop)
        private val itemName: TextView = itemView.findViewById<TextView>(R.id.item_name)
        private val itemPrice: TextView = itemView.findViewById<TextView>(R.id.item_price)

        fun bind(favHelperClass: FavouriteHelperClass, clickListener: OnItemClickListener)
        {
            if(favHelperClass.image!!.isEmpty()){
                image.setImageResource(R.drawable.hm_logo)
            }else {
                Picasso.get().load(favHelperClass.image).into(image)
            }
            itemShop.text = favHelperClass.itemShop
            itemName.text = favHelperClass.itemName
            itemPrice.text = favHelperClass.itemPrice

            itemView.setOnClickListener {
                clickListener.onItemClicked(favHelperClass)
            }

        }
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val favHelperClass: FavouriteHelperClass = items[position]

        holder.bind(favHelperClass,favClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fav_design, parent, false)
        return FavViewHolder(view)
    }

    interface OnItemClickListener{
        fun onItemClicked(favHelperClass: FavouriteHelperClass)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}