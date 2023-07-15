package br.com.writeaway.domain.models

import android.os.Parcelable
import br.com.writeaway.R
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Note(
    val id: Int,
    val description: String,
    val date: Long = Calendar.getInstance().timeInMillis,
    val color: Int = R.color.note_yellow,
) : Parcelable