package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class NoteListFragment : Fragment() {

    private var NotesArray : ArrayList<NoteListItem> = ArrayList<NoteListItem>()
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        // TODO: get note info from somewhere and put into NotesList
        NotesArray.add(NoteListItem("Groceries", NoteType.written, 0))
        NotesArray.add(NoteListItem("School Note", NoteType.written, 1))
        NotesArray.add(NoteListItem("??????", NoteType.audio, 2))
        NotesArray.add(NoteListItem("??????", NoteType.audio, 3))
        NotesArray.add(NoteListItem("funnee", NoteType.drawing, 4))
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
                adapter = MyNoteListRecyclerViewAdapter(NotesArray)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            NoteListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}