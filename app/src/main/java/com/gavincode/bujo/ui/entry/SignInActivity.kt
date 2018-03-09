package com.gavincode.bujo.ui.entry

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.firebase.ui.auth.AuthUI
import com.gavincode.bujo.R
import com.gavincode.bujo.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        ButterKnife.bind(this)
    }


    @OnClick(R.id.sign_in_button)
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