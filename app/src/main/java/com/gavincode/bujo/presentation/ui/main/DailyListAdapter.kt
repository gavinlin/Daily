package com.gavincode.bujo.presentation.ui.main

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gavincode.bujo.R
import com.gavincode.bujo.domain.DailyBullet


class DailyListAdapter: RecyclerView.Adapter<DailyListAdapter.DailyListViewHolder>() {

    var list: MutableList<DailyBullet> = mutableListOf()
    var dailyListOnClickListener: DailyListClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder =  DailyListViewHolder(inflater.inflate(R.layout.list_item_daily_bullet, parent, false))
        viewHolder.itemView.setOnClickListener { dailyListOnClickListener?.onClick(list[viewHolder.adapterPosition].id) }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DailyListViewHolder, position: Int) {
        val dailyBullet = list[position]
        holder?.let {
            if (dailyBullet.title.isNotBlank()) it.titleView.text = dailyBullet.title
            if (dailyBullet.content.isNotBlank()) it.contentView.text = dailyBullet.content
        }
    }

    fun updateList(newList: List<DailyBullet>) {
        val diffUtil = DiffUtil.calculateDiff(DiffUtilCallback(list, newList))
        list.clear()
        list.addAll(newList)
        diffUtil.dispatchUpdatesTo(this)
    }

    class DailyListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.list_item_bullet_title)
        val contentView: TextView = view.findViewById(R.id.list_item_bullet_content)
    }

    class DiffUtilCallback(private val oldList: List<DailyBullet>,
                           private val newList: List<DailyBullet>): DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}