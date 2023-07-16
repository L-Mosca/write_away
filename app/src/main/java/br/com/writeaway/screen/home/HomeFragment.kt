package br.com.writeaway.screen.home

import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.viewModels
import br.com.writeaway.R
import androidx.core.view.isVisible
import br.com.writeaway.base.BaseFragment
import br.com.writeaway.databinding.FragmentHomeBinding
import br.com.writeaway.screen.home.adapter.NoteAdapter
import br.com.writeaway.util.navigate
import br.com.writeaway.util.setAnimationStatus
import com.google.android.material.snackbar.Snackbar
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
        viewModel.saveSuccess.observe(viewLifecycleOwner) { note ->
            val newList = adapter.currentList.toMutableList()
            newList.add(note)
            adapter.submitList(newList.sortedByDescending { it.date }.toList())
            showEmptyPlaceHolder(newList.isEmpty())
        }

        viewModel.saveError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.default_error_message),
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.notes.observe(viewLifecycleOwner) { noteList ->
            showEmptyPlaceHolder(noteList.isEmpty())
            adapter.submitList(noteList)
        }

        viewModel.deleteSuccess.observe(viewLifecycleOwner) { note ->
            if (note != null) {
                val newList = adapter.currentList.toMutableList()
                newList.removeIf { it.id == note.id }
                adapter.submitList(newList.toList())

                showEmptyPlaceHolder(newList.isEmpty())

                val snackBar = Snackbar.make(
                    requireView(),
                    getString(R.string.delete_note_success),
                    Snackbar.LENGTH_LONG
                )
                snackBar.setAction(getString(R.string.undo)) {
                    viewModel.saveNote(note)
                }
                snackBar.show()
            }
        }

        viewModel.deleteError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.default_error_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupAdapter() {
        adapter.onNoteClicked = { note ->
            val direction = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(
                note = Gson().toJson(note)
            )
            navigate(direction)
        }
        adapter.onDeleteClicked = { note ->
            viewModel.deleteNote(note)
        }

        binding.rvNotes.adapter = adapter
    }

    private fun showEmptyPlaceHolder(isVisible: Boolean) {
        binding.includeEmptyList.clPlaceHolder.isVisible = isVisible
        binding.includeEmptyList.lavEmptyNotes.setAnimationStatus(isVisible)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchNotes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.deleteSuccess.value = null
    }
}