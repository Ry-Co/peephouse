package com.exten.peephouse.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.exten.peephouse.MainActivityViewModel
import com.exten.peephouse.R

class CoverFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signInButton = view.findViewById<Button>(R.id.sign_in_button)
        val signUpButton = view.findViewById<Button>(R.id.sign_up_button)
        (activity as AppCompatActivity).supportActionBar?.hide()
        if(viewModel.currentUser == null){
            //remain on registration
        }else{
            //check if premium y-> check questions   n->check questions
            findNavController().navigate(R.id.action_coverFragment_to_questionsFragment)
        }
        signInButton.setOnClickListener {
            //findNavController().navigate(R.id.action_coverFragment_to_signInFragment)
            findNavController().navigate(R.id.action_coverFragment_to_numberFormatFragment)
        }
        signUpButton.setOnClickListener {
            //findNavController().navigate(R.id.action_coverFragment_to_signUpFragment)
            findNavController().navigate(R.id.action_coverFragment_to_numberFormatFragment)

        }
    }

}