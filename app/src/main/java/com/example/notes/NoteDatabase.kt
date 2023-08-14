package com.example.notes

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities=[NoteInfo::class], version=1, exportSchema = true)
abstract class NoteDatabase : RoomDatabase(){

    abstract fun getNoteDao() : NoteDAO

    companion object{

        @Volatile
        private var databaseInstance: NoteDatabase? = null

        fun getDatabase(context : Context) : NoteDatabase {
            return databaseInstance?: synchronized(this){
                Log.d("TAG", "database init")
                val temp = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notedatabase.db"
                ).build()

                databaseInstance = temp
                Log.d("TAG", databaseInstance.toString())

                temp
            }
        }
    }
}