package com.exten.peephouse.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    val commentID:String = "",
    val commenterID: String = "",
    val postID: String = "",
    val parentCommentID: String = "",
    val text: String = "",
    val voteOnPost: Boolean = false,
    val color:Int = 0,
    var subComments: ArrayList<Comment> = arrayListOf<Comment>()
) : Parcelable {
    constructor():this("","","","","",false, 0, arrayListOf<Comment>())
    constructor(cli:CommentListItem) : this(text = cli.comment.text,commentID = cli.comment.commentID, commenterID =cli.comment.commenterID, postID = cli.comment.postID, parentCommentID = cli.comment.parentCommentID, voteOnPost = cli.comment.voteOnPost, subComments = cli.comment.subComments)
}