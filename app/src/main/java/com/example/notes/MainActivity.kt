package com.example.notes


import android.content.pm.ActivityInfo
import com.example.notes.databinding.ActivityMainBinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.*

var counter = 1
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val fragmentManager = supportFragmentManager
    private lateinit var notesRepo : NoteRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // TODO: shared preference / data sharing get current profile

        // Database Init
        notesRepo = NoteRepository(NoteDatabase.getDatabase(applicationContext))
        val noteViewModel = NoteDataViewModel(notesRepo)


        // Setup fragment and main screen
        val noteListFragment : NoteListFragment = NoteListFragment.newInstance(noteViewModel)
        fragmentManager.commit {add(R.id.frame, noteListFragment)}

        setContentView(binding.root)


        binding.bottomNavigationView.setOnItemSelectedListener { id ->
            when (id.itemId) {
                R.id.notesList -> {
                    changeFrameFragment(noteListFragment); true
                } R.id.profilesList -> {
                fragmentManager.commit {
                    replace<option2>(R.id.frame)}; true
                }else -> {true}
            }
        }


        val textNoteEditorFragment = TextNoteFragment.newInstance(noteViewModel)
        val newNoteButton : ImageButton = findViewById(R.id.newNoteButton)
        newNoteButton.setOnClickListener {
            val newTextNote = NoteInfo.createEmptyTextNote(notesRepo.getUser())
            textNoteEditorFragment.setNote(newTextNote)
            changeFrameFragment(textNoteEditorFragment)
            noteViewModel.insertNote(newTextNote)


        }

    }

    private fun changeFrameFragment(newFragment: Fragment){
        // managing hovering button -- theres prob a better way to do this but idk how,,
        if (newFragment is NoteListFragment){
            findViewById<Button>(R.id.newNoteButton).visibility = View.VISIBLE
        } else {
            findViewById<Button>(R.id.newNoteButton).visibility = View.GONE
        }
        fragmentManager.commit {replace(R.id.frame, newFragment)}
    }

}