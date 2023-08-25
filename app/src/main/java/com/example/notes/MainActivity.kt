package com.example.notes


import android.content.pm.ActivityInfo
import com.example.notes.databinding.ActivityMainBinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

//var counter = 1
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val fragmentManager = supportFragmentManager
    private lateinit var notesRepo : NoteRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(binding.root)

        // TODO: shared preference / data sharing get current profile

        // Database Init
        notesRepo = NoteRepository(NoteDatabase.getDatabase(applicationContext))
        val noteViewModel = NoteDataViewModel(notesRepo)


        // Setup fragment and main screen
        val noteListFragment : NoteListFragment = NoteListFragment.newInstance(noteViewModel)
        changeAppBar(noteListFragment)
        fragmentManager.commit {add(R.id.frame, noteListFragment)}


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

            //TODO: make spinner for text note or audio note option, double click for text note shortcut + dialog for note creation
            // dialog needs note name + note color???? + create button + cancel button
            val newTextNote = NoteInfo.createEmptyTextNote(notesRepo.getUser())
            textNoteEditorFragment.setNote(newTextNote)
            changeFrameFragment(textNoteEditorFragment)
            noteViewModel.insertNote(newTextNote)

        }

    }


    fun changeFrameFragment(newFragment: Fragment){
        // managing hovering button -- theres prob a better way
        if (newFragment is NoteListFragment){
            findViewById<FloatingActionButton>(R.id.newNoteButton).visibility = View.VISIBLE
        } else {
            findViewById<FloatingActionButton>(R.id.newNoteButton).visibility = View.GONE
        }

        changeAppBar(newFragment)
        fragmentManager.commit {replace(R.id.frame, newFragment)}
    }

    private fun changeAppBar(newFragment: Fragment){
        if (newFragment is TextNoteFragment){
            findViewById<ImageView>(R.id.appbarBack).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.appbarDelete).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.appbarSettings).visibility = View.VISIBLE

            findViewById<TextView>(R.id.appbarTitle).visibility = View.VISIBLE
            findViewById<Toolbar>(R.id.toolbar).title = ""
        } else {
            findViewById<ImageView>(R.id.appbarBack).visibility = View.GONE
            findViewById<ImageView>(R.id.appbarDelete).visibility = View.GONE
            findViewById<ImageView>(R.id.appbarSettings).visibility = View.GONE

            findViewById<TextView>(R.id.appbarTitle).visibility = View.GONE
            if (newFragment is NoteListFragment){
                findViewById<Toolbar>(R.id.toolbar).title = "Notes"
            } /*else if (newFragment is NoteListFragment){
                    findViewById<Toolbar>(R.id.toolbar).title = "@string/navbar_notes"
            }*/ else {
                findViewById<Toolbar>(R.id.toolbar).title = ""
            }
        }
    }

}