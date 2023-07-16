package br.com.writeaway.screen.home

import androidx.lifecycle.MutableLiveData
import br.com.writeaway.base.BaseViewModel
import br.com.writeaway.domain.models.Note
import br.com.writeaway.domain.repositories.NoteRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val noteRepository: NoteRepositoryContract) :
    BaseViewModel() {
    val notes = MutableLiveData<List<Note>>()
    val deleteSuccess = MutableLiveData<Note>()
    val deleteError = MutableLiveData<Unit>()

    fun fetchNotes() {
        defaultLaunch {
            notes.postValue(noteRepository.fetchAllNotes())
        }
    }

    fun deleteNote(note: Note) {
        defaultLaunch(exceptionHandler = {
            deleteError.postValue(Unit)
        }) {
            noteRepository.deleteNote(note)
            deleteSuccess.postValue(note)
        }
    }
}