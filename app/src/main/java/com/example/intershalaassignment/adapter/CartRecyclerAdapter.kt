package com.example.intershalaassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.findColumnIndexBySuffix
import com.example.intershalaassignment.R
import com.example.intershalaassignment.database.DishesEntity
import org.w3c.dom.Text

class CartRecyclerAdapter(val context: Context,val dishList : List<DishesEntity>,val hotelId : String,val ProcessedToPay : Button) : RecyclerView.Adapter<CartRecyclerAdapter.cartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_cart_single_row,parent,false)
        return cartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        val dish = dishList[position]
        holder.txtName.textContent = dish.name
        holder.txtPrice.textContent = dish.cost_for_one
    }

    class cartViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtName : Text = view.findViewById(R.id.dishName)
        val txtPrice : Text = view.findViewById(R.id.price)
    }
}

