package com.example.intershalaassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intershalaassignment.R
import com.example.intershalaassignment.models.FoodOrder
import com.example.intershalaassignment.models.Orders
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryRecyclerAdapter(val context: Context , val orderList: List<Orders> , val foodOrderList:ArrayList<ArrayList<FoodOrder>>) :
    RecyclerView.Adapter<OrderHistoryRecyclerAdapter.OrderHistoryViewHolder>() {

        class OrderHistoryViewHolder(view : View) : RecyclerView.ViewHolder(view){
            val date : TextView = view.findViewById(R.id.txtDate)
            val resName : TextView = view.findViewById(R.id.resName)

            lateinit var recyclerFoodHistoryAdapter: FoodOrderHistoryRecyclerAdapter
            val recyclerFoodHistory : RecyclerView = view.findViewById(R.id.recyclerView)
            val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(view.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_order_single_row,parent,false)

        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orders = orderList[position]
        holder.resName.text = orders.resName
        holder.date.text = formatDate(orders.orderDate)
        holder.recyclerFoodHistoryAdapter = FoodOrderHistoryRecyclerAdapter(context,foodOrderList[position])
        holder.recyclerFoodHistory.adapter = holder.recyclerFoodHistoryAdapter
        holder.recyclerFoodHistory.layoutManager = holder.layoutManager
    }

    private fun formatDate(orderDate: String): String? {
        val inputFormatter = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH)
        val date : Date = inputFormatter.parse(orderDate) as Date
        val outputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

        return outputFormatter.format(date)
    }
}