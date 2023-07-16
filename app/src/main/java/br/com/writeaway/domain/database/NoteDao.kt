package br.com.writeaway.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.writeaway.domain.models.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Note) : Long?

    @Query("SELECT * FROM Note")
    suspend fun getNoteList(): List<Note>

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM Note WHERE id = :noteId")
    suspend fun deleteNote(noteId: Long)
}