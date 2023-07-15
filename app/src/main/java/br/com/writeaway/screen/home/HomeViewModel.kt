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

    fun fetchNotes() {
        defaultLaunch {
            notes.postValue(noteRepository.fetchAllNotes())
        }
    }
}