package br.com.writeaway.domain.repositories

import androidx.lifecycle.LiveData
import br.com.writeaway.domain.models.Note
import kotlinx.coroutines.flow.Flow


interface NoteRepositoryContract {
    suspend fun fetchAllNotes(): List<Note>

    suspend fun insertNote(note: Note): Long?

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun fetchLayoutManager() : Flow<String>

    suspend fun setLayoutManager(text: String)
}