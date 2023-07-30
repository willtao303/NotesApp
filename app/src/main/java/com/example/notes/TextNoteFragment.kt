package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_NOTE_NAME = "current_note_name"
private const val ARG_NOTE_ID = "current_note_id"

/**
 * A simple [Fragment] subclass.
 * Use the [TextNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TextNoteFragment () : Fragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var id: Int? = null;
    //private var info: TextNoteInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_NOTE_NAME)
            id = it.getInt(ARG_NOTE_ID)
        }

        // get info from database off of id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_note, container, false)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param note_name Name of the TextNote.
         * @return A new instance of fragment TextNoteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(note_name: String, note_id: Int) =
            TextNoteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NOTE_NAME, note_name)
                    putInt(ARG_NOTE_ID, note_id)
                }
            }
    }
}