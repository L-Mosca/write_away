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
    val deleteSuccess = MutableLiveData<Note?>()
    val deleteError = MutableLiveData<Unit>()
    val saveError = MutableLiveData<Unit>()
    val saveSuccess = MutableLiveData<Note>()

    fun fetchNotes() {
        defaultLaunch {
            notes.postValue(noteRepository.fetchAllNotes())
        }
    }

    fun saveNote(note: Note) {
        defaultLaunch(exceptionHandler = {
            saveError.postValue(Unit)
        }) {
            val id = noteRepository.insertNote(note)
            if (id != null && id > 0)
                saveSuccess.postValue(note)
            else
                saveError.postValue(Unit)
        }
    }

    fun deleteNote(note: Note?) {
        defaultLaunch(exceptionHandler = {
            deleteError.postValue(Unit)
        }) {
            if (note != null) {
                noteRepository.deleteNote(note)
                deleteSuccess.postValue(note)
            }
        }
    }
}