package com.exten.peephouse.objects

import androidx.navigation.NavController
import com.xwray.groupie.ExpandableGroup

class CommentListGroup constructor(navController: NavController, comment:Comment, depth:Int=0): ExpandableGroup(CommentListItem(navController, comment, depth),true){
    init {
        for(c in comment.subComments){
            add(CommentListGroup(navController, c, depth.plus(1)))
        }
    }
}