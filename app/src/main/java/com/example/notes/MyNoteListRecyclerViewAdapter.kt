package com.example.notes

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.notes.databinding.FragmentNotesListItemBinding

class MyNoteListRecyclerViewAdapter(
    private val values: List<NoteInfo>
) : RecyclerView.Adapter<MyNoteListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentNotesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.noteName
        holder.contentView.text = item.noteType
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentNotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}