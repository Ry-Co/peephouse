package com.exten.peephouse.ui.community_questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.exten.peephouse.MainActivityViewModel
import com.exten.peephouse.R


class CommunityFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.toolbarMode.value = 2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val discussionButton = requireActivity().findViewById<ImageView>(R.id.left_button_iv)
        val communityButton = requireActivity().findViewById<ImageView>(R.id.right_button_iv)
        discussionButton.setOnClickListener {
            findNavController().navigate(R.id.action_communityFragment_to_discussionFragment)
        }
        communityButton.setOnClickListener {}
    }


}