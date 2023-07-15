package br.com.writeaway.screen.createnote

import androidx.lifecycle.MutableLiveData
import br.com.writeaway.base.BaseViewModel
import br.com.writeaway.domain.models.Note
import br.com.writeaway.domain.repositories.NoteRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(private val noteRepository: NoteRepositoryContract) :
    BaseViewModel() {

    val saveSuccess = MutableLiveData<Unit>()
    val saveError = MutableLiveData<Unit>()
    val invalidDescription = MutableLiveData<Unit>()

    fun saveNote(noteDescription: String, color: Int) {
        if (noteDescription.isEmpty()) {
            invalidDescription.postValue(Unit)
            return
        }

        val newNote = Note(description = noteDescription, date = Date(), color = color)
        defaultLaunch {
            val id = noteRepository.insertNote(newNote)
            if (id != null && id > 0)
                saveSuccess.postValue(Unit)
            else
                saveError.postValue(Unit)
        }
    }
}