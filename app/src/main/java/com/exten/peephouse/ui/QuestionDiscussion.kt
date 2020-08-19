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
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.exten.peephouse.MainActivityViewModel
import com.exten.peephouse.R
import com.exten.peephouse.objects.Comment
import com.exten.peephouse.objects.CommentListGroup
import com.exten.peephouse.objects.Question
import com.exten.peephouse.objects.Vote
import com.exten.peephouse.utils.DataRepository
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_question_discussion.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class QuestionDiscussion() : Fragment() {
    private val args: QuestionDiscussionArgs by navArgs()
    private val dataRepo = DataRepository()
    private val viewModel: MainActivityViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_discussion, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.toolbarMode.value = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = requireActivity().findViewById<ImageView>(R.id.left_button_iv)
        val commentEditText = view.findViewById<EditText>(R.id.current_comment_et)
        val commentButton = view.findViewById<LinearLayout>(R.id.comment_confirm_iv)

        val currentQ = args.currentQuestion
        initQuestionView(view, currentQ)


        commentButton.setOnClickListener {
            //send comment
            sendComment(commentEditText.text.toString(), view)
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        swipeRefreshComments.setOnRefreshListener {
            retrieveComments(){
                swipeRefreshComments.isRefreshing = false
            }
        }

        retrieveComments(){
            setUpListViews(parseComments(it))
        }
    }

    private fun initQuestionView(view:View, currentQ:Question){
        val questionText = view.findViewById<TextView>(R.id.question_text_view_disc)
        val yayVotesText = view.findViewById<TextView>(R.id.yay_percent_disc)
        val nayVotesText = view.findViewById<TextView>(R.id.nay_percent_disc)
        val yayfloat = currentQ.yay_votes.toFloat()
        val nayfloat = currentQ.nay_votes.toFloat()
        val tot = yayfloat+nayfloat
        val yayp = ((yayfloat / tot) * 100).roundToInt()
        val nayp = ((nayfloat / tot) * 100).roundToInt()
        questionText.text = currentQ.text
        yayVotesText.text = "$yayp%"
        nayVotesText.text = "$nayp%"
    }

    private fun retrieveComments(callback: (ArrayList<Comment>) -> Unit){
        dataRepo.getCommentsFromPost(args.currentQuestion.id, false){rawCommentList ->
            callback.invoke(rawCommentList)
        }

    }

    private fun parseComments(rawCommentList:ArrayList<Comment>):ArrayList<Comment>{
        val commentList = ArrayList<Comment>()
        for(comment in rawCommentList){
            if(comment.parentCommentID == ""){
                //its a top level comment
                commentList.add(comment)
            }else{
                //its a child
                val parentIdxNewList = commentList.indexOfFirst { it.commentID == comment.parentCommentID }
                if(parentIdxNewList > -1){
                    //its parent is in the new list
                    commentList[parentIdxNewList].subComments.add(comment)
                }else{
                    //it's parent has yet to be added
                    val parentIdx = rawCommentList.indexOfFirst {it.commentID == comment.parentCommentID}
                    rawCommentList[parentIdx].subComments.add(comment)
                }

            }
        }
        return commentList
    }

    private fun setUpListViews(commentList:ArrayList<Comment>){
        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        comments_recycler_view.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupAdapter
            setHasFixedSize(true)
        }
        for(comment in commentList){
            groupAdapter.add(CommentListGroup(findNavController(),comment))
        }
    }

    private fun sendComment(commentText:String, view:View){
        //todo add user commenter id
        val commentTextView = view.findViewById<EditText>(R.id.current_comment_et)
        val col = getRandomColor()
        if(commentText.contains("@")){

        }
        var vote: Vote? = null
        var parent:String = ""
        for(votee in viewModel.usersVotedQuestions.value!!){
            if(votee.question_id == args.currentQuestion.id){
                vote = votee
            }
        }
        if(commentText.contains("@")){
            parent = "@parent"
        }
        val randCommentID = dataRepo.todayRef.document().id
        val comment = Comment(commentID= randCommentID, commenterID = vote!!.vote_id, postID = args.currentQuestion.id, parentCommentID = parent, text = commentText, voteOnPost = vote.vote, color = col, subComments = arrayListOf<Comment>())
        dataRepo.commentTodaysQuestions(comment){
            commentTextView.setText("")
            hideKeyboard()
            retrieveComments(){setUpListViews(parseComments(it))}
        }
    }

    private fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256))
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