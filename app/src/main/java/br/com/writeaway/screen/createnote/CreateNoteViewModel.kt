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
    val initialData = MutableLiveData<Note>()
    val editTextFocus = MutableLiveData<Unit>()
    val updateError = MutableLiveData<Unit>()

    fun submitNote(noteDescription: String, noteColor: Int, noteArgs: String?) {
        if (noteArgs.isNullOrEmpty())
            saveNote(noteDescription, noteColor)
        else
            updateNote(noteArgs, noteDescription, noteColor)
    }

    private fun saveNote(noteDescription: String, color: Int) {
        if (noteDescription.isEmpty()) {
            invalidDescription.postValue(Unit)
            return
        }

        defaultLaunch(exceptionHandler = {
            saveError.postValue(Unit)
        }) {
            val newNote = Note(description = noteDescription, date = Date(), color = color)
            val id = noteRepository.insertNote(newNote)
            if (id != null && id > 0)
                saveSuccess.postValue(Unit)
            else
                saveError.postValue(Unit)
        }
    }

    private fun updateNote(noteArg: String, newDescription: String, newColor: Int) {
        defaultLaunch(exceptionHandler = {
            updateError.postValue(Unit)
        }) {
            val newNote = Gson().fromJson(noteArg, Note::class.java)
            newNote.description = newDescription
            newNote.color = newColor
            newNote.date = Date()
            noteRepository.updateNote(newNote)
            updateSuccess.postValue(Unit)
        }
    }

    fun setInitialData(note: String?) {
        try {
            if (!note.isNullOrEmpty())
                initialData.postValue(Gson().fromJson(note, Note::class.java))
            else
                editTextFocus.postValue(Unit)
        } catch (e: Exception) {
            editTextFocus.postValue(Unit)
        }
    }
}