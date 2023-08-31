package com.example.notes

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


class NoteRepository (database : NoteDatabase) {

    private var NoteBase : NoteDatabase = database
    private var NoteDao: NoteDAO = NoteBase.getNoteDao()
    private var user: String = "user" // hard coded for now
    var liveNotesList : LiveData<List<NoteInfo>> = NoteDao.getUserNotes(user)
    var liveAllNotesList : LiveData<List<NoteInfo>> = NoteDao.getAllNotes()

    @WorkerThread
    suspend fun insertNote(note: NoteInfo) {
        NoteDao.insertNote(note)
    }
    @WorkerThread
    suspend fun updateNote(note: NoteInfo) {
        NoteDao.updateNote(note)
    }
    @WorkerThread
    suspend fun deleteNote(note: NoteInfo) {
        NoteDao.deleteNote(note)
    }

    fun changeUser(newUser : String){
        user = newUser
        liveNotesList = NoteDao.getUserNotes(user)
    }
    fun getUser(): String{
        return user
    }

    @WorkerThread
    suspend fun getAllNotesDebug(): List<NoteInfo> {
        return NoteDao.getAllNotesNonLive()
    }

}