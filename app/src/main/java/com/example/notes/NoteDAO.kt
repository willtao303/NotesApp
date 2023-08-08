package com.example.notes

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
    fun insertNote(noteInfo: NoteInfo)

    @Update
    fun updateNote(noteInfo:NoteInfo)

    @Delete
    fun deleteNote(noteInfo: NoteInfo)

    @Query("SELECT * FROM $TABLE WHERE creation_user LIKE :user ORDER BY note_starred DESC, creation_time DESC")
    fun getAllNotes(user: String): List<NoteInfo>


}