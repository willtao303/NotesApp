package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class NoteListFragment (_noteDatabase : NoteDAO, currentProfile: String): Fragment() {

    private lateinit var notesArray : List<NoteInfo>
    private var columnCount = 1
    private var profile : String = currentProfile
    private var noteDatabase = _noteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        // TODO: get note info from somewhere and put into NotesArray
        notesArray = noteDatabase.getAllNotes(profile)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyNoteListRecyclerViewAdapter(notesArray)
            }
        }

        var listView = view.findViewById<ListView>(R.id.notes_list)

        listView.setOnItemClickListener { _, _, i, _ ->  {
            // index database using i and retrieve note type + note info based on note type -> store in NoteInfo struct
            // val newTextNote = TextNoteFragment.newInstance(newNoteInfo.name, newNoteInfo)
        }}


        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val columnCount = 1
        @JvmStatic
        fun newInstance(NoteDatabase : NoteDAO, CurrentProfile : String) =
            NoteListFragment(NoteDatabase, CurrentProfile).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}