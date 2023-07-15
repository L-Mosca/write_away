package br.com.writeaway.domain.repositories

import br.com.writeaway.domain.models.Note


interface NoteRepositoryContract {
    suspend fun fetchAllNotes(): List<Note>

    suspend fun insertNote(note: Note) : Long?
}