package com.example.intershalaassignment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Dishes")
data class DishesEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "dish_name") val name: String,
    @ColumnInfo(name = "dish_price") val cost_for_one: String,
    @ColumnInfo(name = "hotel_id")  val resId: String
    )