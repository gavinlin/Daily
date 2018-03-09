package com.gavincode.bujo.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.gavincode.bujo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

/**
 * Created by gavinlin on 26/2/18.
 */

class DailyPlanFragment: Fragment() {
    companion object {
        fun getInstance(): DailyPlanFragment {
            val fragment = DailyPlanFragment()
            return fragment
        }
    }

    @BindView(R.id.daily_plan_content)
    lateinit var contentTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_daily_plan, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("users").document(it.uid).collection("tasks")
                    .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        Timber.d("${doc.id} => ${doc.data["content"]}")
                    }
                } else {
                    it.exception?.printStackTrace()
                }
            }
        }
    }
}