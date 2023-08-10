package com.example.notes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.OnConflictStrategy

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

const val TABLE = "note_table";

@Dao
interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteInfo: NoteInfo)

    @Update
    suspend fun updateNote(noteInfo:NoteInfo)

    @Delete
    suspend fun deleteNote(noteInfo: NoteInfo)

    @Query("SELECT * FROM $TABLE ORDER BY creation_user DESC, note_starred DESC, creation_time DESC")
    suspend fun getAllNotes(): List<NoteInfo>

    @Query("SELECT * FROM $TABLE WHERE creation_user LIKE :user ORDER BY note_starred DESC, creation_time DESC")
    suspend fun getUserNotes(user: String): List<NoteInfo>

}