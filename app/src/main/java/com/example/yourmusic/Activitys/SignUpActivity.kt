package com.example.yourmusic.Activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yourmusic.MainActivity
import com.example.yourmusic.Model.UserData
import com.example.yourmusic.R
import com.example.yourmusic.Utils
import com.example.yourmusic.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUpActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private var userInfo = mutableListOf<String>()
    private lateinit var firebaseAuth: FirebaseAuth
    val refrence = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginTxt.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }

        binding.signUpBtn.setOnClickListener {
            signup()
        }
        fatchExistUserData()
    }

    private fun fatchExistUserData() {
        val ref = FirebaseDatabase.getInstance().reference.child("user")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userInfo.clear()
                for (snapshot in snapshot.children) {
                    val data = snapshot.getValue(UserData::class.java)
                    if (data != null) {
                        userInfo.add(data.name!!)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun signup() {
        val name = binding.name.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val userName = binding.userName.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || userName.isEmpty()) {
            Toast.makeText(this, "Fill the all Require Details", Toast.LENGTH_SHORT).show()
        }
        if (userName.equals(userInfo)){
            binding.userName.setError("username already exist")
        }
        else {
            Utils.apply {
                showDialog(this@SignUpActivity, "Working on it.......")
            }
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val UserID = firebaseAuth.currentUser!!.uid
                        val userData = UserData(
                            name = name,
                            email = email,
                            userId = UserID,
                            userName = userName,
                            password = password
                        )

                        val ref = refrence.child("user").child(UserID)
                        ref.setValue(userData).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Utils.showDialog(this, "Sign In Successful.....")
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }

                        }

                    } else {
                        Utils.hideDialog()
                        Toast.makeText(this, it.exception!!.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }


        }

    }
}