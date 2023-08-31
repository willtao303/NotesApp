package com.example.notes

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

class ProfileListFragment(private val notesRepo: NoteDataViewModel) : Fragment() {
    private var users: ArrayList<String> = ArrayList()
    private var currentUserIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadUsers(requireActivity())
        currentUserIndex = users.indexOf(notesRepo.getUser())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_list, container, false)

        // Set the adapter
        if (view is ListView) {
            view.adapter = ProfileListAdapter(requireContext(), users, notesRepo)
            view.setOnItemClickListener { _adapterView, _view, i, _ ->
                run {
                    if (i == users.size - 1){
                        val dialog = Dialog(requireActivity())
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setContentView(R.layout.new_user_dialog)

                        dialog.findViewById<Button>(R.id.createNewUser).setOnClickListener{
                            val newProfileName = dialog.findViewById<TextView>(R.id.newUserName).text.toString()
                            users.add(i, newProfileName)
                            dialog.dismiss()
                        }
                        dialog.findViewById<Button>(R.id.cancelNewUser).setOnClickListener{
                            dialog.dismiss()
                        }

                        dialog.show()
                    } else {
                        notesRepo.setUser(users[i])
                        _adapterView.getChildAt(currentUserIndex).findViewById<ImageView>(R.id.user_list_item_icon).setImageResource(R.drawable.ic_profile_hollow)
                        _view.findViewById<ImageView>(R.id.user_list_item_icon).setImageResource(R.drawable.ic_profile_selected)
                        currentUserIndex = i
                    }
                }
            }
        }

        return view
    }

    override fun onStop() {
        super.onStop()

        saveUsers(requireActivity())
    }


    companion object {
        @JvmStatic
        fun newInstance(notesRepo: NoteDataViewModel) = ProfileListFragment(notesRepo)
    }

    private fun loadUsers(activity: Activity){
        val preferences: SharedPreferences = activity.getSharedPreferences(getString(R.string.preference_key_users_list), Context.MODE_PRIVATE)
        val importString = preferences.getString(getString(R.string.preferences_retrieve_users), null)

        users.clear()
        if (importString != "" && importString != null){
            users.addAll(importString.split("|"))
            users.add("New User")
        } else {
            users.add("user")
            users.add("New User")
            saveUsers(activity)
        }


    }

    private fun saveUsers(activity: Activity){
        users.removeLast()
        val preferences: SharedPreferences = activity.getSharedPreferences(getString(R.string.preference_key_users_list), Context.MODE_PRIVATE)
        val prefEditor = preferences.edit()
        val exportString = users.joinToString("|")
        prefEditor.putString(getString(R.string.preferences_retrieve_users), exportString)
        prefEditor.apply()
        users.add("New User")
    }
}