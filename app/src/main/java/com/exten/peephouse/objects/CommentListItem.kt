package com.exten.peephouse.objects

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavController
import com.exten.peephouse.R
import com.exten.peephouse.ui.QuestionDiscussionDirections
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.comment_item.*
import kotlinx.android.synthetic.main.comment_item.view.*


open class CommentListItem constructor(val navController: NavController, val comment: Comment, private val depth: Int) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //todo bind values

        viewHolder.comment_text_textview.text = comment.text

        if (comment.voteOnPost) {
            viewHolder.yay_or_nay_image_view.setImageResource(R.drawable.yay_vote_circle)
        } else {
            viewHolder.yay_or_nay_image_view.setImageResource(R.drawable.nay_vote_circle)
        }
        viewHolder.commenter_id_textview.text =comment.commenterID

        depthSeparatorHandling(viewHolder)

        viewHolder.itemView.apply {
            setOnLongClickListener {
                println("exp group togg")
                expandableGroup.onToggleExpanded()
                true
            }
        }

        viewHolder.reply_tv.setOnClickListener {
            val action =
                QuestionDiscussionDirections.actionQuestionDiscussionToCommentFragment(comment)
            navController.navigate(action)
        }

    }

    override fun getLayout() = R.layout.comment_item

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

    private fun depthSeparatorHandling(viewHolder: GroupieViewHolder) {
        viewHolder.itemView.seperator_container.removeAllViews()
        viewHolder.itemView.seperator_container.visibility =
            if (depth > 0) {
                View.VISIBLE
            } else {
//                val v : View = LayoutInflater.from(viewHolder.itemView.context).inflate(R.layout.layout_seperator_view_horz, viewHolder.itemView.vertical_seperator, false)
//                println("Horz::"+comment.text)
//                v.setBackgroundColor(comment.color)
//                viewHolder.itemView.vertical_seperator.addView(v)
                View.VISIBLE
            }
        var prevColor = 0
        for (i in 0..depth) {
            val v: View = LayoutInflater.from(viewHolder.itemView.context).inflate(
                R.layout.layout_seperator_view,
                viewHolder.itemView.seperator_container,
                false
            )
            if (prevColor == 0) {
                v.setBackgroundColor(comment.color)
                prevColor = comment.color
            } else {
                v.setBackgroundColor(darkenColor(prevColor))
                prevColor = darkenColor(prevColor)

            }


            viewHolder.itemView.seperator_container.addView(v)
        }
        viewHolder.itemView.comment_layout.requestLayout()
    }

    private fun darkenColor(parentColor: Int): Int {
        return Color.HSVToColor(FloatArray(3).apply {
            Color.colorToHSV(parentColor, this)
            this[2] *= 0.8f
        })
    }


}