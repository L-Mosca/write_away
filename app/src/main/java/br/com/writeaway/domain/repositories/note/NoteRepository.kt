package br.com.writeaway.domain.repositories.note

import android.app.Application
import br.com.writeaway.WriteAwayApp
import br.com.writeaway.domain.local.PreferencesContract
import br.com.writeaway.domain.models.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val application: Application,
    private val dataStore: PreferencesContract
) : NoteRepositoryContract {

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

    override suspend fun fetchLayoutManager(): Flow<String> =
        dataStore.getLayoutManagerMode()

    override suspend fun setLayoutManager(text: String) =
        dataStore.setLayoutManagerMode(text = text)
}