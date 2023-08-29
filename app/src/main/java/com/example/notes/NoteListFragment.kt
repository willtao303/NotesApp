package com.example.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.LiveData

class NoteListFragment (private var noteDatabase : NoteDataViewModel): Fragment() {

    private var columnCount = 1
    private var notesListLive : LiveData<List<NoteInfo>> = noteDatabase.liveAllNoteList
    private lateinit var notesAdapter: NotesListAdapter
    private var notesList : List<NoteInfo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tempList = ArrayList<NoteInfo>()
        notesAdapter = NotesListAdapter(requireContext(), tempList)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        notesListLive.observe(this) { newNotesListRaw ->
            run {
                val newNotesList = getNotesByUser(newNotesListRaw, noteDatabase.getUser())
                notesList = newNotesList
                notesAdapter.setNoteList(newNotesList)
                Log.d("Database", "aaa -- $newNotesList ===== ")
            }
        }

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
            }
        }


        val listView = view.findViewById<ListView>(R.id.notes_list)

        listView.setOnItemClickListener { _, _, i, _ ->
            run {
                Log.d("TAG", "item $i clicked")

                val textNoteEditorFragment = TextNoteFragment.newInstance(noteDatabase)
                textNoteEditorFragment.setNote(notesList[i])

                (requireActivity() as MainActivity).changeFrameFragment(textNoteEditorFragment)
                requireActivity().findViewById<TextView>(R.id.appbarTitle).text = notesList[i].noteName

            }
        }

        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        //const val columnCount = 1
        @JvmStatic
        fun newInstance(NoteDatabase: NoteDataViewModel) =
            NoteListFragment(NoteDatabase).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    private fun getNotesByUser(notesList: List<NoteInfo>, user: String): List<NoteInfo>{
        val tempList = ArrayList<NoteInfo>()
        for (note in notesList){
            if (note.noteUser == user){
                tempList.add(note)
            }
        }
        return tempList.toList()
    }

}









