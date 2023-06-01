package com.example.intershalaassignment.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RestaurantDao {
    @Insert
    fun insertRes(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRes(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM Restaurant")
    fun getAllRes() :List<RestaurantEntity>

    @Query("SELECT * FROM restaurant WHERE id = :resId")
    fun getRestaurantById(resId :String) :RestaurantEntity
}