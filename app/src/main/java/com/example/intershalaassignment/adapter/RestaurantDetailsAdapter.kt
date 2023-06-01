package com.example.intershalaassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.intershalaassignment.R
import com.example.intershalaassignment.models.Dishes
import java.util.ArrayList

class RestaurantDetailsAdapter(val context: Context, val itemList: ArrayList<Dishes>) :
    RecyclerView.Adapter<RestaurantDetailsAdapter.RestaurantDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantDetailsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_menu, parent, false)
        return RestaurantDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RestaurantDetailsViewHolder, position: Int) {
        val dishes = itemList[position]
        holder.dishName.text = dishes.Name
        holder.dishPrice.text = dishes.cost_for_one.toString()
        holder.llContent.setOnClickListener {
            Toast.makeText(context,"${dishes.Name} selected",Toast.LENGTH_SHORT).show()
        }
    }

    class RestaurantDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dishName: TextView = view.findViewById(R.id.DishName)
        val dishPrice: TextView = view.findViewById(R.id.DishPrice)
        val llContent : RelativeLayout = view.findViewById(R.id.llContent)
    }
}