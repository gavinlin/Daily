package com.gavincode.bujo.presentation.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gavincode.bujo.R

class SettingFragment: androidx.fragment.app.Fragment() {

    companion object {
        fun getInstance(): SettingFragment {
            val fragment = SettingFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.setting_toolbar)
        toolbar.setTitle(R.string.setting_title)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        return view
    }
}