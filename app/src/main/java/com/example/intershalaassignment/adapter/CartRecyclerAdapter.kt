package com.example.intershalaassignment.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.intershalaassignment.R
import com.example.intershalaassignment.database.DishesEntity
import com.example.intershalaassignment.util.ConnectionManager
import com.example.intershalaassignment.util.SessionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.intershalaassignment.activity.SuccessScreenActivity
import com.example.intershalaassignment.fragment.SuccessScreenFragment
import java.lang.Exception


class CartRecyclerAdapter(val context: Context,val fragmentManager: FragmentManager, val dishList : List<DishesEntity>, val resId : String, val ProcessedToPay : Button) : RecyclerView.Adapter<CartRecyclerAdapter.cartViewHolder>() {
    lateinit var sessionManager: SessionManager
    lateinit var dishListFinal: ArrayList<DishesEntity>
    lateinit var sharedPreferences : SharedPreferences
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_cart_single_row,parent,false)

        return cartViewHolder(view)
    }

    override fun getItemCount(): Int {
        dishListFinal = ArrayList(dishList)
        dishListFinal.clear()
        for(i in 0 until dishList.size ){
            if(resId == dishList[i].resId){
                val dish = DishesEntity(
                dishList[i].id,
                dishList[i].name,
                dishList[i].cost_for_one,
                dishList[i].resId
                )
                dishListFinal.add(dish)
            }
        }
        return dishListFinal.size
    }
    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        sessionManager = SessionManager(context)
        sharedPreferences = context.getSharedPreferences(sessionManager.PREF_NAME, sessionManager.PRIVATE_MODE)
        val dish = dishListFinal[position]
        holder.txtName.text = dish.name
        holder.txtPrice.text = dish.cost_for_one


        dishListFinal = ArrayList(dishList)
        dishListFinal.clear()
        var totalCost = 0
        for(i in 0 until dishList.size ){
            if(resId == dishList[i].resId){
                val dish = DishesEntity(
                    dishList[i].id,
                    dishList[i].name,
                    dishList[i].cost_for_one,
                    dishList[i].resId
                )
                dishListFinal.add(dish)
            }
        }

        for (i in 0 until dishListFinal.size){
            totalCost += dishListFinal[i].cost_for_one.toInt()
        }
        ProcessedToPay.text = "Processed To Pay( Rs. ${totalCost} )"

        val jsonArray = JSONArray()
        for (i in 0 until dishListFinal.size){
            val jsonObject = JSONObject()
            try {
                jsonObject.put("food_item_id",dishListFinal[i].resId)
            }catch (e : JSONException){
                Toast.makeText(context, "${e.message}" ,Toast.LENGTH_SHORT).show()
            }
            jsonArray.put(jsonObject)
        }

        ProcessedToPay.setOnClickListener {
            val urlPlaceOrder = "http://13.235.250.119/v2/place_order/fetch_result/"
            val queue = Volley.newRequestQueue(context)

            val jsonParam = JSONObject()
            jsonParam.put("user_id",sharedPreferences.getString("user_id","").toString())
            jsonParam.put("restaurant_id",resId).toString()
            jsonParam.put("total_cost",totalCost)
            jsonParam.put("food",jsonArray)

            if(ConnectionManager().checkManager(context)){
                val jsonRequest = object : JsonObjectRequest(Method.POST,urlPlaceOrder,jsonParam,Response.Listener {
                    try{
                        val data  = it.getJSONObject("data")
                        val success  = data.getBoolean("success")
                        if(success){
//                            val fragment = SuccessScreenFragment()
//                            fragmentManager.beginTransaction()
//                                .replace(R.id.linear_layout, fragment)  // Replace "R.id.fragment_container" with the ID of the container where you want to replace the fragment
//                                .addToBackStack(null)
//                                .commit()
//                            holder.linearLayout.visibility = View.VISIBLE

                            val intent = Intent(context,SuccessScreenActivity::class.java)
                            context.startActivity(intent)
                        }
                    }catch (e:Exception){
                        Log.d("error","${e.message}")
                        Toast.makeText(context, "${e.message}" ,Toast.LENGTH_SHORT).show()
                    }
                },Response.ErrorListener {
                    Toast.makeText(context, "${it.message}" ,Toast.LENGTH_SHORT).show()
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "3e8acc119299ba"
                        return headers
                    }
                }
                queue.add(jsonRequest)
            }else{
                val dialog = AlertDialog.Builder(context)
                dialog.setTitle("Error")
                dialog.setMessage("Internal Connection is not Found")
                dialog.setPositiveButton("Open Setting"){ _ , _ ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    context.startActivity(settingsIntent)
                    (context as Activity).finish()
                }
                dialog.setNegativeButton("Exit"){ _ , _ ->
                    ActivityCompat.finishAffinity(context as Activity)
                }
                dialog.create()
                dialog.show()
            }
        }
    }
    class cartViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtName  : TextView = view.findViewById(R.id.dishName)
        val txtPrice : TextView = view.findViewById(R.id.price)
        val linearLayout: LinearLayout = view.findViewById(R.id.linear_layout)

    }
}

