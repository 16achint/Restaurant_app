package com.example.intershalaassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intershalaassignment.R
import com.example.intershalaassignment.models.FoodOrder

class FoodOrderHistoryRecyclerAdapter(
    val context: Context,
    val foodOrderListEach: ArrayList<FoodOrder>
) : RecyclerView.Adapter<FoodOrderHistoryRecyclerAdapter.recyclerFoodHistoryHolder>() {
    class recyclerFoodHistoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodName: TextView = view.findViewById(R.id.txtResName)
        val foodPrice: TextView = view.findViewById(R.id.txtPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recyclerFoodHistoryHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_food_single_row, parent, false)

        return recyclerFoodHistoryHolder(view)
    }

    override fun getItemCount(): Int {
        return foodOrderListEach.size
    }

    override fun onBindViewHolder(holder: recyclerFoodHistoryHolder, position: Int) {
        val foodOrder = foodOrderListEach[position]
        holder.foodName.text = foodOrder.foodName
        holder.foodPrice.text = foodOrder.foodCost
    }

    override fun onViewRecycled(holder: recyclerFoodHistoryHolder) {
        super.onViewRecycled(holder)
        foodOrderListEach.clear()
    }
}