package com.example.intershalaassignment.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.intershalaassignment.R
import com.example.intershalaassignment.adapter.DashboardRecyclerAdapter
import com.example.intershalaassignment.adapter.FavouritesRecyclerAdapter
import com.example.intershalaassignment.adapter.RestaurantDetailsAdapter
import com.example.intershalaassignment.database.RestaurantDatabase
import com.example.intershalaassignment.database.RestaurantEntity

class FavouritesRestaurantFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: FavouritesRecyclerAdapter
    var favList = listOf<RestaurantEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites_restaurant, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourites)
        layoutManager = LinearLayoutManager(activity as Context)

        favList = RetrieveFavourites(activity as Context).execute().get()
        if(activity != null){
            recyclerAdapter = FavouritesRecyclerAdapter(activity as Context, favList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }
        return view
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void,Void,List<RestaurantEntity>>(){
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val db = Room.databaseBuilder(context,RestaurantDatabase::class.java,"res-db").build()

            return db.RestaurantDao().getAllRes()
        }
    }
}