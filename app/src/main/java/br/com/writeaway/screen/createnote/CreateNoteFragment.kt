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
import android.widget.Toast
import androidx.core.content.ContextCompat

@AndroidEntryPoint
class CreateNoteFragment : BaseFragment<FragmentCreateNoteBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentCreateNoteBinding =
        FragmentCreateNoteBinding::inflate
    override val viewModel: CreateNoteViewModel by viewModels()

    private val adapter: ColorAdapter by lazy { ColorAdapter() }

    override fun initViews() {
        setupAdapter()

        binding.fabSaveNote.setOnClickListener {
            val cardColor = if (adapter.oldColor == 0) ContextCompat.getColor(
                requireContext(),
                R.color.note_yellow
            ) else adapter.oldColor
            viewModel.saveNote(
                binding.etNoteDescription.text.toString(),
                cardColor
            )
        }
    }

    override fun initObservers() {
        viewModel.saveSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Salvo com sucesso", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        viewModel.saveError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Erro", Toast.LENGTH_SHORT).show()
        }

        viewModel.invalidDescription.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Ta uma merda", Toast.LENGTH_SHORT).show()
        }
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
}