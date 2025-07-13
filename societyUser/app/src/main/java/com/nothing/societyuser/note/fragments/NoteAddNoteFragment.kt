package com.nothing.societyuser.note.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.nothing.societyuser.R
import com.nothing.societyuser.databinding.FragmentNoteAddNoteBinding
import com.nothing.societyuser.note.NotesMainActivity
import com.nothing.societyuser.note.model.Note
import com.nothing.societyuser.note.viewmodel.NoteViewModel


class NoteAddNoteFragment : Fragment(R.layout.fragment_note_add_note), MenuProvider {

    private var addNoteBinding : FragmentNoteAddNoteBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNoteBinding = FragmentNoteAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)
        notesViewModel = (activity as NotesMainActivity).noteViewModel
        addNoteView = view
    }

    private fun saveNote(view: View) {
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()

        if(noteTitle.isNotEmpty()){
            val note = Note(0,noteTitle,noteDesc)
            notesViewModel.addNote(note)
            Toast.makeText(addNoteView.context,"Note Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.noteHomeFragment, false)
        }else{
            Toast.makeText(addNoteView.context, "please enter note Title ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveNote(addNoteView)
                true
            }
            else -> false
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        addNoteBinding = null
    }


}