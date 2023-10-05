package br.com.writeaway.screen.home

import androidx.lifecycle.MutableLiveData
import br.com.writeaway.base.BaseViewModel
import br.com.writeaway.domain.models.Note
import br.com.writeaway.domain.repositories.note.NoteRepositoryContract
import br.com.writeaway.domain.repositories.settings.SettingsRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepositoryContract,
    private val settingsRepository: SettingsRepositoryContract
) :
    BaseViewModel() {
    val notes = MutableLiveData<List<Note>>()
    val deleteSuccess = MutableLiveData<Note?>()
    val deleteError = MutableLiveData<Unit>()
    val saveError = MutableLiveData<Unit>()
    val saveSuccess = MutableLiveData<Note>()
    val showBiometricView = MutableLiveData<Note>()

    val textSize = MutableLiveData<Float>()
    val layoutManager = MutableLiveData<String>()
    val orderType = MutableLiveData<Int>()

    fun fetchNotes() {
        defaultLaunch {
            val noteList = noteRepository.fetchAllNotes()
            notes.postValue(noteList)
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

    fun blockedNoteClicked(note: Note) {
        if (note.isProtectedNote)
            showBiometricView.postValue(note)
    }

    fun fetchTextSize() {
        defaultLaunch {
            settingsRepository.fetchTextSize().collect { textSize ->
                this.textSize.postValue(textSize)
            }
        }
    }

    fun fetchOrderType() {
        defaultLaunch {
            settingsRepository.fetchOrder().collect { orderType ->
                this.orderType.postValue(orderType)
            }
        }
    }

    fun fetchLayoutManager() {
        defaultLaunch {
            settingsRepository.fetchLayoutManager().collect { layoutManager ->
                this.layoutManager.postValue(layoutManager)
            }
        }
    }
}