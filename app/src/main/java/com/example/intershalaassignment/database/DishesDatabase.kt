package com.example.intershalaassignment.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database;
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [DishesEntity::class],version = 2)
abstract class DishesDatabase : RoomDatabase(){
    abstract fun DishesDao() : DishesDao
}