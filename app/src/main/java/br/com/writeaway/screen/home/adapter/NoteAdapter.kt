package br.com.writeaway.screen.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import br.com.writeaway.base.BaseListAdapter
import br.com.writeaway.base.ViewHolder
import br.com.writeaway.databinding.AdapterNoteBinding
import br.com.writeaway.domain.models.Note
import br.com.writeaway.util.AppConstants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter :
    BaseListAdapter<AdapterNoteBinding, Note>(object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }) {
    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterNoteBinding
        get() = { layoutInflater, viewGroup ->
            AdapterNoteBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    var onNoteClicked: ((Note) -> Unit)? = null
    var onDeleteClicked: ((Note) -> Unit)? = null
    var onLockClicked: ((Note) -> Unit)? = null

    var textSize = 14f

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: ViewHolder<AdapterNoteBinding>,
        data: Note,
        position: Int
    ) {
        holder.binding.apply {
            cvNote.setOnClickListener { onNoteClicked?.invoke(data) }
            ivDeleteItem.setOnClickListener { onDeleteClicked?.invoke(data) }

            with(data) {
                cvNote.setCardBackgroundColor(color)
                tvNoteTitle.text = title
                tvNoteDescription.text = description
                tvNoteDate.text = formatDate(date)
            }
        }
    }

    private fun formatDate(date: Date): String {
        val format = "dd/MM/yyyy - HH:mm"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}