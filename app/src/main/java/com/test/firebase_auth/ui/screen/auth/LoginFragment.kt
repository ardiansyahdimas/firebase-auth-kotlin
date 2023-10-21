package com.test.firebase_auth.ui.screen.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.test.firebase_auth.App
import com.test.firebase_auth.R
import com.test.firebase_auth.databinding.FragmentLoginBinding
import com.test.firebase_auth.shared_preference.SharedPrefManager
import com.test.firebase_auth.ui.screen.home.HomeActivity
import com.test.firebase_auth.utils.Utils.errorEditText
import com.test.firebase_auth.utils.Utils.isEmailValid
import com.test.firebase_auth.utils.Utils.isLoading
import com.test.firebase_auth.utils.Utils.showNotif

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val sharedPref = SharedPrefManager(App.context)

    private lateinit var  auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        login()
        onClick()
    }

    private fun login(){
        with(binding){
            btnLogin.setOnClickListener {
                val email = edtEmail.text.toString().trim()
                val password = edtPassword.text.toString().trim()
                when {
                    email.isEmpty() -> errorEditText(edtEmail, getString(R.string.masukkan_email_anda))
                    !isEmailValid(email) -> errorEditText(edtEmail, getString(R.string.email_tidak_valid))
                    password.isEmpty() -> errorEditText(edtPassword, getString(R.string.masukkan_password_anda))
                    else -> {
                        isLoading(true, progressBar, btnLogin)
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) {
                            if (it.isSuccessful) {
                                sharedPref.setLogin(true)
                                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                                requireActivity().finishAffinity()
                            }
                            val message = if (it.isSuccessful) getString(R.string.info_sucess, "Login", "") else getString(R.string.info_failed, "Login", it.exception?.message)
                            showNotif(requireActivity(), "Login", message)
                            isLoading(false, progressBar, btnLogin)
                        }
                    }
                }
            }
        }
    }

    private fun onClick(){
        binding.tvForgotPassword.setOnClickListener {
            replaceFragment(ForgotPasswordFragment(),R.id.frame_container)
        }
        binding.tvSignUp.setOnClickListener {
            replaceFragment(RegisterFragment(),R.id.frame_container)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun replaceFragment(fragment: Fragment, containerId: Int) {
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(containerId, fragment, fragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
    }
}