package com.example.notes

import com.example.notes.databinding.ActivityMainBinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.*

var counter = 1;
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)

        fragmentManager.commit {
            add<NoteListFragment>(R.id.frame)}

        //setContentView(R.layout.activity_main)
        setContentView(binding.root)


        binding.bottomNavigationView.setOnItemSelectedListener { id ->
            when (id.itemId) {
                R.id.noteslist -> { fragmentManager.commit {
                    replace<NoteListFragment>(R.id.frame)}; true
                } R.id.option2 -> { fragmentManager.commit {
                    replace<option2>(R.id.frame)}; true
                }else -> {true}
            }
        }

        var list_view : ListView = findViewById<ListView>(R.id.notes_list)
        list_view.setOnItemClickListener { _, _, i, _ ->  {
            // index database using i and retrieve note type + note info based on note type -> store in NoteInfo struct
            // val newTextNote = TextNoteFragment.newInstance(newNoteInfo.name, newNoteInfo)
        }}

        var newNoteButton : ImageButton = findViewById<ImageButton>(R.id.newNoteButton)
        newNoteButton.setOnClickListener {
            val newTextNote = TextNoteFragment.newInstance("blank$counter", counter+5)
            counter++
            fragmentManager.commit { replace(R.id.frame, newTextNote) }
        }

    }

}