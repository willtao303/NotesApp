package com.example.notes

import com.example.notes.databinding.ActivityMainBinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
//import java.time.LocalDateTime
import java.util.Calendar

var counter = 1
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val fragmentManager = supportFragmentManager
    private lateinit var notesRepo : NoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // TODO: shared preference / data sharing get current profile

        // Database Init
        notesRepo = NoteRepository(NoteDatabase.getDatabase(applicationContext))
        val noteViewModel = NoteDataViewModel(notesRepo)


        // Setup fragment and main screen
        val noteListFragment : NoteListFragment = NoteListFragment.newInstance(noteViewModel)
        fragmentManager.commit {
            add(R.id.frame, noteListFragment)}

        setContentView(binding.root)




        //Log.d("TAG", "aaa -- " + notesRepo.getAllNotes().value.toString() + " ===== " + notesRepo.getNotes().size)

        // wtf is going on ------------------------

        binding.bottomNavigationView.setOnItemSelectedListener { id ->
            when (id.itemId) {
                R.id.noteslist -> { fragmentManager.commit {
                    replace(R.id.frame, noteListFragment)}; true
                } R.id.option2 -> { fragmentManager.commit {
                    replace<option2>(R.id.frame)}; true
                }else -> {true}
            }
        }

        val textNoteEditorFragment = TextNoteFragment(noteViewModel)
        val newNoteButton : ImageButton = findViewById(R.id.newNoteButton)
        newNoteButton.setOnClickListener {
            //var newNote = NoteInfo(0, timeInstance.time.toString() , "user", false,"blank$counter", NoteType.written, null, null)
            textNoteEditorFragment.createNote()
            //Log.d("Tag", "insert; len = ${(noteViewModel.liveAllNoteList.value?:emptyList()).size}")
            //noteListFragment.updateListView() TODO: automate this


            //TODO: note edit screen
            //val newTextNote = TextNoteFragment.newInstance("blank$counter", counter+5)
            //fragmentManager.commit { replace(R.id.frame, newTextNote) }
        }

    }

}