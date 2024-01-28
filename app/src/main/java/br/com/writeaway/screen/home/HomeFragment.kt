package br.com.writeaway.screen.home

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import br.com.writeaway.R
import br.com.writeaway.base.BaseFragment
import br.com.writeaway.base.CustomItemAnimator
import br.com.writeaway.databinding.FragmentHomeBinding
import br.com.writeaway.domain.models.Note
import br.com.writeaway.screen.home.adapter.NoteAdapter
import br.com.writeaway.util.AppConstants
import br.com.writeaway.util.TransitionAnimation
import br.com.writeaway.util.getLisViewValue
import br.com.writeaway.util.navigate
import br.com.writeaway.util.orderList
import br.com.writeaway.util.setStatusBarColor
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate
    override val viewModel: HomeViewModel by viewModels()

    private val adapter: NoteAdapter by lazy { NoteAdapter() }
    private var listOrder = AppConstants.ORDER_BY_UPDATE_DATE

    override fun initViews() {
        setupBackPressed()
        setupAdapter()
        setupFab()
        binding.ivSettings.setOnClickListener { goToSettingsScreen() }
        val animation = CustomItemAnimator()
        binding.rvNotes.itemAnimator = animation
    }

    override fun initObservers() {
        viewModel.switchTopRightButton.observe(viewLifecycleOwner) { iconResource ->
            with(binding.ivSettings) {
                setImageResource(iconResource)
                setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            }
        }

        viewModel.setSelectionMode.observe(viewLifecycleOwner) {
            adapter.switchSelectionMode(!adapter.isSelectionMode)
        }

        viewModel.textSize.observe(viewLifecycleOwner) { textSize ->
            adapter.textSize = textSize
        }

        viewModel.orderType.observe(viewLifecycleOwner) { orderType ->
            listOrder = orderType
            viewModel.fetchLayoutManager()
        }

        viewModel.layoutManager.observe(viewLifecycleOwner) { layoutManager ->
            binding.rvNotes.layoutManager = layoutManager.getLisViewValue(requireContext())
            viewModel.fetchNotes()
        }

        viewModel.showBiometricView.observe(viewLifecycleOwner) { note ->
            authenticateWithBiometrics(note)
        }

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
            adapter.submitList(noteList.orderList(listOrder))
        }

        viewModel.deleteSuccess.observe(viewLifecycleOwner) { note ->
            note?.let { data ->
                val newList = adapter.currentList.toMutableList()
                newList.removeIf { it.id == data.id }
                adapter.submitList(newList.toList())

                showEmptyPlaceHolder(newList.isEmpty())

                val snackBar = Snackbar.make(
                    requireView(),
                    getString(R.string.delete_note_success),
                    Snackbar.LENGTH_LONG
                )
                snackBar.setAction(getString(R.string.undo)) {
                    viewModel.saveNote(data)
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

    private fun setupFab() {
        binding.fabAddNote.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        binding.fabAddNote.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment()
            navigate(direction, animation = TransitionAnimation.TRANSLATE_FROM_DOWN_POP)
        }
    }

    private fun setupAdapter() {
        adapter.onLockClicked = { note -> viewModel.blockedNoteClicked(note) }
        adapter.onNoteClicked = { note ->
            val direction = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(
                note = Gson().toJson(note)
            )
            navigate(direction, animation = TransitionAnimation.TRANSLATE_FROM_DOWN_POP)
        }
        adapter.onDeleteClicked = { note -> viewModel.deleteNote(note) }
        adapter.onLongClicked = { _ ->
            viewModel.handleSelectionMode(adapter.isSelectionMode)
            viewModel.setTopRightButton(adapter.isSelectionMode)
        }

        binding.rvNotes.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvNotes.adapter = adapter
    }

    private fun showEmptyPlaceHolder(isVisible: Boolean) {
        binding.stub.isVisible = isVisible
    }

    private fun authenticateWithBiometrics(note: Note) {
        val executor: Executor = ContextCompat.getMainExecutor(requireContext())

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showAuthenticationErrorDialog(note)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val direction = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(
                        note = Gson().toJson(note)
                    )
                    navigate(direction)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showAuthenticationErrorDialog(note)
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_authenticate_title))
            .setDescription(getString(R.string.biometric_authenticate_message))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showAuthenticationErrorDialog(note: Note) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Authentication Error")
        builder.setMessage("Authentication failed. Please try again.")
        builder.setPositiveButton("Try Again") { _, _ ->
            authenticateWithBiometrics(note)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun goToSettingsScreen() {
        val direction = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
        navigate(direction)
    }

    private fun setupBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (adapter.isSelectionMode) adapter.closeSelectionMode()
                else requireActivity().finish()
            }
        }

        callback.isEnabled = true
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.white))
        viewModel.fetchOrderType()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.deleteSuccess.value = null
    }
}