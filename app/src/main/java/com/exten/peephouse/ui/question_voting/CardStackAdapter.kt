package com.exten.peephouse.ui.question_voting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exten.peephouse.R
import com.exten.peephouse.objects.Question

class CardStackAdapter(private var spots: List<Question> = emptyList()) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.question_card, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = spots[position]
        holder.questionText.text = spot.text
    }
    override fun getItemCount(): Int {
        return spots.size
    }

    fun setSpots(spots: List<Question>) {
        this.spots = spots
    }

    fun getSpots(): List<Question> {
        return spots
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var questionText:TextView = view.findViewById(R.id.quesiton_text_view)
    }
}