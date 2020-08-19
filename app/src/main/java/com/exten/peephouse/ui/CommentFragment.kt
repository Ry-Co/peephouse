package com.exten.peephouse.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.exten.peephouse.MainActivityViewModel
import com.exten.peephouse.R
import com.exten.peephouse.objects.Comment
import com.exten.peephouse.objects.Vote
import com.exten.peephouse.utils.DataRepository

class CommentFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()
    private val dataRepo = DataRepository()
    private val args: CommentFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        viewModel.toolbarMode.value = 3
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //todo add commenter id to comment
        //todo add @s
        super.onViewCreated(view, savedInstanceState)
        val commentText = view.findViewById<TextView>(R.id.comment_text)
        val commenterID = view.findViewById<TextView>(R.id.commenter_id_tv)
        val subCommentEditText = view.findViewById<EditText>(R.id.reply_ET)
        val cancelButton = requireActivity().findViewById<ImageView>(R.id.left_button_iv)
        val postButton = requireActivity().findViewById<TextView>(R.id.toolbar_post_tv)
        val parentComment = args.comment
        commentText.text = parentComment.text
        commenterID.text = parentComment.commenterID
        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
        postButton.setOnClickListener{
            val text = subCommentEditText.text.toString()
            var vote: Vote? = null
            for(votee in viewModel.usersVotedQuestions.value!!){
                if(votee.question_id == parentComment.postID){
                    vote = votee
                }
            }
            if(text == ""){
                findNavController().popBackStack()
            }else{
                val randCommentID = dataRepo.todayRef.document().id
                val comment = Comment(color = parentComment.color, commentID = randCommentID, commenterID = vote!!.vote_id, text = text, voteOnPost = vote!!.vote, subComments = arrayListOf<Comment>(), parentCommentID = parentComment.commentID, postID = parentComment.postID)
                dataRepo.commentTodaysQuestions(comment){
                    hideKeyboard()
                    findNavController().popBackStack()
                }
            }
        }
    }


    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}