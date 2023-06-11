package com.example.intershalaassignment.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.intershalaassignment.R
import com.example.intershalaassignment.adapter.OrderHistoryRecyclerAdapter
import com.example.intershalaassignment.models.FoodOrder
import com.example.intershalaassignment.models.Orders
import com.example.intershalaassignment.util.SessionManager
import org.json.JSONObject
import java.lang.Exception

class OrderHistoryFragment : Fragment() {
    lateinit var recyclerOrderHistory: RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var recyclerAdapter : OrderHistoryRecyclerAdapter
    lateinit var progessBar : ProgressBar
    lateinit var progressLayout : RelativeLayout

    lateinit var arrayFoodId : MutableList<String>
    lateinit var arrayFoodName : MutableList<String>
    lateinit var arrayFoodCost : MutableList<String>


    lateinit var sharedPreferences : SharedPreferences
    lateinit var sessionManager : SessionManager

    val ordersList = ArrayList<Orders>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        sessionManager = SessionManager(requireContext())
        sharedPreferences = requireActivity().getSharedPreferences(sessionManager.PREF_NAME,sessionManager.PRIVATE_MODE)

        recyclerOrderHistory = view.findViewById(R.id.recyclerOrders)
        progressLayout = view.findViewById(R.id.progressLayout)

        val foodItemList = ArrayList<FoodOrder>()
        val foodItemListArray = ArrayList<ArrayList<FoodOrder>>()

        progessBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE

        val userId = sharedPreferences.getString("user_id","").toString()
        layoutManager = LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity)
        val urlResHistory = "http://13.235.250.119/v2/orders/fetch_result/$userId"

        val jsonRequestObject = object : JsonObjectRequest(Method.GET,urlResHistory,null,Response.Listener<JSONObject> {
            try{
                progressLayout.visibility = View.GONE
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if(success){
                    val orderArray = data.getJSONArray("data")
                    for (i in 0 until orderArray.length()){
                        val orderObject = orderArray.getJSONObject(i)
                        val foodArray = orderObject.getJSONArray("food_items")

                        val order = Orders(
                            orderObject.getString("order_id"),
                            orderObject.getString("restaurant_name"),
                            orderObject.getString("total_cost"),
                            orderObject.getString("order_placed_at")
                        )
                        ordersList.add(order)
                        for (j in 0 until foodArray.length()){
                            val foodArrayObject = foodArray.getJSONObject(j)
                            val foodObject = FoodOrder(
                                foodArrayObject.getString("food_item_id"),
                                foodArrayObject.getString("name"),
                                foodArrayObject.getString("cost")
                            )
                            foodItemList.add(j,foodObject)
                        }
                        foodItemListArray.add(i,foodItemList)
                    }
                    recyclerAdapter = OrderHistoryRecyclerAdapter(activity as Context,ordersList,foodItemListArray)
                    recyclerOrderHistory.adapter = recyclerAdapter
                    recyclerOrderHistory.layoutManager = layoutManager
                }
            }catch (e:Exception){
                Toast.makeText(context,"${e.message} value nai aay",Toast.LENGTH_SHORT).show()
            }
        },Response.ErrorListener {
            Toast.makeText(context,"${it.message} error aa gyi ",Toast.LENGTH_SHORT).show()
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers =  HashMap<String,String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "3e8acc119299ba"
                return headers
            }

        }
        queue.add(jsonRequestObject)
        return view
    }
}