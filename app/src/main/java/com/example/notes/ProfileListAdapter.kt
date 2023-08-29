package com.example.notes

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ProfileListAdapter(ctx: Context, private var values: ArrayList<String>, private val noteRepo: NoteDataViewModel): ArrayAdapter<String>(ctx, 0, values){

    @Override
    override fun getView(index: Int, _view: View?, parent: ViewGroup) : View {
        var tileView = _view
        val userName : String? = getItem(index)

        if (tileView == null) {
            tileView = LayoutInflater.from(context).inflate(R.layout.fragment_profile_list_item, parent, false)
        }
        tileView!!

        val icon : ImageView = tileView.findViewById(R.id.user_list_item_icon)
        val name : TextView = tileView.findViewById(R.id.user_list_item_name)

        name.text = userName


        if (index == values.size - 1){
            icon.setImageResource(R.drawable.ic_profile_add)
        } else if (userName == noteRepo.getUser()){
            icon.setImageResource(R.drawable.ic_profile_selected)
        } else {
            icon.setImageResource(R.drawable.ic_profile_hollow)
        }

        return tileView
    }
}

