package com.example.yourmusic.Activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yourmusic.MainActivity
import com.example.yourmusic.R
import com.example.yourmusic.Utils
import com.example.yourmusic.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signupTxt.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        auth = FirebaseAuth.getInstance()

        if (FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }
        else{
            binding.signUpBtn.setOnClickListener {
                signIn()
            }
        }

    }

    private fun signIn() {
        val email =binding.email.text.toString()
        val password =binding.password.text.toString()

        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Fill the all Require Details", Toast.LENGTH_SHORT).show()
        }

        else{
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    Utils.showDialog(this,"Sign in...")

                    startActivity(Intent(this, MainActivity::class.java))
                }

                else{
                    Toast.makeText(this, it.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}