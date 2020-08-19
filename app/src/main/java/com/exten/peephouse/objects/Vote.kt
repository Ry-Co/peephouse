package com.exten.peephouse.objects

data class Vote(
    var voter_id: String = "",
    var vote_id:String = "",
    var vote: Boolean = false,
    var question_id: String = "",
    var timestamp: String = ""
)