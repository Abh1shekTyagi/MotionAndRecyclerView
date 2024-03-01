package com.example.complexmotion

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class NameAdapter() : RecyclerView.Adapter<NameAdapter.ViewHolder>(),
    ItemTouchHelperAdapter {

    private lateinit var list: MutableList<Name>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    fun updateList(names: List<Name>) {
        list = names.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = list[position]
        holder.textView.text = name.text
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Log.d("Abhishek", "onitemmove")
        val (initialIndex, targetIndex) = if (fromPosition < toPosition) Pair(
            fromPosition,
            toPosition
        ) else Pair(toPosition, fromPosition)
        for (i in initialIndex until targetIndex) {
            Collections.swap(list, i, i + 1)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        Log.d("Abhishek", "on item dismiss")
        list.removeAt(position);
        notifyItemRemoved(position);
    }
}
