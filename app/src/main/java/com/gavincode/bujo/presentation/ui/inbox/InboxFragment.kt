package com.gavincode.bujo.presentation.ui.inbox

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gavincode.bujo.R


class InboxFragment: androidx.fragment.app.Fragment() {
    companion object {
        fun getInstance(): InboxFragment {
            return InboxFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)
        return view
    }
}

