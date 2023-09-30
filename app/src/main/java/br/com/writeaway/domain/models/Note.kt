package br.com.writeaway.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.writeaway.R
import java.util.Date

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "note_title") var title: String,
    @ColumnInfo(name = "note_description") var description: String,
    @ColumnInfo(name = "note_date") var date: Date = Date(),
    @ColumnInfo(name = "note_modified_date") var modifiedDate: Date = Date(),
    @ColumnInfo(name = "note_color") var color: Int = R.color.note_yellow,
    @ColumnInfo(name = "note_password") var isProtectedNote: Boolean = false
)