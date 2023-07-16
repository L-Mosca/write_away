package br.com.writeaway.screen.createnote

import android.animation.ValueAnimator
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.writeaway.R
import br.com.writeaway.base.BaseFragment
import br.com.writeaway.databinding.FragmentCreateNoteBinding
import br.com.writeaway.screen.createnote.adapter.ColorAdapter
import dagger.hilt.android.AndroidEntryPoint
import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.text.Editable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import br.com.writeaway.domain.models.Note
import br.com.writeaway.util.hideKeyboard
import br.com.writeaway.util.showKeyboard

@AndroidEntryPoint
class CreateNoteFragment : BaseFragment<FragmentCreateNoteBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentCreateNoteBinding =
        FragmentCreateNoteBinding::inflate
    override val viewModel: CreateNoteViewModel by viewModels()

    private val adapter: ColorAdapter by lazy { ColorAdapter() }
    private val args: CreateNoteFragmentArgs by navArgs()

    override fun initViews() {
        viewModel.setInitialData(args.note)
        setupAdapter()
        binding.fabSaveNote.setOnClickListener { onFabClicked() }
    }

    override fun initObservers() {
        viewModel.saveError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.default_error_message),
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.updateError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.default_error_message),
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.updateSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.note_update_success),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }

        viewModel.editTextFocus.observe(viewLifecycleOwner) {
            binding.etNoteDescription.requestFocus()
            showKeyboard(binding.etNoteDescription)
        }

        viewModel.initialData.observe(viewLifecycleOwner) { note ->
            setupInitialData(note)
        }

        viewModel.saveSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.note_save_success),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }

        viewModel.invalidDescription.observe(viewLifecycleOwner) {
            showKeyboard(binding.etNoteDescription)
            binding.etNoteDescription.requestFocus()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setupInitialData(note: Note) {
        binding.etNoteDescription.text =
            Editable.Factory.getInstance().newEditable(note.description)
        binding.clCreateNote.setBackgroundColor(note.color)
        adapter.oldColor = note.color
    }

    private fun setupAdapter() {
        binding.rvColors.adapter = adapter
        adapter.submitList(getColorList())
        adapter.oldColor = ContextCompat.getColor(requireContext(), R.color.note_yellow)
        adapter.onColorClicked = { cardColor ->
            val currentColor =
                (binding.clCreateNote.backgroundTintList?.defaultColor ?: adapter.oldColor)

            adapter.oldColor = cardColor.defaultColor
            changeColorAnimate(currentColor, cardColor.defaultColor)
        }
    }

    private fun changeColorAnimate(currentColor: Int, cardColor: Int) {
        val colorAnimator =
            ValueAnimator.ofObject(ArgbEvaluator(), currentColor, cardColor)
        colorAnimator.duration = 500

        colorAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            binding.clCreateNote.setBackgroundColor(animatedValue)
        }
        colorAnimator.start()
    }

    private fun getColorList(): List<Int> {
        val list = mutableListOf<Int>()
        list.apply {
            add(R.color.note_yellow)
            add(R.color.note_cian)
            add(R.color.note_green)
            add(R.color.note_pink)
            add(R.color.note_purple)
            add(R.color.note_red)
        }
        return list
    }

    private fun onFabClicked() {
        val cardColor = if (adapter.oldColor == 0) ContextCompat.getColor(
            requireContext(),
            R.color.note_yellow
        ) else adapter.oldColor
        viewModel.submitNote(
            noteDescription = binding.etNoteDescription.text.toString(),
            noteColor = cardColor,
            noteArgs = args.note
        )
        hideKeyboard()
    }
}