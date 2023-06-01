package com.example.intershalaassignment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurant")
data class RestaurantEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "res_name") val name: String,
    @ColumnInfo(name = "res_price") val cost_for_one: String,
    @ColumnInfo(name = "res_rating") val rating: String,
    @ColumnInfo (name="res_image") val image: String
)