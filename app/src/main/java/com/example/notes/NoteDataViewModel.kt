package com.example.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteDataViewModel(_repository: NoteRepository) : ViewModel() {
    private val repository = _repository
    var liveNoteList = repository.liveNotesList
    var liveAllNoteList = repository.liveAllNotesList

    fun insertNote(newNote: NoteInfo) = viewModelScope.launch{
        repository.insertNote(newNote)
    }
    fun deleteNote(newNote: NoteInfo) = viewModelScope.launch{
        repository.deleteNote(newNote)
    }
    fun update(newNote: NoteInfo) = viewModelScope.launch{
        repository.updateNote(newNote)
    }

    fun debug() = viewModelScope.launch{
        val tmp =  repository.getAllNotesDebug()
        Log.d("Database", tmp.toString())
    }

    fun getUser(): String{
        return repository.getUser()
    }
    fun setUser(newUser: String){
        repository.changeUser(newUser)
    }
}