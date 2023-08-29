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
    }
    fun getUser(): String{
        return user
    }
    fun setUser(_user: String){
        this.user = _user
        liveNotesList = NoteDao.getUserNotes(user)
    }

    @WorkerThread
    suspend fun getAllNotesDebug(): List<NoteInfo> {
        return NoteDao.getAllNotesNonLive()
    }

}