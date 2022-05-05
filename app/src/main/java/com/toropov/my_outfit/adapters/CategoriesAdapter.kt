package com.toropov.my_outfit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.toropov.my_outfit.R
import com.toropov.my_outfit.helperClasses.CategoriesHelperClass

class CategoriesAdapter(_categories: List<CategoriesHelperClass>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {
    private val categories: List<CategoriesHelperClass> = _categories


    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById<ImageView>(R.id.category_image)
        val title: TextView = itemView.findViewById<TextView>(R.id.category_title)
        val back: RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.category_layout)

        fun bind(categoriesHelperClass: CategoriesHelperClass, clickListener: OnItemClickListener)
        {
            title.text = categoriesHelperClass.title
            image.setImageResource(categoriesHelperClass.image)
            back.background = categoriesHelperClass.back

            itemView.setOnClickListener {
                clickListener.onItemCategoriesClicked(categoriesHelperClass)
            }
        }
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val categoriesHelperClass: CategoriesHelperClass = categories[position]

        holder.bind(categoriesHelperClass,itemClickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.categories_card_design, parent, false)
        return CategoriesViewHolder(view)
    }

    interface OnItemClickListener{
        fun onItemCategoriesClicked(categoriesHelperClass: CategoriesHelperClass)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}