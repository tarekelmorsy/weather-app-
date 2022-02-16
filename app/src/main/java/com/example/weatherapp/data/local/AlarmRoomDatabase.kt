package com.example.weatherapp.data.local

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.prsentation.weather.view.ui.notifications.ModelAlarm
//
//@Database(entities = [ModelAlarm::class], version = 1, exportSchema = false)
//abstract class AlarmRoomDatabase : RoomDatabase() {
//
//  abstract fun AlarmDao(): AlarmDao
//
//  companion object {
//
//    @Volatile private var INSTANCE: AlarmRoomDatabase? = null
//
//    fun getDatabase(context: Context): AlarmRoomDatabase =
//      INSTANCE ?: synchronized(this) { INSTANCE ?: buildDatabase(context).also { INSTANCE = it } }
//
//    private fun buildDatabase(ctx: Context) =
//      Room.databaseBuilder(ctx.applicationContext, AlarmRoomDatabase::class.java, "alarm_database")
//        .build()
//  }
//}


@Database(entities = [ModelAlarm::class], version = 1)
abstract class AlarmDatabase: RoomDatabase() {
  abstract fun movieDao(): AlarmDao

  companion object {
    @Volatile
    private var INSTANCE: AlarmDatabase? = null

    fun getInstance(context: Context): AlarmDatabase {
      val tempInstance = INSTANCE
      if (tempInstance != null) {
        return tempInstance
      }

      synchronized(AlarmDatabase::class) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AlarmDatabase::class.java,
          "movie_database"
        )

          .build()

        INSTANCE = instance
        return instance
      }
    }
  }
}


@Database(entities = [ModelFavorite::class], version = 1)
abstract class FavoriteDatabase: RoomDatabase() {
  abstract fun favoriteDao(): FavoriteDao

  companion object {
    @Volatile
    private var INSTANCE: FavoriteDatabase? = null

    fun getInstance(context: Context): FavoriteDatabase {
      val tempInstance = INSTANCE
      if (tempInstance != null) {
        return tempInstance
      }

      synchronized(FavoriteDatabase::class) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          FavoriteDatabase::class.java,
          "favorite_database"
        )

          .build()

        INSTANCE = instance
        return instance
      }
    }
  }
}