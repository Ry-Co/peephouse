package com.exten.peephouse.ui.todays_questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.exten.peephouse.MainActivityViewModel
import com.exten.peephouse.R
import com.exten.peephouse.objects.QuestionListItem
import com.exten.peephouse.objects.Question
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_discussion.*

class TodayDiscussionsFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()


    override fun onResume() {
        super.onResume()
        viewModel.toolbarMode.value = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.show()
        return inflater.inflate(R.layout.fragment_discussion, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val communityButton = requireActivity().findViewById<ImageView>(R.id.right_button_iv)
        val discussionButton = requireActivity().findViewById<ImageView>(R.id.left_button_iv)
        communityButton.setOnClickListener {
            findNavController().navigate(R.id.action_discussionFragment_to_communityFragment)
        }
        discussionButton.setOnClickListener {  }
        val questionListItems = generateListItems(viewModel.todaysQuestions.value!!)
        val mAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(questionListItems)
        }
        recycler_view.apply {
            //addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@TodayDiscussionsFragment.context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }

    private fun generateListItems(qList:List<Question>):MutableList<QuestionListItem>{
        val returnList:MutableList<QuestionListItem> = arrayListOf()
        for(q in qList){
            val qItem = QuestionListItem(findNavController(),q.id, q.text,q.yay_votes,q.nay_votes)
            returnList.add(qItem)
        }
        return returnList
    }
}