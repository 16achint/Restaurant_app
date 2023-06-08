package com.example.intershalaassignment.activity

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.intershalaassignment.R
import com.example.intershalaassignment.adapter.CartRecyclerAdapter
import com.example.intershalaassignment.database.DishesDatabase
import com.example.intershalaassignment.database.DishesEntity

class CartActivity : AppCompatActivity() {

    lateinit var recyclerCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var dbDishList: List<DishesEntity>
    lateinit var proceedToPay : Button
    lateinit var hotelName: TextView
    lateinit var hotelId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerCart = findViewById(R.id.recyclerDishesCart)
        layoutManager = LinearLayoutManager(this)

        dbDishList = RetrieveCartItems(this@CartActivity).execute().get()
        hotelId = intent.getStringExtra("hotel_id").toString()
        hotelName = findViewById(R.id.resName)
        hotelName.text = intent.getStringExtra("hotel_name").toString()
        proceedToPay  = findViewById(R.id.btnProceedToPay)

        recyclerAdapter = CartRecyclerAdapter(this,dbDishList,hotelId,proceedToPay)
        recyclerCart.adapter = recyclerAdapter
        recyclerCart.layoutManager = layoutManager


    }

    class RetrieveCartItems(val context: Context) : AsyncTask<Void,Void,List<DishesEntity>>(){

        override fun doInBackground(vararg params: Void?): List<DishesEntity> {
            val db = Room.databaseBuilder(context,DishesDatabase::class.java,"Dish-db").build()

            return db.DishesDao().getAllDishes()
        }

    }
}