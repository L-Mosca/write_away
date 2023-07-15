package br.com.writeaway.screen.home

import androidx.lifecycle.MutableLiveData
import br.com.writeaway.base.BaseViewModel
import br.com.writeaway.domain.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {
    val notes = MutableLiveData<List<Note>>()


    fun fetchNotes() {
        val list = mutableListOf<Note>()
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota",
                date = Calendar.getInstance().timeInMillis
            )
        )
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota 1",
                date = Calendar.getInstance().timeInMillis
            )
        )
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota 2",
                date = Calendar.getInstance().timeInMillis
            )
        )
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota 3",
                date = Calendar.getInstance().timeInMillis
            )
        )
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota 4",
                date = Calendar.getInstance().timeInMillis
            )
        )
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota 5",
                date = Calendar.getInstance().timeInMillis
            )
        )
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota 5",
                date = Calendar.getInstance().timeInMillis
            )
        )
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota 5",
                date = Calendar.getInstance().timeInMillis
            )
        )
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota 5",
                date = Calendar.getInstance().timeInMillis
            )
        )
        list.add(
            Note(
                id = 1,
                description = "Texto de teste de nova nota 5",
                date = Calendar.getInstance().timeInMillis
            )
        )

        notes.postValue(list)
    }
}