package com.exten.peephouse.ui.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.exten.peephouse.utils.DataRepository
import com.exten.peephouse.MainActivityViewModel
import com.exten.peephouse.R
import com.exten.peephouse.objects.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class PhoneVerification : Fragment() {
    private val args: PhoneVerificationArgs by navArgs()
    private var verificationID: String? = null
    private val viewModel: MainActivityViewModel by activityViewModels()
    private val dataRepo = DataRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_phone_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val number = args.phoneNumber
        val verifyButton = view.findViewById<Button>(R.id.anonymous_button)
        val codeET = view.findViewById<EditText>(R.id.SMScode_ET)
        sendCode(number.trim())
        verifyButton.setOnClickListener {
            val code = codeET.text.toString()
            if(verificationID != null){
                val cred = PhoneAuthProvider.getCredential(verificationID!!, code)
                signInWithCred(cred)
            }

        }


    }

    private fun sendCode(number:String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            phoneAuthCallbacks
        )
    }


    private val phoneAuthCallbacks = object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            signInWithCred(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(context, p0.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            verificationID = p0
        }
    }

    private fun signInWithCred(phoneAuthCred:PhoneAuthCredential){
        if(viewModel.currentUser == null){
            //sign in user, and register new?
            dataRepo.mAuth.signInWithCredential(phoneAuthCred).addOnCompleteListener {
                if(it.isSuccessful){
                    viewModel.currentUser = dataRepo.getCurrentUser()
                    addNewUserToDatabase(viewModel.currentUser!!){
                        if( it.isSuccessful){
                            viewModel.init()
                            findNavController().navigate(R.id.action_phoneVerificationFragment_to_questionsFragment)
                        }else{
                            Toast.makeText(activity, "Eorr: "+it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }


                }else{
                    Toast.makeText(activity, "There was an error:"+it.exception.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("TAG", it.exception.toString())
                }
            }
        }else{
            //clear current user and sign in with new one?
            Log.wtf("huh?", "user is not null but is signing in")
        }
    }

    private fun addNewUserToDatabase(user: FirebaseUser, callback: () -> Unit){
        val newUser = User(uid = user.uid)
        dataRepo.db.collection("users").document(user.uid).set(newUser).addOnCompleteListener {
            if(it.isSuccessful){
                callback.invoke()
            }else{
                Toast.makeText(activity, "There was an error:"+it.exception.toString(), Toast.LENGTH_SHORT).show()
                Log.e("TAG", it.exception.toString())
            }
        }
    }

}



