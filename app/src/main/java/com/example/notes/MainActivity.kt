package com.example.notes

import com.example.notes.databinding.ActivityMainBinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.*

var counter = 1;
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val fragmentManager = supportFragmentManager
    private lateinit var database : NoteDAO
    private lateinit var currentProfile: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        NoteDatabase.initDatabase(applicationContext)
        database = NoteDatabase.getDatabase().getNoteDao()
        binding = ActivityMainBinding.inflate(layoutInflater)

        // shared preference / data sharing get current profile
        currentProfile = "user"

        val noteListFragment : NoteListFragment = NoteListFragment.newInstance(database, currentProfile)

        fragmentManager.commit {
            add(R.id.frame, noteListFragment)}

        //setContentView(R.layout.activity_main)
        setContentView(binding.root)


        binding.bottomNavigationView.setOnItemSelectedListener { id ->
            when (id.itemId) {
                R.id.noteslist -> { fragmentManager.commit {
                    replace(R.id.frame, noteListFragment)}; true
                } R.id.option2 -> { fragmentManager.commit {
                    replace<option2>(R.id.frame)}; true
                }else -> {true}
            }
        }


        var newNoteButton : ImageButton = findViewById(R.id.newNoteButton)
        newNoteButton.setOnClickListener {
            val newTextNote = TextNoteFragment.newInstance("blank$counter", counter+5)
            counter++
            fragmentManager.commit { replace(R.id.frame, newTextNote) }
        }

    }

}