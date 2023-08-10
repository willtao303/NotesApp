package com.example.notes

import com.example.notes.databinding.ActivityMainBinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.fragment.app.*
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

var counter = 1;
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val fragmentManager = supportFragmentManager
    private lateinit var notesRepo : NoteRepository
    private lateinit var currentProfile: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        NoteDatabase.initDatabase(applicationContext)
        notesRepo = NoteRepository(NoteDatabase.getDatabase())
        binding = ActivityMainBinding.inflate(layoutInflater)

        // shared preference / data sharing get current profile
        currentProfile = "user"

        val noteListFragment : NoteListFragment = NoteListFragment.newInstance(notesRepo, currentProfile)

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
            //val newTextNote = TextNoteFragment.newInstance("blank$counter", counter+5)
            runBlocking {
                notesRepo.insertNote(NoteInfo(counter, LocalDateTime.now().minute, "user", false,"blank$counter", NoteType.written, null, null))
                counter++
            }


            //fragmentManager.commit { replace(R.id.frame, newTextNote) }
        }

    }

}