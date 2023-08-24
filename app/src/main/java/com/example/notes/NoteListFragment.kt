package com.example.notes

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class NoteListFragment (_noteDatabase : NoteDataViewModel): Fragment() {

    private var columnCount = 1
    private var noteDatabase = _noteDatabase
    private var notesList : LiveData<List<NoteInfo>> = noteDatabase.liveNoteList
    private lateinit var notesAdapter: NotesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var tempList = ArrayList<NoteInfo>()
        notesAdapter = NotesListAdapter(requireContext(), tempList)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        //Log.d("tag", notesList.value.toString())

        notesList.observe( this, Observer<List<NoteInfo>> {
            newNotesList -> run {
                notesAdapter.setNoteList(newNotesList)
                Log.d("Database", "aaa -- $newNotesList ===== ")
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes_list, container, false)

        // Set the adapter
        if (view is ListView) {
            with(view) {
                adapter = notesAdapter
                Log.d("asdfhashdruilskd", adapter.count.toString())
            }
        }


        val listView = view.findViewById<ListView>(R.id.notes_list)

        listView.setOnItemClickListener { _, _, i, _ ->
            run {
                Log.d("TAG", "item $i clicked")
                // index database using i and retrieve note type + note info based on note type -> store in NoteInfo struct
                // val newTextNote = TextNoteFragment.newInstance(newNoteInfo.name, newNoteInfo)
            }
        }

        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val columnCount = 1
        @JvmStatic
        fun newInstance(NoteDatabase: NoteDataViewModel) =
            NoteListFragment(NoteDatabase).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

}









