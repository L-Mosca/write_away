package br.com.writeaway.screen.home.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.writeaway.databinding.AdapterNoteBinding
import br.com.writeaway.domain.models.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteViewHolder>(
    object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
    }
) {

    var onNoteClicked: ((Note) -> Unit)? = null
    var onLongClicked: ((Note) -> Unit)? = null
    var onDeleteClicked: ((Note) -> Unit)? = null
    var onLockClicked: ((Note) -> Unit)? = null

    var textSize = 14f
    var isSelectionMode = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = AdapterNoteBinding.inflate(inflater, parent, false)
        return NoteViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val data = getItem(position)
        onBind(holder, data, position)
    }

    @SuppressLint("ResourceAsColor")
    private fun onBind(holder: NoteViewHolder, data: Note, position: Int) {
        holder.binding.apply {
            with(data) {
                cvNote.setCardBackgroundColor(color)
                tvNoteTitle.text = title
                tvNoteDescription.text = description
                tvNoteDate.text = formatDate(date)
                ivDeleteItem.isVisible = isSelectionMode
                vCover.isVisible = isSelectionMode && ivDeleteItem.isChecked
            }
        }
    }

    inner class NoteViewHolder(val binding: AdapterNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                cvNote.setOnClickListener {
                    if (isSelectionMode) {
                        ivDeleteItem.isChecked = !ivDeleteItem.isChecked
                    } else {
                        getItem(adapterPosition)?.let { note ->
                            onNoteClicked?.invoke(note)
                        }
                    }
                }
                cvNote.setOnLongClickListener {
                    getItem(adapterPosition)?.let { note ->
                        onLongClicked?.invoke(note)
                    }
                    false
                }

                ivDeleteItem.setOnCheckedChangeListener { _, isChecked ->
                    vCover.isVisible = isSelectionMode && isChecked
                }
            }
        }
    }

    private fun formatDate(date: Date): String {
        val format = "dd/MM/yyyy - HH:mm"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun switchSelectionMode(enableSelectionMode: Boolean) {
        Log.e("teste", "teste do valor = $isSelectionMode")
        isSelectionMode = enableSelectionMode
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun closeSelectionMode() {
        isSelectionMode = false
        notifyDataSetChanged()
    }
}

