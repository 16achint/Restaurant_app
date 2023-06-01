package com.example.intershalaassignment.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.RestaurantDetailsActivity
import com.example.intershalaassignment.database.RestaurantDatabase
import com.example.intershalaassignment.database.RestaurantEntity
import com.example.intershalaassignment.models.Restaurants
import com.squareup.picasso.Picasso
import java.util.ArrayList

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurants>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_home_single, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurants = itemList[position]
        holder.txtName.text = restaurants.name
        holder.txtPrice.text = restaurants.cost_for_one.toString()
        holder.txtRating.text = restaurants.rating
        Picasso.get().load(restaurants.Res_image).error(R.drawable.resimage).into(holder.imgResImage)
        Log.d("img",restaurants.Res_image)

        val listOfFavourites = GetAllFavAsyncTask(context).execute().get()

        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(restaurants.id.toString())) {
            holder.favIcon.setImageResource(R.drawable.ic_action_fav)
        } else {
            holder.favIcon.setImageResource(R.drawable.ic_ratings)
        }
        holder.favIcon.setOnClickListener {
            val restaurantEntity = RestaurantEntity(
                restaurants.id,
                restaurants.name,
                restaurants.rating,
                restaurants.cost_for_one.toString(),
                restaurants.Res_image
            )
//            val checkFav = DBAsyncTask(context, restaurantEntity, 1).execute()
//            val isFav = checkFav.get()
            if (!DBAsyncTask(context, restaurantEntity, 1).execute().get()) {
                val async = DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if (result) {
                    holder.favIcon.setImageResource(R.drawable.ic_action_fav)
                } else {
                        Toast.makeText(context,"Some Error has Occurred",Toast.LENGTH_SHORT).show()
                }
            } else {
                val async = DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()
                if (result) {
                    holder.favIcon.setImageResource(R.drawable.ic_ratings)
                } else {
                    Toast.makeText(context, "Some Error has Occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.llContent.setOnClickListener {
//            val intent = Intent(context, RestaurantDescriptionActivity::class.java)
            val intent = Intent(context, RestaurantDetailsActivity::class.java)
            intent.putExtra("id", restaurants.id)
            intent.putExtra("image", restaurants.Res_image)
            context.startActivity(intent)
        }
    }
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.ResName)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val txtRating: TextView = view.findViewById(R.id.txtRating)
        val imgResImage: ImageView = view.findViewById(R.id.imgResImage)
        val favIcon: ImageView = view.findViewById(R.id.ic_fav)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
    }
    class DBAsyncTask(context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "res-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    val restaurant: RestaurantEntity? = db.RestaurantDao().getRestaurantById(restaurantEntity.id.toString())
                    db.close()
                    return restaurant != null
                }

                2 -> {
                    db.RestaurantDao().insertRes(restaurantEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.RestaurantDao().deleteRes(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }

    class GetAllFavAsyncTask(context: Context) : AsyncTask<Void, Void, List<String>>() {
        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "res-db").build()
//        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()
        override fun doInBackground(vararg params: Void?): List<String> {
            val list = db.RestaurantDao().getAllRes()
            val listOfIds = arrayListOf<String>()
            for (i in list) {
                listOfIds.add(i.id.toString())
            }
            return listOfIds
        }
    }
}