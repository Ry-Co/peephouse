package com.exten.peephouse.utils

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.exten.peephouse.MainActivityViewModel
import com.exten.peephouse.objects.Comment
import com.exten.peephouse.objects.Question
import com.exten.peephouse.objects.User
import com.exten.peephouse.objects.Vote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DataRepository {
    private val TAG: String? = DataRepository::class.simpleName
    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val todayRef= db.collection("today_questions")
    var currentAuthUser = MutableLiveData<FirebaseUser>(mAuth.currentUser)
    var currentUserObj = MutableLiveData<User>()
    var currentUserQuestionList = MutableLiveData<Question>()
    var todayQuestionList = MutableLiveData<Question>()
    var voteNumber = 0


    fun yayVote(questionID:String, voterID:String){
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val voteMap = hashMapOf(
            "vote_id" to System.nanoTime().toString(),
            "question_id" to questionID,
            "timestamp" to currentDate,
            "vote" to true,
            "voter_id" to voterID
        )
        todayRef.document(questionID).collection("votes").document(voterID).set(voteMap, SetOptions.merge())
        todayRef.document(questionID).update("yay_votes", FieldValue.increment(1))

    }

    fun nayVote(questionID:String, voterID:String){
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val voteMap = hashMapOf(
            "vote_id" to System.nanoTime().toString(),
            "question_id" to questionID,
            "timestamp" to currentDate,
            "vote" to false,
            "voter_id" to voterID
        )
        todayRef.document(questionID).collection("votes").document(voterID).set(voteMap, SetOptions.merge())
        todayRef.document(questionID).update("nay_votes", FieldValue.increment(1))
    }

    fun getCurrentUser():FirebaseUser?{
        return mAuth.currentUser
    }

    fun getTodaysQuestions(callback:(List<Question>) -> Unit){
        todayRef.get().addOnCompleteListener {
            val questionList = ArrayList<Question>()
            if(it.isSuccessful){
                Log.d(TAG, "build list")
                val result = it.result
                if( result?.documents != null){
                    for(doc in result.documents){
                        val q = doc.toObject(Question::class.java)
                        q!!.id = doc.id
                        questionList.add(q)
                    }
                    callback.invoke(questionList)
                }
            }else{
                Log.e(TAG,it.exception.toString())
            }
        }
    }

    fun getCurrentUserVotes(uid:String, callback: (List<Vote>) -> Unit){
        db.collectionGroup("votes").whereEqualTo("voter_id", uid).get().addOnCompleteListener {
            val voteList = ArrayList<Vote>()
            if(it.isSuccessful){
                Log.d(TAG, "build list")
                val result = it.result
                if( result?.documents != null){
                    for(doc in result.documents){
                        val q = doc.toObject(Vote::class.java)
                        //q!!.id = doc.id
                        voteList.add(q!!)
                    }
                    callback.invoke(voteList)
                }
            }else{
                Log.e(TAG,it.exception.toString())
            }
        }
    }

    fun commentTodaysQuestions(comment:Comment, callback: () -> Unit){
        //todo add failure state
        todayRef.document(comment.postID).collection("comments").document(comment.commentID).set(comment).addOnCompleteListener {
            if(it.isSuccessful){
                callback.invoke()
            }else{
                Log.e("err", it.exception.toString())
                callback.invoke()
            }
        }
    }

    fun getCommentsFromPost(postID:String, communityQuestion:Boolean,  callback: (ArrayList<Comment>) -> Unit){
        if(communityQuestion){
            Log.e(TAG, "search community questions")
        }else{
            todayRef.document(postID).collection("comments").get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result
                    if(result?.documents == null || result.documents.isEmpty()){
                        Log.e(TAG, "No comments on post")
                    }else{
                        val commentsRaw = ArrayList<Comment>()
                        for(doc in result.documents){
                            //build comment from
                            val comment = doc.toObject(Comment::class.java)
                            commentsRaw.add(comment!!)
                        }

                        callback.invoke(commentsRaw)
                    }
                }else{
                    Log.e(TAG,it.exception.toString())
                }
            }
        }
    }





}