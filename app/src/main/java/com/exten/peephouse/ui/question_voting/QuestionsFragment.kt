package com.exten.peephouse.ui.question_voting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.exten.peephouse.utils.DataRepository
import com.exten.peephouse.MainActivityViewModel
import com.exten.peephouse.R
import com.exten.peephouse.objects.Question
import com.exten.peephouse.objects.Vote
import com.yuyakaido.android.cardstackview.*


class QuestionsFragment : Fragment(), CardStackListener {
    private val viewModel: MainActivityViewModel by activityViewModels()
    private val dataRepo = DataRepository()
    private val manager by lazy { CardStackLayoutManager(requireContext(), this) }
    private var answeredQuestions = ArrayList<Question>()
    private var vote:Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.todaysQuestions.observe(viewLifecycleOwner, Observer{ todayQ ->
            viewModel.usersVotedQuestions.observe(viewLifecycleOwner, Observer{usersVotes ->
                answeredQuestions = parseForUnansweredQuestionsToday(todayQ,usersVotes)
                if(answeredQuestions.size == 0){
                    Log.e("TAG", "GO TO NEXT WINDOW")
                    findNavController().navigate(R.id.action_questionsFragment_to_discussionFragment)
                }
                val adapter = CardStackAdapter(answeredQuestions)
                val cardView = view.findViewById<CardStackView>(R.id.card_stack_view)
                setUpCardStackView(adapter, cardView)
            })
        })
        setUpNavigation()
        setUpButtons()
    }

    private fun parseForUnansweredQuestionsToday(todayQ: List<Question>, usersVotes:List<Vote>): ArrayList<Question> {
        val ansQ = ArrayList<Question>()
        for(question in todayQ){
            for(vote in usersVotes){
                if(vote.question_id == question.id){
                    //user has answered todays question
                    ansQ.add(question)
                }
            }
        }
        val finalQList = ArrayList<Question>()
        for(q in todayQ){
            if(ansQ.contains(q)){

            }else{
                finalQList.add(q)
            }
        }
        return finalQList
    }

    private fun setUpNavigation(){
        //hide toolbar
    }

    private fun setUpCardStackView(adapter: CardStackAdapter, cardStackView: CardStackView){
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.FREEDOM)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun setUpButtons(){

    }

    private fun createSpots(): List<Question>{
        val spots = ArrayList<Question>()
        spots.add(
            Question(
                id = "ASDFASDFQWERQWE",
                text = "This is a sample question?",
                yay_votes = 0,
                nay_votes = 0
            )
        )
        spots.add(
            Question(
                id = "ASDFASDFQWERQWE",
                text = "This is a sample question1?",
                yay_votes = 0,
                nay_votes = 0
            )
        )
        spots.add(
            Question(
                id = "ASDFASDFQWERQWE",
                text = "This is a sample question2?",
                yay_votes = 0,
                nay_votes = 0
            )
        )
        return spots
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.quesiton_text_view)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
        val question = answeredQuestions[position]
        val uid = viewModel.currentUser!!.uid

        when(vote){
            true -> dataRepo.yayVote(questionID = question.id, voterID = uid)
            false -> dataRepo.nayVote(questionID = question.id, voterID = uid)
            null -> Log.wtf("QFrag", "vote is null and being passed?")
        }

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction!!.name}, r = $ratio")
        when (direction.toString()) {
            "Top" -> {
                Log.e("TAG", "Upvote")
                vote = true
            }
            "Right" -> {
                Log.e("TAG", "Upvote")
                vote = true
            }
            "Bottom" -> {
                Log.e("TAG", "Downvote")
                vote = false
            }
            else -> {
                Log.e("TAG", "Downvote")
                vote = false
            }
        }
    }

    override fun onCardSwiped(direction: Direction?) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if(manager.topPosition == answeredQuestions.size){
            Log.e("TAG", "GO TO NEXT WINDOW")
            findNavController().navigate(R.id.action_questionsFragment_to_discussionFragment)
        }
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        val textView = requireView().findViewById<TextView>(R.id.quesiton_text_view)
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }
}