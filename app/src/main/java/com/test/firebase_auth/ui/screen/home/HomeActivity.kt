package com.test.firebase_auth.ui.screen.home

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.test.firebase_auth.App
import com.test.firebase_auth.R
import com.test.firebase_auth.databinding.ActivityHomeBinding
import com.test.firebase_auth.model.User
import com.test.firebase_auth.shared_preference.SharedPrefManager
import com.test.firebase_auth.ui.screen.auth.AuthActivity
import com.test.firebase_auth.utils.Utils.isLoading
import com.test.firebase_auth.utils.Utils.showNotif

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private val sharedPref = SharedPrefManager(App.context)
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userAdapter = UserAdapter()
        firebaseDatabase = FirebaseDatabase.getInstance()
        getListUsers()
        logout()
        searchUser()
    }

    private fun searchUser(){
      binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isNotEmpty() == true) {
                    userAdapter.filter.filter(query)
                } else {
                    showNotif(this@HomeActivity, getString(R.string.app_name), "Enter user name")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun getListUsers(){
        isLoading(true, binding.progressBar)
        binding.rvUsers.isVisible = false
        firebaseDatabase.getReference("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userList = ArrayList<User>()

                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { userList.add(it) }
                }
                userAdapter.setData(userList)
                isLoading(false, binding.progressBar)
                binding.rvUsers.isVisible = true
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showNotif(this@HomeActivity, getString(R.string.app_name), databaseError.message)
            }
        })

        with(binding.rvUsers) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }


    private fun logout(){
        val auth = FirebaseAuth.getInstance()
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            sharedPref.setLogin(false)
            startActivity(Intent(this, AuthActivity::class.java))
            finishAffinity()
        }
    }
}