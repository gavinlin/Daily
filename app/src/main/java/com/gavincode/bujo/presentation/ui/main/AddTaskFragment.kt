package com.gavincode.bujo.presentation.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.*
import com.gavincode.bujo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddTaskFragment: BottomSheetDialogFragment() {

    companion object {
        fun getInstance(): BottomSheetDialogFragment {
            return AddTaskFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialogue_add_bullet, container, false)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
        dialog.window.attributes.gravity = Gravity.BOTTOM
        return dialog
    }
}