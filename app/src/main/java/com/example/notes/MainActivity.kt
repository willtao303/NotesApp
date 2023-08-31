package com.example.notes


import android.Manifest
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import com.example.notes.databinding.ActivityMainBinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
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
        val noteListFragment = NoteListFragment.newInstance(noteViewModel)
        val profileListFragment = ProfileListFragment.newInstance(noteViewModel)
        changeAppBar(noteListFragment)
        fragmentManager.commit {add(R.id.frame, noteListFragment)}


        binding.bottomNavigationView.setOnItemSelectedListener { id ->
            when (id.itemId) {
                R.id.notesList -> {
                    changeFrameFragment(noteListFragment); true
                } R.id.profilesList -> {
                    changeFrameFragment(profileListFragment); true
                } R.id.settingsList -> {
                    changeFrameFragment(SettingsFragment.newInstance("a", "b")); true
                }
                else -> {true}
            }
        }

        val newNoteButton = findViewById<FloatingActionButton>(R.id.newNoteButton)
        val newTextNoteButton = findViewById<Button>(R.id.newTextNoteButton)
        val newAudioNoteButton = findViewById<Button>(R.id.newAudioNoteButton)
        val otherNoteButtons = findViewById<View>(R.id.otherNoteButtons)
        val notNewNoteButton = findViewById<Button>(R.id.notNewNoteButton)

        newNoteButton.setOnClickListener {
            otherNoteButtons.visibility = View.VISIBLE
        }
        notNewNoteButton.setOnClickListener{
            otherNoteButtons.visibility = View.GONE
        }


        newTextNoteButton.setOnClickListener {
            val textNoteEditorFragment = TextNoteFragment.newInstance(noteViewModel)
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.new_note_dialog)

            val submitNewTextNoteButton = dialog.findViewById<Button>(R.id.createNewNote)
            submitNewTextNoteButton.setOnClickListener{
                val newNoteName = dialog.findViewById<TextView>(R.id.newNoteName).text.toString()
                val newTextNote = if (newNoteName.isNotEmpty()){
                    NoteInfo.createEmptyNote(notesRepo.getUser(), newNoteName, NoteType.written)
                } else {
                    NoteInfo.createEmptyNote(notesRepo.getUser(), "Untitled Note", NoteType.written)
                }
                noteViewModel.insertNote(newTextNote)
                textNoteEditorFragment.setNote(newTextNote)
                changeFrameFragment(textNoteEditorFragment)
                findViewById<TextView>(R.id.appbar_title).text = newNoteName

                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.cancelNewNote).setOnClickListener{
                dialog.dismiss()
            }

            dialog.show()
        }

        newAudioNoteButton.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)
                } else {
                    Log.d("a", "${Build.VERSION.SDK_INT} -- ${Build.VERSION_CODES.S}")
                    val audioNoteEditorFragment = AudioNoteFragment.newInstance(noteViewModel)
                    val dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(R.layout.new_note_dialog)

                    val submitNewTextNoteButton = dialog.findViewById<Button>(R.id.createNewNote)
                    submitNewTextNoteButton.setOnClickListener{
                        val newNoteName = dialog.findViewById<TextView>(R.id.newNoteName).text.toString()
                        val newAudioNote = if (newNoteName.isNotEmpty()){
                            NoteInfo.createEmptyNote(notesRepo.getUser(), newNoteName, NoteType.audio)
                        } else {
                            NoteInfo.createEmptyNote(notesRepo.getUser(), "Untitled Note", NoteType.audio)
                        }
                        noteViewModel.insertNote(newAudioNote)
                        audioNoteEditorFragment.setNote(newAudioNote)
                        changeFrameFragment(audioNoteEditorFragment)
                        findViewById<TextView>(R.id.appbar_title).text = newNoteName

                        dialog.dismiss()
                    }
                    dialog.show()
                }
            }
        }

        val backButton = findViewById<ImageView>(R.id.appbar_back)
        backButton.setOnClickListener {
            //fragmentManager.popBackStackImmediate()
            changeFrameFragment(noteListFragment)
            Log.d("Tag", "back button clicked")

        }

    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
    }
    fun changeFrameFragment(newFragment: Fragment){
        // managing hovering button -- theres prob a better way
        if (newFragment is NoteListFragment){
            findViewById<FloatingActionButton>(R.id.newNoteButton).visibility = View.VISIBLE
        } else {
            findViewById<FloatingActionButton>(R.id.newNoteButton).visibility = View.GONE
            findViewById<View>(R.id.otherNoteButtons).visibility = View.GONE
        }

        changeAppBar(newFragment)
        fragmentManager.commit {replace(R.id.frame, newFragment)}
    }

    private fun changeAppBar(newFragment: Fragment){
        if (newFragment is TextNoteFragment || newFragment is AudioNoteFragment){
            findViewById<ImageView>(R.id.appbar_back).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.appbar_delete).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.appbar_settings).visibility = View.VISIBLE

            findViewById<TextView>(R.id.appbar_title).visibility = View.VISIBLE
            findViewById<Toolbar>(R.id.toolbar).title = ""
        } else {
            findViewById<ImageView>(R.id.appbar_back).visibility = View.GONE
            findViewById<ImageView>(R.id.appbar_delete).visibility = View.GONE
            findViewById<ImageView>(R.id.appbar_settings).visibility = View.GONE

            findViewById<TextView>(R.id.appbar_title).visibility = View.GONE
            if (newFragment is NoteListFragment){
                findViewById<Toolbar>(R.id.toolbar).title = "Notes"
            } else if (newFragment is ProfileListFragment){
                    findViewById<Toolbar>(R.id.toolbar).title = "Profiles"
            } else if (newFragment is SettingsFragment){
                findViewById<Toolbar>(R.id.toolbar).title = "Settings"
            } else {
                findViewById<Toolbar>(R.id.toolbar).title = ""
            }
        }
    }

}