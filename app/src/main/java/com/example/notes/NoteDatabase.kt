package com.example.notes

import android.content.Context
import android.util.Log
import androidx.constraintlayout.motion.widget.Debug
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper


@Database(entities=[NoteInfo::class], version=1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase(){

    abstract fun getNoteDao() : NoteDAO

    companion object{
        private var databaseInstance: NoteDatabase? = null

        fun initDatabase(context : Context) {
            Log.d("TAG", "database init")
            databaseInstance = Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_database"
            ).build()

            Log.d("TAG", databaseInstance.toString())

        }

        fun getDatabase() : NoteDatabase {
            return databaseInstance as NoteDatabase
        }
    }
}