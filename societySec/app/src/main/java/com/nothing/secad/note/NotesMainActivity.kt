package com.nothing.secad.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.nothing.secad.R
import com.nothing.secad.note.database.NoteDatabase
import com.nothing.secad.note.repository.NoteRepository
import com.nothing.secad.note.viewmodel.NoteViewModel
import com.nothing.secad.note.viewmodel.NoteViewModelFactory

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
        noteViewModel= ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }
}