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
        val dataList = dao.getNoteList()

        return dataList.sortedByDescending { it.date }
    }

    override suspend fun insertNote(note: Note): Long? {
        val app = application as WriteAwayApp
        val dao = app.db.noteDao()
        return dao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        val app = application as WriteAwayApp
        val dao = app.db.noteDao()
        dao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        val app = application as WriteAwayApp
        val dao = app.db.noteDao()
        dao.deleteNote(note.id)
    }

}