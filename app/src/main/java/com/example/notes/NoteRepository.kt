package com.example.notes

import androidx.annotation.WorkerThread


class NoteRepository (database : NoteDatabase) {

    private var NoteBase : NoteDatabase = database
    private var NoteDao: NoteDAO = NoteBase.getNoteDao()


    @WorkerThread
    suspend fun insertNote(note: NoteInfo) {
        NoteDao.insertNote(note)
    }


    @WorkerThread
    suspend fun getUserNotes(user: String) : List<NoteInfo> {
        return NoteDao.getUserNotes(user)
    }

}