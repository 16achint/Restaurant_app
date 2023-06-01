package com.example.intershalaassignment.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.intershalaassignment.R
import com.example.intershalaassignment.activity.MainActivity
import com.example.intershalaassignment.activity.RestaurantDetailsActivity
import com.example.intershalaassignment.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context: Context,val resList : List<RestaurantEntity>) : RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouritesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_fav_single_row,parent,false)

        return FavouritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resList.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val res = resList[position]
        holder.txtResName.text = res.name
        holder.txtResPrice.text = res.cost_for_one
        holder.txtResRating.text = res.rating
        Picasso.get().load(res.image).error(R.drawable.resimage).into(holder.imgResImage)

        holder.favIcon.setImageResource(R.drawable.ic_action_fav)
//
//        holder.iiContent.setOnClickListener {
//            val intent = Intent(context, MainActivity::class.java)
//            context.startActivity(intent)
//        }
    }
    class FavouritesViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtResName : TextView = view.findViewById(R.id.txtFavResTitle)
        val txtResPrice : TextView = view.findViewById(R.id.txtFavRating)
        val txtResRating : TextView = view.findViewById(R.id.txtFavResPrice)
        val favIcon : ImageView = view.findViewById(R.id.favIcon)
        val imgResImage : ImageView = view.findViewById(R.id.imgFavResImage)
        val iiContent : LinearLayout = view.findViewById(R.id.llFavContent)
    }
}