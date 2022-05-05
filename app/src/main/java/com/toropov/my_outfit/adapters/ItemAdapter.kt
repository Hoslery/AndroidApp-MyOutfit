package com.toropov.my_outfit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.toropov.my_outfit.helperClasses.ItemHelperClass
import com.toropov.my_outfit.R

class ItemAdapter(_items: List<ItemHelperClass>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val items: List<ItemHelperClass> = _items

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.item_photo)
        val itemShop: TextView = itemView.findViewById<TextView>(R.id.item_shop)
        val itemName: TextView = itemView.findViewById<TextView>(R.id.item_name)
        val itemPrice: TextView = itemView.findViewById<TextView>(R.id.item_price)

        fun bind(itemHelperClass: ItemHelperClass, clickListener: OnItemClickListener)
        {
            if(itemHelperClass.image.isEmpty()){
                image.setImageResource(R.drawable.hm_logo)
            }else {
                Picasso.get().load(itemHelperClass.image).into(image)
            }
            itemShop.text = itemHelperClass.itemShop
            itemName.text = itemHelperClass.itemName
            itemPrice.text = itemHelperClass.itemPrice

            itemView.setOnClickListener {
                clickListener.onItemClicked(itemHelperClass)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemHelperClass: ItemHelperClass = items[position]

        holder.bind(itemHelperClass,itemClickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_design, parent, false)
        return ItemViewHolder(view)
    }

    interface OnItemClickListener{
        fun onItemClicked(itemHelperClass: ItemHelperClass)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}