package br.com.writeaway.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.writeaway.R
import java.util.Date

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "note_description") val description: String,
    @ColumnInfo(name = "note_date") val date: Date = Date(),
    @ColumnInfo(name = "note_color") val color: Int = R.color.note_yellow,
)