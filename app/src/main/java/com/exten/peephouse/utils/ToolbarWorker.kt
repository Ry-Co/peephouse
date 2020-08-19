package com.exten.peephouse.utils

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.exten.peephouse.R

class ToolbarWorker constructor(private val mActivity: Activity){
    private val toolbarTitleTV = mActivity.findViewById<TextView>(R.id.toolbar_title_tv)
    private val toolbarPostTv = mActivity.findViewById<TextView>(R.id.toolbar_post_tv)
    private val toolbarCancelTv = mActivity.findViewById<TextView>(R.id.toolbar_cancel_tv)
    private val rightImageButton =mActivity.findViewById<ImageView>(R.id.right_button_iv)
    private val leftImageButton =mActivity.findViewById<ImageView>(R.id.left_button_iv)
    private val leftCursorImg = mActivity.findViewById<ImageView>(R.id.left_cursor_iv)
    private val rightCursorImg = mActivity.findViewById<ImageView>(R.id.right_cursor_iv)


    fun switchBox(int:Int){
        when(int){
            0 -> todaysQuestionsToolbar()
            1 -> questionDiscussionToolbar()
            2 -> communityQuestionsToolbar()
            3 -> commentReplyToolbar()
        }
    }

    private fun todaysQuestionsToolbar() {
        toolbarTitleTV.text = "Today's Roster"
        toolbarPostTv.visibility = View.INVISIBLE
        toolbarCancelTv.visibility = View.INVISIBLE
        rightImageButton.visibility = View.VISIBLE
        leftImageButton.visibility = View.VISIBLE
        leftCursorImg.visibility = View.VISIBLE
        rightCursorImg.visibility = View.INVISIBLE
        rightImageButton.setImageResource(R.drawable.ic_baseline_people_24)
        leftImageButton.setImageResource(R.drawable.ic_baseline_gavel_24)
    }

    private fun communityQuestionsToolbar() {
        toolbarTitleTV.text = "Up for vote"
        toolbarPostTv.visibility = View.INVISIBLE
        toolbarCancelTv.visibility = View.INVISIBLE
        rightImageButton.visibility = View.VISIBLE
        leftImageButton.visibility = View.VISIBLE
        leftCursorImg.visibility = View.INVISIBLE
        rightCursorImg.visibility = View.VISIBLE
        rightImageButton.setImageResource(R.drawable.ic_baseline_people_24)
        leftImageButton.setImageResource(R.drawable.ic_baseline_gavel_24)
    }

    private fun questionDiscussionToolbar() {
        toolbarTitleTV.text = "Discuss"
        toolbarPostTv.visibility = View.INVISIBLE
        toolbarCancelTv.visibility = View.INVISIBLE
        rightImageButton.visibility = View.INVISIBLE
        leftImageButton.visibility = View.VISIBLE
        leftCursorImg.visibility = View.INVISIBLE
        rightCursorImg.visibility = View.INVISIBLE
        rightImageButton.setImageResource(R.drawable.ic_baseline_people_24)
        leftImageButton.setImageResource(R.drawable.ic_baseline_arrow_back_24)

    }

    private fun commentReplyToolbar(){
        toolbarTitleTV.text = "Reply"
        toolbarPostTv.visibility = View.VISIBLE
        toolbarCancelTv.visibility = View.INVISIBLE
        rightImageButton.visibility = View.INVISIBLE
        leftImageButton.visibility = View.VISIBLE
        leftCursorImg.visibility = View.VISIBLE
        rightCursorImg.visibility = View.INVISIBLE
        rightImageButton.setImageResource(R.drawable.ic_baseline_people_24)
        leftImageButton.setImageResource(R.drawable.ic_baseline_close_24)
    }



}