package com.example.intershalaassignment.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface DishesDao {
    @Insert
    fun insertDish(dishesEntity: DishesEntity)

    @Delete
    fun deleteDishes(dishesEntity: DishesEntity)

    @Query("SELECT * FROM Dishes")
    fun getAllDishes() : List<DishesEntity>

    @Query("SELECT * FROM DISHES WHERE id = :DishId")
    fun getDishesId(DishId : String) : DishesEntity

    @Query("SELECT * FROM DISHES WHERE hotel_id = :hotelId")
    fun getFoodByHotelId(hotelId: String): DishesEntity


}