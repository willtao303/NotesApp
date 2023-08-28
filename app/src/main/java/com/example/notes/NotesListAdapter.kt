package com.example.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


class NotesListAdapter(ctx: Context, private var values: ArrayList<NoteInfo>): ArrayAdapter<NoteInfo>(ctx, 0, values){
    fun setNoteList(newNotes: List<NoteInfo>){
        clear()
        values.addAll(newNotes)
        notifyDataSetChanged()
    }

    @Override
    override fun getView(index: Int, _view: View?, parent: ViewGroup) : View {
        var tileView = _view
        val note : NoteInfo? = getItem(index)
        if (tileView == null) {
            tileView = LayoutInflater.from(context).inflate(R.layout.fragment_notes_list_item, parent, false)
        }
        tileView!!

        val icon : ImageView = tileView.findViewById(R.id.note_type)
        val name : TextView = tileView.findViewById(R.id.note_name)
        val type : TextView = tileView.findViewById(R.id.note_timestamp)

        name.text = note?.noteName
        type.text = note?.noteTime

        if (note?.noteType == NoteType.written){
            icon.setImageResource(R.drawable.ic_note_text)
        } else if (note?.noteType == NoteType.audio){
            icon.setImageResource(R.drawable.ic_note_audio)
        }

        return tileView
    }
}

