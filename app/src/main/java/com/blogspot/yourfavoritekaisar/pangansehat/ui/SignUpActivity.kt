package com.blogspot.yourfavoritekaisar.pangansehat.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.yourfavoritekaisar.pangansehat.R
import com.blogspot.yourfavoritekaisar.pangansehat.data.DATA_USERS
import com.blogspot.yourfavoritekaisar.pangansehat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseListener = FirebaseAuth.AuthStateListener {
        val user = FirebaseAuth.getInstance().currentUser?.uid
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_sign_up)


        setTextChangedListene(edt_email)
        setTextChangedListene(edt_password)
        setTextChangedListene(edt_username)
        setTextChangedListene(edt_mobile)
        progress_layout_signup.setOnTouchListener { _, _ -> true }

        btn_signup.setOnClickListener {
            onSignUp()
        }

        txt_login.setOnClickListener {
            onLogin()
        }
    }

    private fun setTextChangedListene(edt: MaterialEditText?) {
        edt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edt.errorColor
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun onSignUp() {
        var proceed = true
        if (edt_username.text.isNullOrEmpty()) {
            edt_username.error = "Required Username"
            edt_username.errorColor
            proceed = false
        }

        if (edt_mobile.text.isNullOrEmpty()) {
            edt_mobile.error = "Required Phone Number"
            edt_mobile.errorColor
            proceed = false
        }

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
            progress_layout_signup.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(
                edt_email.text.toString(),
                edt_password.text.toString()
            )
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        progress_layout_signup.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "SignUp Error: ${it.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (firebaseAuth.uid != null) {
                        val email = edt_email.text.toString()
                        val mobile = edt_mobile.text.toString()
                        val username = edt_username.text.toString()
                        val user = User(email, mobile, username, "", "Hello World! I'm new", "", "")

                        firebaseDB.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)
                    }
                    progress_layout_signup.visibility = View.GONE
                }
                .addOnFailureListener {
                    progress_layout_signup.visibility = View.GONE
                }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseListener)
    }

    private fun onLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}