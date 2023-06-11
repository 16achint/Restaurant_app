package com.example.intershalaassignment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.intershalaassignment.R
import com.example.intershalaassignment.adapter.RestaurantDetailsAdapter
import com.example.intershalaassignment.models.Dishes
import com.example.intershalaassignment.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception

class RestaurantDetailsActivity : AppCompatActivity() {
    lateinit var restaurantRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: RestaurantDetailsAdapter
    lateinit var linearLayout : LinearLayout
    lateinit var linearLayout1 : RelativeLayout
    lateinit var cartBtn : Button
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    val dishesList = arrayListOf<Dishes>()
    var resId = 0
    var image = "image"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        restaurantRecyclerView = findViewById(R.id.DishesRecyclerView)
        toolbar = findViewById(R.id.toolbar)
        linearLayout = findViewById(R.id.linear_layout)
        linearLayout1 = findViewById(R.id.res_details)
        cartBtn = findViewById(R.id.btnCart)
        setSupportActionBar(toolbar)

        if (intent != null) {
            resId = intent.getIntExtra("id", 0)
            supportActionBar?.title = intent.getStringExtra("name")
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sort : View? = findViewById(R.id.action_sort)
        sort?.visibility = View.GONE
        val intent = Intent(this,CartActivity::class.java)
        startActivity(intent)
        supportActionBar?.title = "Add to Cart"

        linearLayout.visibility = View.GONE
        return super.onOptionsItemSelected(item)
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
                                    resObject.getString("id"),
                                    resObject.getString("name"),
                                    resObject.getString("cost_for_one"),
                                    resObject.getString("restaurant_id")
                                )

                                dishesList.add(dishes)
                            }
                            layoutManager = LinearLayoutManager(this)
                            recyclerAdapter = RestaurantDetailsAdapter(this,dishesList,cartBtn,intent.getStringExtra("name").toString())
                            restaurantRecyclerView.adapter = recyclerAdapter
                            restaurantRecyclerView.layoutManager = layoutManager
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