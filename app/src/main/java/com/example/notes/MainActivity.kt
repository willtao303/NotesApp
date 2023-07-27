package com.example.notes

import com.example.notes.databinding.ActivityMainBinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.*


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
    }

}