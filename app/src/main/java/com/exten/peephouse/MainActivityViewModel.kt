package com.exten.peephouse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exten.peephouse.objects.Question
import com.exten.peephouse.objects.Vote
import com.exten.peephouse.utils.DataRepository

class MainActivityViewModel : ViewModel(){
    private val dataRepo = DataRepository()
    var toolbarMode = MutableLiveData<Int>()
    var currentUser = dataRepo.getCurrentUser()
    var todaysQuestions = MutableLiveData<List<Question>>()
    var usersVotedQuestions = MutableLiveData<List<Vote>>()


    fun init(){
        currentUser = dataRepo.getCurrentUser()
        dataRepo.getTodaysQuestions {
            todaysQuestions.value = it
        }
        if(currentUser != null){
            dataRepo.getCurrentUserVotes(currentUser!!.uid){
                usersVotedQuestions.value = it
            }
        }
    }

    fun getUsersCurrentVotes(callback:(List<Vote>) -> Unit){
        dataRepo.getCurrentUserVotes(currentUser!!.uid){
            callback.invoke(it)
        }
    }

}