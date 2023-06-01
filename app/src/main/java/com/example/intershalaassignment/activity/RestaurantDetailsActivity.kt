package com.example.intershalaassignment.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.intershalaassignment.R
import com.example.intershalaassignment.adapter.RestaurantDetailsAdapter
import com.example.intershalaassignment.models.Dishes
import com.example.intershalaassignment.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class RestaurantDetailsActivity : AppCompatActivity() {
    lateinit var restaurantRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: RestaurantDetailsAdapter
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    val dishesList = arrayListOf<Dishes>()
    var resId = 0
    var image = "image"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        restaurantRecyclerView = findViewById(R.id.DishesRecyclerView)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Restaurant Menu"

        if (intent != null) {
            resId = intent.getIntExtra("id", 0)
        } else {
            finish()
            Toast.makeText(
                this@RestaurantDetailsActivity,
                "Some UnExpected Error the Occurred",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (resId == 0) {
            Toast.makeText(
                this@RestaurantDetailsActivity,
                "Some UnExpected Error the Occurred",
                Toast.LENGTH_SHORT
            ).show()
        }

        callApi()
    }

    private fun callApi() {
        val queue = Volley.newRequestQueue(this@RestaurantDetailsActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"

        val jsonParams = JSONObject()
        jsonParams.put("id", resId)
        if (ConnectionManager().checkManager(this@RestaurantDetailsActivity)) {
            Log.d("tag", "checking")
            val jsonRequest = object :
                JsonObjectRequest(Method.GET, url, jsonParams, Response.Listener { response ->
                    try {
                        val data = response.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val resArray = data.getJSONArray("data")
                            for (i in 0 until resArray.length()) {
                                val resObject = resArray.getJSONObject(i)
                                val dishes = Dishes(
                                    resObject.getString("id").toInt(),
                                    resObject.getString("name"),
                                    resObject.getString("cost_for_one").toInt(),
                                    resObject.getString("restaurant_id").toInt()
                                )
                                dishesList.add(dishes)
                                layoutManager = GridLayoutManager(this,2)
                                recyclerAdapter = RestaurantDetailsAdapter(this,dishesList)
                                restaurantRecyclerView.adapter = recyclerAdapter
                                restaurantRecyclerView.layoutManager = layoutManager
                            }
                        }else{
                            Toast.makeText(this, "Some Error has Occurred", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@RestaurantDetailsActivity,
                            "${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener { error ->
                    Toast.makeText(
                        this@RestaurantDetailsActivity,
                        "Volley Error $error",
                        Toast.LENGTH_SHORT
                    ).show()

                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "3e8acc119299ba"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
    }
}