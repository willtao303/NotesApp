package com.example.notes

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.S)
class AudioNoteFragment(private val note_data: NoteDataViewModel) : Fragment() {
    private var id: String? = null
    private var note: NoteInfo? = null
    private var mainFile: File? = null

    private var validFile = false
    private var recorder: Recorder? = null
    private var player: Player? = null
    private var threadAlive = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_audio_note, container, false)
        val activity = requireActivity()
        threadAlive = true

        view.findViewById<ImageButton>(R.id.audio_note_swap_button).setOnClickListener {
            if (view.findViewById<LinearLayout>(R.id.audio_note_record_controls).visibility == View.GONE) {
                view.findViewById<LinearLayout>(R.id.audio_note_record_controls).visibility = View.VISIBLE
                view.findViewById<LinearLayout>(R.id.audio_note_play_controls).visibility = View.GONE

            } else {
                view.findViewById<LinearLayout>(R.id.audio_note_record_controls).visibility = View.GONE
                view.findViewById<LinearLayout>(R.id.audio_note_play_controls).visibility = View.VISIBLE
            }
        }

        val seekbar = view.findViewById<SeekBar>(R.id.audio_note_seek_bar)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, value: Int, userInput: Boolean) {
                if (userInput && validFile){
                    player?.seek(value)
                }
            }

            override fun onStartTrackingTouch(p: SeekBar?) {}
            override fun onStopTrackingTouch(p: SeekBar?) {}
        })
        seekbar.max = player?.getDuration()?:0

        view.findViewById<ImageButton>(R.id.audio_note_play_button).setOnClickListener {
            if (validFile){
                player?.play()
            }

            lifecycleScope.launch {
                while(player?.isPlaying() == true){
                    Log.d("a", (player?.getProgress()?: 0).toString())
                    seekbar.progress = player?.getProgress()?: 0
                    delay(200)
                }
                seekbar.progress = player?.getProgress()?: 0
            }
        }
        view.findViewById<ImageButton>(R.id.audio_note_forward_button).setOnClickListener {
            if (validFile){
                player?.forward()
            }
        }
        view.findViewById<ImageButton>(R.id.audio_note_rewind_button).setOnClickListener {
            if (validFile){
                player?.rewind()
                seekbar.progress = player?.getProgress()?: 0
            }
        }


        val recordButton = view.findViewById<ImageButton>(R.id.audio_note_record_button)
        recordButton.setOnClickListener {
            if (recorder?.isRecording() == true){
                recorder?.stopRecording()
                player?.reset()
                validFile = true
                recordButton.setImageResource(R.drawable.ic_audio_note_record)
                seekbar.max = player?.getDuration()?:0
            } else {
                recorder?.startRecording()
                recordButton.setImageResource(R.drawable.ic_audio_note_stop)
            }
        }
        view.findViewById<ImageButton>(R.id.audio_note_pause_button).setOnClickListener {
            recorder?.pauseRecording()
        }

        // appbar
        val settingsButton = activity.findViewById<ImageView>(R.id.appbar_settings)
        settingsButton.setOnClickListener {
            val dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.edit_note_name_dialog)
            val renameTextView = dialog.findViewById<TextView>(R.id.noteNewName)
            renameTextView.text = note!!.noteName

            val submitNoteRenameButton = dialog.findViewById<Button>(R.id.submitRename);
            submitNoteRenameButton.setOnClickListener{
                val newNoteName = renameTextView.text.toString()
                note!!.noteName = if (newNoteName != "") {newNoteName} else {"Untitled Note"}
                activity.findViewById<TextView>(R.id.appbar_title).text = newNoteName
                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.cancelRename).setOnClickListener{
                dialog.dismiss()
            }

            dialog.show()
        }
        val deleteButton = activity.findViewById<ImageView>(R.id.appbar_delete)
        deleteButton.setOnClickListener {
            Log.d("ButtonEvent", "delete clicked")
            if (note != null){
                note_data.deleteNote(note!!)
                if (mainFile != null){
                    mainFile!!.delete()
                    mainFile = null
                }
            }
            (activity as MainActivity).changeFrameFragment(NoteListFragment(note_data))
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val fileDir = File(context.filesDir, note_data.getUser())
        fileDir.mkdirs()

        if (note != null) {
            if (note!!.primaryFile != null){
                validFile = true
            } else {
                validFile = false
                note!!.primaryFile = note!!.noteId + ".mp3"
                File(fileDir, note!!.noteId + ".mp3")
            }
            mainFile = File(fileDir, note!!.primaryFile!!)
        }

        recorder = Recorder()
        recorder!!.setup(context, mainFile)
        player = Player()
        player!!.setup(context, mainFile)
    }

    override fun onStop() {
        player?.close()
        recorder?.close()
        if (note != null){
            note_data.update(note!!)
        }
        threadAlive = false
        super.onStop()
    }

    companion object {
        /** @return A new instance of fragment AudioNoteFragment. */
        @JvmStatic
        fun newInstance(note_data: NoteDataViewModel) =
            AudioNoteFragment(note_data).apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun setNote(newNote: NoteInfo){
        note = newNote
        validFile = false
    }

    @RequiresApi(Build.VERSION_CODES.S)
    class Recorder{
        private var mediaRecorder: MediaRecorder? = null

        private var recording = false
        private var paused = false

        fun setup(context: Context, file: File?){
            mediaRecorder = MediaRecorder(context)

            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(file)
        }

        fun startRecording(){
            try {
                mediaRecorder?.prepare()
                mediaRecorder?.start()
                recording = true
                paused = false
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        fun pauseRecording(){
            try {
                if (paused) {
                    mediaRecorder?.resume()
                    paused = false
                } else {
                    mediaRecorder?.pause()
                    paused = true
                }
            } catch (e: Exception){
                e.printStackTrace()
            }

        }

        fun stopRecording(){
            try {
                mediaRecorder?.stop()
                mediaRecorder?.reset()
                recording = false
                paused = false
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        fun isRecording(): Boolean{
            return recording
        }

        fun close(){
            mediaRecorder?.release()
            mediaRecorder = null

        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    class Player {
        private var mediaPlayer: MediaPlayer? = null
        private var duration : Int? = -1
        private var paused = false

        fun setup(context: Context, file: File?){
            mediaPlayer = MediaPlayer.create(context, Uri.fromFile(file))
            duration = mediaPlayer?.duration
        }

        fun play(){
            try {
                mediaPlayer?.start()
                paused = false
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        fun pause(){
            try {
                mediaPlayer?.pause()
                paused = true
            } catch (e: Exception){
                e.printStackTrace()
            }

        }

        fun seek(value: Int){
            try {
                mediaPlayer?.seekTo(value)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        fun rewind(){
            try {
                seek(((mediaPlayer?.currentPosition?.minus(5000) ?: 0).coerceAtLeast(0)))
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        fun forward(){
            try {
                seek(((mediaPlayer?.currentPosition?.plus(10000) ?: 0).coerceAtMost(mediaPlayer?.duration!!)))
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        fun getDuration():Int{
            return (duration?:0).coerceAtLeast(0)
        }
        fun getProgress():Int{
            return (mediaPlayer?.currentPosition?:0).coerceAtLeast(0)
        }
        fun isPlaying():Boolean {
            return mediaPlayer?.isPlaying?:false
        }

        fun close(){
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }

        fun reset(){
            mediaPlayer?.reset()
        }
    }
}
