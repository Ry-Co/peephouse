package com.exten.peephouse.objects

import android.util.Log
import androidx.navigation.NavController
import com.exten.peephouse.R
import com.exten.peephouse.ui.todays_questions.TodayDiscussionsFragmentDirections
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.question_item.*
import kotlin.math.roundToInt

class QuestionListItem(val navController: NavController, val id:String, val text:String, val yayVotes:Int, val nayVotes:Int):Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val yayfloat = yayVotes.toFloat()
        val nayfloat = nayVotes.toFloat()
        val tot = yayfloat+nayfloat
        val yayp = ((yayfloat / tot) * 100).roundToInt()
        val nayp = ((nayfloat / tot) * 100).roundToInt()


        viewHolder.question_textview.text = text
        viewHolder.yay_percent.text = "$yayp%"
        viewHolder.nay_percent.text = "$nayp%"

        viewHolder.question_item_cardview.setOnClickListener {
            Log.e("tag:: ", text)
            val question = Question(id,text,yayVotes,nayVotes)
            val action = TodayDiscussionsFragmentDirections.actionDiscussionFragmentToQuestionDiscussion(question)
            navController.navigate(action)
        }

    }

    override fun getLayout() = R.layout.question_item

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        //this is for grid instead of list
        return super.getSpanSize(spanCount, position)
    }

}