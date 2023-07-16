package br.com.writeaway.screen.home

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import br.com.writeaway.base.BaseFragment
import br.com.writeaway.databinding.FragmentHomeBinding
import br.com.writeaway.screen.home.adapter.NoteAdapter
import br.com.writeaway.util.navigate
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate
    override val viewModel: HomeViewModel by viewModels()

    private val adapter: NoteAdapter by lazy { NoteAdapter() }

    override fun initViews() {
        setupAdapter()
        binding.fabAddNote.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment()
            navigate(direction)
        }
    }

    override fun initObservers() {
        viewModel.notes.observe(viewLifecycleOwner) { noteList ->
            adapter.submitList(noteList)
        }
    }

    private fun setupAdapter() {
        adapter.onNoteClicked = { note ->
            val direction = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(
                note = Gson().toJson(note)
            )
            navigate(direction)
        }
        binding.rvNotes.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchNotes()
    }
}