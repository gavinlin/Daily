package com.gavincode.bujo.presentation.ui.entry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signin.*
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by gavinlin on 25/2/18.
 */

class SignInActivity: AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        sign_in_button.setOnClickListener {
            onClick()
        }
    }

    fun onClick() {
        val providers = Arrays.asList(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val intent = Intent(this, MainActivity::class.java)
            val firestore = FirebaseFirestore.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                val user = HashMap<String, Any>()
                user.put("displayName", it.displayName as Any)
                user.put("paid", false )
                user.put("photoUrl", it.photoUrl.toString() as Any)
                user.put("email", it.email as Any)
                user.put("uid", it.uid)
                firestore.collection("users")
                        .document(it.uid)
                        .set(user)
            }
            startActivity(intent)
            finish()
        }
    }
}