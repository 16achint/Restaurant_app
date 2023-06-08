package com.example.intershalaassignment.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.CartActivity
import com.example.intershalaassignment.database.DishesDatabase
import com.example.intershalaassignment.database.DishesEntity
import com.example.intershalaassignment.models.Dishes

class RestaurantDetailsAdapter(val context: Context, private val itemList: ArrayList<Dishes>,val cartBtn : Button,val hotelName:String) :
    RecyclerView.Adapter<RestaurantDetailsAdapter.RestaurantDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantDetailsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_menu, parent, false)
        return RestaurantDetailsViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: RestaurantDetailsViewHolder, position: Int) {
        val dishes = itemList[position]
        holder.dishName.text = dishes.Name
        holder.dishPrice.text = dishes.cost_for_one
//        holder.llContent.setOnClickListener {
//            Toast.makeText(context, "${dishes.Name} selected", Toast.LENGTH_SHORT).show()
//        }
        var check = getAllCartAsyncTask(context,dishes.restaurant_id).execute().get()
        if (check) {
            cartBtn.visibility = View.VISIBLE
            cartBtn.setOnClickListener {
                val intent = Intent(context, CartActivity::class.java)
                intent.putExtra("hotel_id", dishes.restaurant_id)
                intent.putExtra("hotel_name", hotelName)
                context.startActivity(intent)
            }
        } else {
            cartBtn.visibility = View.GONE
        }
        val dishesEntity = DishesEntity(
            dishes.id,
            dishes.Name,
            dishes.cost_for_one,
            dishes.restaurant_id
        )
        val checkCart = DBAsyncTask(context, dishesEntity, 1).execute()
        val isCartItem = checkCart.get()
        if (isCartItem) {
            holder.add_btn.setBackgroundColor(context.getColor(R.color.white))
            holder.add_btn.setTextColor(context.getColor(R.color.fonts))
            holder.add_btn.text = "Remove"
        } else {
            holder.add_btn.setBackgroundColor(context.getColor(R.color.colorPrimaryDark))
            holder.add_btn.setTextColor(context.getColor(R.color.white))
            holder.add_btn.text = "Add"
        }
        val ts: Toast = Toast.makeText(
            context,
            "Welcome",
            Toast.LENGTH_SHORT
        )
        holder.add_btn.setOnClickListener {
            if (!DBAsyncTask(context, dishesEntity, 1).execute().get()) {
                val async = DBAsyncTask(context, dishesEntity, 2).execute()
                val result = async.get()
                if (result) {
                    check = getAllCartAsyncTask(context,dishes.restaurant_id).execute().get()
                    if (check) {
                        cartBtn.visibility = View.VISIBLE
                        cartBtn.setOnClickListener {
                            val intent = Intent(context, CartActivity::class.java)
                            intent.putExtra("hotel_id", dishes.restaurant_id)
                            intent.putExtra("hotel_name", hotelName)
                            context.startActivity(intent)
                        }
                    } else {
                        cartBtn.visibility = View.GONE
                    }
                    holder.add_btn.setBackgroundColor(context.getColor(R.color.white))
                    holder.add_btn.setTextColor(context.getColor(R.color.fonts))
                    holder.add_btn.text = "Remove"

                } else {
                    Toast.makeText(context,"Some Error has Occurred",Toast.LENGTH_SHORT).show()
                }
            }else{
                val async = DBAsyncTask(context,dishesEntity,3).execute()
                val result = async.get()
                if(result){
                    check = getAllCartAsyncTask(context, dishes.restaurant_id).execute().get()
                    if (check) {
                        cartBtn.visibility = View.VISIBLE
                        cartBtn.setOnClickListener {
                            val intent = Intent(context, CartActivity::class.java)
                            intent.putExtra("hotel_id", dishes.restaurant_id)
                            intent.putExtra("hotel_name", hotelName)
                            context.startActivity(intent)
                        }
                    } else {
                        cartBtn.visibility = View.GONE
                    }
                    holder.add_btn.setBackgroundColor(context.getColor(R.color.colorPrimaryDark))
                    holder.add_btn.setTextColor(context.getColor(R.color.white))
                    holder.add_btn.text = "Add"
                }else{
                    Toast.makeText(context,"Some Error has Occurred",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    class RestaurantDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dishName: TextView = view.findViewById(R.id.DishName)
        val dishPrice: TextView = view.findViewById(R.id.DishPrice)
        val llContent: RelativeLayout = view.findViewById(R.id.llContent)
        val add_btn: Button = view.findViewById(R.id.btn_add)
        val sort : View? = view.findViewById(R.id.action_sort)
    }
    class DBAsyncTask(content: Context, val DishesEntity: DishesEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(content, DishesDatabase::class.java, "Dish-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    // check db if dishes is fav or not
                    val dishes: DishesEntity? =
                        db.DishesDao().getDishesId(DishesEntity.id.toString())
                    db.close()
                    return dishes != null
                }

                2 -> {
                    // save dishes into db as fav
                    db.DishesDao().insertDish(DishesEntity)
                    db.close()
                    return true
                }

                3 -> {
                    // Remove the fav dishes
                    db.DishesDao().deleteDishes(DishesEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }

    // migration problem solved by deleting app from mobile and re-install it
    class getAllCartAsyncTask(val content: Context,val hotelId: String) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(content, DishesDatabase::class.java, "Dish-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            val cartFood: DishesEntity = db.DishesDao().getFoodByHotelId(hotelId)

            return cartFood != null
        }
    }
//
//        class getAllCartAsyncTask(val context: Context, val hotelId: String):AsyncTask<Void, Void, Boolean>() {

//            val dbase = Room.databaseBuilder(context, DishesDatabase::class.java, "Food_Cart")
//                .build()
//
//            val MIGRATION_1_2 = object : Migration(1, 2){
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    database.execSQL(UserSqlUtils().createTable)
//                }
//            }
//            val db = Room.databaseBuilder(context, DishesDatabase::class.java, "Food_Cart")
//                .addMigrations(MIGRATION_1_2).build()
//            override fun doInBackground(vararg params: Void?): Boolean {
//
//                val cartFood: DishesEntity? = db.DishesDao().getFoodByHotelId(hotelId)
//                return (cartFood != null)
//            }
//    }
//    class UserSqlUtils {
//        var createTable = "CREATE TABLE IF NOT EXISTS User (id INTEGER, PRIMARY KEY(id))"
//    }
}