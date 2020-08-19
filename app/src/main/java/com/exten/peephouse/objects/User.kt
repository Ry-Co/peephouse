package com.exten.peephouse.objects

data class User(
    val uid: String = "",
    val premium: Boolean = false,
    var userName: String = ""
)