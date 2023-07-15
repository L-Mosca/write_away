package br.com.writeaway.screen.home

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import br.com.writeaway.base.BaseFragment
import br.com.writeaway.databinding.FragmentHomeBinding
import br.com.writeaway.screen.home.adapter.NoteAdapter
import br.com.writeaway.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate
    override val viewModel: HomeViewModel by viewModels()

    private val adapter: NoteAdapter by lazy { NoteAdapter() }

    override fun initViews() {
        viewModel.fetchNotes()

        binding.fabAddNote.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment()
            navigate(direction)
        }
    }

    override fun initObservers() {
        viewModel.notes.observe(viewLifecycleOwner) { noteList ->
            binding.rvNotes.adapter = adapter
            adapter.submitList(noteList)
        }
    }
}