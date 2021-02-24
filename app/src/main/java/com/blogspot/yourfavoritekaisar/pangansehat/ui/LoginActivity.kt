package com.blogspot.yourfavoritekaisar.pangansehat.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.yourfavoritekaisar.pangansehat.R
import com.google.firebase.auth.FirebaseAuth
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.edt_email
import kotlinx.android.synthetic.main.activity_sign_up.edt_password

class LoginActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setTextChangedListener(edt_email)
        setTextChangedListener(edt_password)
        progress_layout.setOnTouchListener { _, _ -> true }

        btn_login.setOnClickListener {
            onLogin()
        }

        btn_signup.setOnClickListener {
            onSingUp()
        }
    }

    private fun setTextChangedListener(edt: MaterialEditText?) {
        edt?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edt.errorColor
            }
        })
    }

    private fun onLogin() {
        var proceed = true
        if (edt_email.text.isNullOrEmpty()) {
            edt_email.error = "Required Email"
            edt_email.errorColor
            proceed = false
        }

        if (edt_password.text.isNullOrEmpty()) {
            edt_password.error = "Required Password"
            edt_password.errorColor
            proceed = false
        }
        if (proceed) {
            progress_layout.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(
                edt_email.text.toString(),
                edt_password.text.toString()
            ).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    progress_layout.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Login Error: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                progress_layout.visibility = View.GONE
                it.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    private fun onSingUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

}