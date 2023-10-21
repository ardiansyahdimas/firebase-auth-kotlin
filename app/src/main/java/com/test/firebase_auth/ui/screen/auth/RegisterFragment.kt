package com.test.firebase_auth.ui.screen.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.firebase_auth.R
import com.test.firebase_auth.databinding.FragmentRegisterBinding
import com.test.firebase_auth.model.User
import com.test.firebase_auth.utils.Utils.errorEditText
import com.test.firebase_auth.utils.Utils.isEmailValid
import com.test.firebase_auth.utils.Utils.isLoading
import com.test.firebase_auth.utils.Utils.isPasswordValid
import com.test.firebase_auth.utils.Utils.showNotif

class RegisterFragment : Fragment() {
    private var _binding:FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        firebaseDatabase = Firebase.database
        back()
        signUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun back(){
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun signUp(){
        with(binding){
            btnSignUp.setOnClickListener {
                val fullName = edtName.text.toString().trim()
                val email = edtEmail.text.toString().trim()
                val password = edtPassword.text.toString().trim()
                val cfPassword = edtConfirmPassword.text.toString().trim()

                when{
                    fullName.isEmpty() -> errorEditText(edtName, getString(R.string.enter_your_full_name))
                    fullName.length < 3 -> errorEditText(edtName, getString(R.string.invalid_fullname))
                    email.isEmpty() -> errorEditText(edtEmail, getString(R.string.masukkan_email_anda))
                    !isEmailValid(email) -> errorEditText(edtEmail, getString(R.string.email_tidak_valid))
                    password.isEmpty() -> errorEditText(edtPassword, getString(R.string.masukkan_password_anda))
                    !isPasswordValid(password) -> errorEditText(edtPassword, getString(R.string.invalid_password))
                    cfPassword.isEmpty() -> errorEditText(edtConfirmPassword, getString(R.string.masukkan_password_anda))
                    cfPassword != password -> errorEditText(edtConfirmPassword, getString(R.string.password_not_match))
                    else -> {
                        isLoading(true, progressBar, btnSignUp)
                        auth.createUserWithEmailAndPassword(email, cfPassword).addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build()
                                val newUser = User(fullName, email)
                                firebaseDatabase.getReference("users").child(user?.uid.toString()).setValue(newUser)
                                user?.updateProfile(userProfileChangeRequest)
                                user?.sendEmailVerification()
                            }
                            val message = if (task.isSuccessful) {
                                getString(R.string.info_sucess, getString(R.string.register), getString(R.string.check_email))
                            } else {
                                getString(R.string.info_failed, "Registration", task.exception?.message)
                            }
                            updateUi()
                            showNotif(requireActivity(), getString(R.string.register), message)
                            isLoading(false, progressBar, btnSignUp)
                        }
                    }
                }
            }
        }
    }

    private fun updateUi(){
        with(binding) {
            edtName.setText("")
            edtEmail.setText("")
            edtPassword.setText("")
            edtConfirmPassword.setText("")
        }
    }
}