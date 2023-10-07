package br.com.writeaway.screen.createnote

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.text.Editable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.writeaway.R
import br.com.writeaway.base.BaseFragment
import br.com.writeaway.databinding.FragmentCreateNoteBinding
import br.com.writeaway.domain.models.Note
import br.com.writeaway.screen.createnote.adapter.ColorAdapter
import br.com.writeaway.util.getDefaultTextSize
import br.com.writeaway.util.getDetailTextSize
import br.com.writeaway.util.getTitleTextSize
import br.com.writeaway.util.hideKeyboard
import br.com.writeaway.util.setStatusBarColor
import br.com.writeaway.util.showKeyboard
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreateNoteFragment : BaseFragment<FragmentCreateNoteBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentCreateNoteBinding =
        FragmentCreateNoteBinding::inflate
    override val viewModel: CreateNoteViewModel by viewModels()

    private val adapter: ColorAdapter by lazy { ColorAdapter() }
    private val args: CreateNoteFragmentArgs by navArgs()
    private var noteData: Note? = null

    override fun initViews() {
        noteData = Gson().fromJson(args.note, Note::class.java)
        viewModel.setInitialData(noteData)
        viewModel.fetchTextSize()
        binding.fabSaveNote.setOnClickListener { onFabClicked() }
        setupAdapter()
        //binding.swProtectNote.isVisible = hasDevicePassword()
    }

    override fun initObservers() {
        viewModel.textSize.observe(viewLifecycleOwner) { textSize ->
            setupTextSize(textSize)
        }

        viewModel.characterQuantity.observe(viewLifecycleOwner) { textLength ->
            binding.tvCharacters.text = getString(R.string.character, textLength)
        }

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
            binding.etNoteTitle.requestFocus()
            showKeyboard(binding.etNoteDescription)
            setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.note_yellow))
            setupDescription(0)
            setupDate(Date())
        }

        viewModel.initialData.observe(viewLifecycleOwner) { note ->
            note?.let { setupInitialData(it) }
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
        binding.etNoteTitle.text = Editable.Factory.getInstance().newEditable(note.title)
        binding.clCreateNote.setBackgroundColor(note.color)
        adapter.oldColor = note.color
        setStatusBarColor(note.color)
        binding.swProtectNote.isChecked = note.isProtectedNote
        setupDescription(note.description.length)
        setupDate(note.date)
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
            setStatusBarColor(cardColor)
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
            noteTitle = binding.etNoteTitle.text.toString(),
            noteDescription = binding.etNoteDescription.text.toString(),
            noteColor = cardColor,
            noteArgs = args.note,
            noteHasPassword = binding.swProtectNote.isChecked
        )
        hideKeyboard()
    }

    private fun setupDescription(textLength: Int?) {
        binding.tvCharacters.text = getString(R.string.character, textLength ?: 0)
        binding.etNoteDescription.addTextChangedListener {
            viewModel.setCharacterQuantity(it?.length ?: 0)
        }
    }

    private fun setupDate(date: Date?) {
        binding.tvDate.text =
            SimpleDateFormat(
                "d '${getString(R.string.of)}' MMMM HH:mm",
                Locale.getDefault()
            ).format(date ?: Date())
    }

    private fun setupTextSize(textSize: Float) {
        with(binding) {
            tvDate.textSize = textSize.getDetailTextSize()
            tvCharacters.textSize = textSize.getDetailTextSize()
            etNoteTitle.textSize = textSize.getTitleTextSize()
            etNoteDescription.textSize = textSize.getDefaultTextSize()
        }
    }
}