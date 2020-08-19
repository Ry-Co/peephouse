package com.exten.peephouse.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.exten.peephouse.MainActivityViewModel
import com.exten.peephouse.R
import com.hbb20.CountryCodePicker


class NumberFormatFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_number_format, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val verifyButton = view.findViewById<Button>(R.id.signInButton)
        val phoneNumberET = view.findViewById<EditText>(R.id.passwordEditText)
        val ccp = view.findViewById<CountryCodePicker>(R.id.ccp)
        var phoneNumber = ""
        if(viewModel.currentUser?.phoneNumber != null){
            phoneNumber = viewModel.currentUser!!.phoneNumber!!
        }
        ccp.registerCarrierNumberEditText(phoneNumberET)
        phoneNumberET.setText(phoneNumber)
        verifyButton.setOnClickListener {
            // GO TO PHONE VERIFICATION PAGE WITH CODE
            val fullNumber = ccp.fullNumberWithPlus
            if(ccp.isValidFullNumber){
                phoneNumber = fullNumber
                val action = NumberFormatFragmentDirections.actionNumberFormatFragmentToPhoneVerificationFragment(phoneNumber)
                findNavController().navigate(action)
            }else{
                Toast.makeText(context, "$fullNumber is not a valid number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
