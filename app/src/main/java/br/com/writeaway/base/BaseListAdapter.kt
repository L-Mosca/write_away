package br.com.writeaway.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<VB : ViewBinding, T>(
    diffUtilCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, ViewHolder>(diffUtilCallback) {

    abstract val bindingInflater: (LayoutInflater, ViewGroup) -> VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = bindingInflater(inflater, parent)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        onBindViewHolder(holder, data, position)
    }

    abstract fun onBindViewHolder(holder: ViewHolder, data: T, position: Int)

}