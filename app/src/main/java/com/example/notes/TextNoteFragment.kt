package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import java.util.Calendar

private const val ARG_NOTE_ID = "current_note_id"

/**
 * A simple [Fragment] subclass.
 * Use the [TextNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TextNoteFragment (private val note_data: NoteDataViewModel) : Fragment() {
    private var id: String? = null;
    //private var info: TextNoteInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ARG_NOTE_ID)
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
        fun newInstance(note_data: NoteDataViewModel, note_id: Int) =
            TextNoteFragment(note_data).apply {
                arguments = Bundle().apply {
                    putInt(ARG_NOTE_ID, note_id)
                }
            }

        @JvmStatic
        fun newInstance(note_data: NoteDataViewModel) =
            TextNoteFragment(note_data).apply {
                val noteId = createNote()
                arguments = Bundle().apply {
                    putString(ARG_NOTE_ID, noteId)
                }
            }
    }


    fun createNote(): String{
        val timeInstance = Calendar.getInstance()
        val newNote = NoteInfo(timeInstance.time.toString(), note_data.getUser(), false, "Untitled Note", NoteType.written, null, null)
        note_data.insertNote(newNote)

        return newNote.noteTime
    }
    fun submitNote(){

    }
}