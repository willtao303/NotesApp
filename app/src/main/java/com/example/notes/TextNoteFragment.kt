package com.example.notes

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.io.File
import java.io.FileInputStream

private const val ARG_NOTE_ID = "current_note_id"

/**
 * A simple [Fragment] subclass.
 * Use the [TextNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TextNoteFragment (private val note_data: NoteDataViewModel) : Fragment() {

    private var id: String? = null
    private var note: NoteInfo? = null
    private var mainFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ARG_NOTE_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_text_note, container, false)


        val textBody = view.findViewById<TextView>(R.id.textNoteEditorMainBody)
        if (mainFile != null){
            textBody.text =  FileInputStream(mainFile).bufferedReader().use {it.readText()}
        } else {
            textBody.text =  "Error Reading Attached Note.txt File"
        }

        val settingsButton = requireActivity().findViewById<ImageView>(R.id.appbarSettings)
        settingsButton?.setOnClickListener {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.edit_note_name_dialog)
            val renameTextView = dialog.findViewById<TextView>(R.id.noteNewName)
            renameTextView.text = note!!.noteName

            val submitNoteRenameButton = dialog.findViewById<Button>(R.id.submitRename);
            submitNoteRenameButton.setOnClickListener{
                val newNoteName = renameTextView.text.toString()
                note!!.noteName = if (newNoteName != "") {newNoteName} else {"Untitled Note"}
                requireActivity().findViewById<TextView>(R.id.appbarTitle).text = newNoteName
                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.cancelRename).setOnClickListener{
                dialog.dismiss()
            }

            dialog.show()
        }

        val deleteButton = requireActivity().findViewById<ImageView>(R.id.appbarDelete)
        deleteButton?.setOnClickListener {
            Log.d("ButtonEvent", "delete clicked")
            if (note != null){
                note_data.deleteNote(note!!)
                if (mainFile != null){
                    mainFile!!.delete()
                    mainFile = null
                }
            }
            (requireActivity() as MainActivity).changeFrameFragment(NoteListFragment(note_data))
        }

        return view
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)

        val fileDir = File(context.filesDir, note_data.getUser())
        fileDir.mkdirs()

        if (note != null) {
            if (note!!.primaryFile != null){
                mainFile = File(fileDir, note!!.primaryFile!!)
            } else {
                note!!.primaryFile =  "${note!!.noteId}.txt"
                mainFile = File(fileDir, note!!.primaryFile!!)
                mainFile!!.writeText("")
                Log.d("File", "Created a file at ${mainFile!!.absolutePath}")
            }
        }
    }

    override fun onStop() {

        val textBody = requireView().findViewById<TextView>(R.id.textNoteEditorMainBody)
        if (mainFile != null){
            mainFile!!.writeText(textBody.text.toString())
        }
        note_data.update(note!!)

        super.onStop()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TextNoteFragment.
         */
        @JvmStatic
        fun newInstance(note_data: NoteDataViewModel) =
            TextNoteFragment(note_data).apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun setNote(newNote: NoteInfo){
        note = newNote
    }

}