package br.com.writeaway.screen.createnote

import androidx.lifecycle.MutableLiveData
import br.com.writeaway.base.BaseViewModel
import br.com.writeaway.domain.models.Note
import br.com.writeaway.domain.repositories.NoteRepositoryContract
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(private val noteRepository: NoteRepositoryContract) :
    BaseViewModel() {

    val saveSuccess = MutableLiveData<Unit>()
    val saveError = MutableLiveData<Unit>()
    val updateSuccess = MutableLiveData<Unit>()
    val invalidDescription = MutableLiveData<Unit>()
    val initialData = MutableLiveData<Note?>()
    val editTextFocus = MutableLiveData<Unit>()
    val updateError = MutableLiveData<Unit>()

    fun submitNote(
        noteTitle: String,
        noteDescription: String,
        noteColor: Int,
        noteArgs: String?,
        noteHasPassword: Boolean
    ) {
        if (noteArgs.isNullOrEmpty())
            saveNote(noteTitle, noteDescription, noteColor, noteHasPassword)
        else
            updateNote(noteArgs, noteDescription, noteColor, noteHasPassword)
    }

    private fun saveNote(
        noteTitle: String,
        noteDescription: String,
        color: Int,
        noteHasPassword: Boolean
    ) {
        if (noteDescription.isEmpty()) {
            invalidDescription.postValue(Unit)
            return
        }

        defaultLaunch(exceptionHandler = {
            saveError.postValue(Unit)
        }) {
            val newNote = Note(
                title = noteTitle,
                description = noteDescription,
                date = Date(),
                modifiedDate = Date(),
                color = color,
                isProtectedNote = noteHasPassword
            )
            val id = noteRepository.insertNote(newNote)
            if (id != null && id > 0)
                saveSuccess.postValue(Unit)
            else
                saveError.postValue(Unit)
        }
    }

    private fun updateNote(
        noteArg: String,
        newDescription: String,
        newColor: Int,
        noteHasPassword: Boolean
    ) {
        defaultLaunch(exceptionHandler = { updateError.postValue(Unit) }) {
            val newNote = Gson().fromJson(noteArg, Note::class.java)
            newNote.apply {
                description = newDescription
                color = newColor
                modifiedDate = Date()
                isProtectedNote = noteHasPassword
            }
            noteRepository.updateNote(newNote)
            updateSuccess.postValue(Unit)
        }
    }

    fun setInitialData(note: Note?) {
        if (note != null) {
            initialData.postValue(note)
        } else {
            editTextFocus.postValue(Unit)
        }
    }
}