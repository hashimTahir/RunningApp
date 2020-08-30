/*
 * Copyright (c) 2020/  8/ 30.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments.run

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hashim.runningapp.db.Run

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    val hDiffCallBack = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hId == newItem.hId
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    val hListDiffer = AsyncListDiffer(this, hDiffCallBack)

    fun hSubmitList(list: List<Run>) {
        hListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}