package com.test.firebase_auth.ui.screen.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.test.firebase_auth.R
import com.test.firebase_auth.databinding.FragmentForgotPasswordBinding
import com.test.firebase_auth.utils.Utils.errorEditText
import com.test.firebase_auth.utils.Utils.isEmailValid
import com.test.firebase_auth.utils.Utils.isLoading
import com.test.firebase_auth.utils.Utils.showNotif


class ForgotPasswordFragment : Fragment() {
    private var _binding:FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        submitEmail()
        back()
    }

    private fun back(){
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun submitEmail(){
        with(binding){
            btnSubmit.setOnClickListener {
                val email = edtEmail.text.toString().trim()

                when{
                   email.isEmpty() -> errorEditText(edtEmail, getString(R.string.masukkan_email_anda))
                   !isEmailValid(email) -> errorEditText(edtEmail, getString(R.string.email_tidak_valid))
                   else -> {
                       isLoading(true,progressBar, btnSubmit)
                       auth.sendPasswordResetEmail(email)
                           .addOnCompleteListener { task ->
                               val message = if (task.isSuccessful) getString(R.string.info_sucess, getString(R.string.reset_password),getString(R.string.please_check_your_email)) else getString(R.string.info_failed, getString(R.string.reset_password), task.exception?.message)
                               showNotif(requireActivity(),getString(R.string.forgot_password),message)
                               edtEmail.setText("")
                               isLoading(false,progressBar, btnSubmit)
                           }
                   }
                }
            }
        }
    }
}