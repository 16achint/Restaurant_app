package com.example.intershalaassignment.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.util.findColumnIndexBySuffix
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.intershalaassignment.R
import com.example.intershalaassignment.adapter.DashboardRecyclerAdapter
import com.example.intershalaassignment.database.RestaurantDatabase
import com.example.intershalaassignment.database.RestaurantEntity
import com.example.intershalaassignment.models.Restaurants
import com.example.intershalaassignment.util.ConnectionManager
import org.json.JSONException

class DashboardFragment : Fragment() {
    lateinit var HomeRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    val restaurantList = arrayListOf<Restaurants>()
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var iconFavourites: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_deshboard, container, false)
        HomeRecyclerView = view.findViewById(R.id.DashboardRecyclerView)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        CallApi()
        return view
    }

    private fun CallApi() {
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if (ConnectionManager().checkManager(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(
                    Method.GET,
                    url,
                    null,
                    Response.Listener { response -> // added response in lambda expression
                        try {
                            progressLayout.visibility = View.GONE
                            Log.d("api", "Api call success1")
                            val data = response.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                Log.d("api", "Api call success")
                                val resArray = data.getJSONArray("data")
                                for (i in 0 until resArray.length()) {
                                    val resObject = resArray.getJSONObject(i)
                                    val ResObject = Restaurants(
                                        resObject.getString("id").toInt(),
                                        resObject.getString("name"),
                                        resObject.getString("rating"),
                                        resObject.getString("cost_for_one").toInt(),
                                        resObject.getString("image_url")
                                    )
                                    restaurantList.add(ResObject)
                                    layoutManager = LinearLayoutManager(activity)
                                    recyclerAdapter = DashboardRecyclerAdapter(activity as Context, restaurantList)
                                    HomeRecyclerView.adapter = recyclerAdapter
                                    HomeRecyclerView.layoutManager = layoutManager
                                }
                            } else {
                                Toast.makeText(activity as Context, "Some Error has Occurred", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(activity as Context, "Some Unexpected Error has Occurred", Toast.LENGTH_SHORT).show()
                        }
                        println("Response is $response")
                    },
                    Response.ErrorListener { error: VolleyError? ->
                        Log.d("api", "Api call error")
                        // Here will handle Error
                        if (activity != null) {
//                    Toast.makeText(activity as Context, "Volley Error has Occurred", Toast.LENGTH_SHORT).show()
                            Toast.makeText(activity as Context, error?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "3e8acc119299ba"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        } else {
            // internet not available
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                // Do nothing
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                // Do nothing
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
    }
}

