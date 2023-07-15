package br.com.writeaway.screen.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import br.com.writeaway.base.BaseListAdapter
import br.com.writeaway.base.ViewHolder
import br.com.writeaway.databinding.AdapterNoteBinding
import br.com.writeaway.domain.models.Note
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

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterNoteBinding>,
        data: Note,
        position: Int
    ) {
        holder.binding.apply {
            cvNote.setCardBackgroundColor(ContextCompat.getColor(root.context, data.color))
            tvNoteDescription.text = data.description
            //tvNoteDate.text = formatDate(data.date)
        }
    }

    private fun formatDate(timeMillis: Long): String {
        val format = "dd/MM/yyyy - HH:mm"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(timeMillis)
        return simpleDateFormat.format(date)
    }
}