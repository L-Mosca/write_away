package br.com.writeaway.domain.repositories

import android.app.Application
import br.com.writeaway.WriteAwayApp
import br.com.writeaway.domain.models.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(private val application: Application) :
    NoteRepositoryContract {

    override suspend fun fetchAllNotes(): List<Note> {
        val app = application as WriteAwayApp
        val dao = app.db.noteDao()
        return dao.getNoteList()
    }

    override suspend fun insertNote(note: Note) : Long? {
        val app = application as WriteAwayApp
        val dao = app.db.noteDao()
        return dao.insertNote(note)
    }

}