package br.com.writeaway.screen.createnote.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import br.com.writeaway.base.BaseListAdapter
import br.com.writeaway.base.ViewHolder
import br.com.writeaway.databinding.AdapterColorBinding

class ColorAdapter :
    BaseListAdapter<AdapterColorBinding, Int>(object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }) {
    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterColorBinding
        get() = { layoutInflater, viewGroup ->
            AdapterColorBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    var onColorClicked: ((ColorStateList) -> Unit)? = null
    var oldColor: Int = 0

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterColorBinding>,
        data: Int,
        position: Int
    ) {
        holder.binding.apply {
            cvColor.setCardBackgroundColor(ContextCompat.getColor(root.context, data))
            cvColor.setOnClickListener {
                onColorClicked?.invoke(cvColor.cardBackgroundColor)
            }
        }
    }
}