package com.exten.peephouse.ui.question_voting

import androidx.recyclerview.widget.DiffUtil
import com.exten.peephouse.objects.Question

class SpotDiffCallback(
    private val old: List<Question>,
    private val new: List<Question>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition].id == new[newPosition].id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition] == new[newPosition]
    }

}