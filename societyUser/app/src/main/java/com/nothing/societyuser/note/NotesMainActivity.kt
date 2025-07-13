package com.nothing.societyuser.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.nothing.societyuser.R
import com.nothing.societyuser.note.database.NoteDatabase
import com.nothing.societyuser.note.repository.NoteRepository
import com.nothing.societyuser.note.viewmodel.NoteViewModel
import com.nothing.societyuser.note.viewmodel.NoteViewModelFactory

class NotesMainActivity : AppCompatActivity() {
    lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_main)

        setupViewModel()
    }

    private fun setupViewModel() {
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application,noteRepository)
         noteViewModel=ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }
}