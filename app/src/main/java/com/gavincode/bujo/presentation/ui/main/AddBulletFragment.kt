package com.gavincode.bujo.presentation.ui.main

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.ui.Navigator
import com.gavincode.bujo.presentation.ui.bullet.BulletModel
import com.gavincode.bujo.presentation.ui.bullet.BulletViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_dialogue_add_bullet.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class AddBulletFragment: BottomSheetDialogFragment() {

    companion object {

        fun getInstance(date: Long): BottomSheetDialogFragment{
            val args = Bundle()
            args.putLong(Navigator.ARG_DATE_LONG, date)
            val fragment = AddBulletFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bulletViewModel: BulletViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialogue_add_bullet, container, false)
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            val id = com.google.android.material.R.id.design_bottom_sheet
            val bottomSheet = dialog.findViewById<FrameLayout>(id)
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED)
        }

        return dialog
    }

    var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    override fun onResume() {
        super.onResume()
        onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            view?.viewTreeObserver?.removeOnGlobalLayoutListener(onGlobalLayoutListener)
            setupAnimation()
        }
        view?.viewTreeObserver?.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val date = arguments?.getLong(Navigator.ARG_DATE_LONG) ?: 0

        bulletViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BulletViewModel::class.java)

        if (date != LocalDate.MIN.toEpochDay()) {
            val localDate = LocalDate.ofEpochDay(date)
            setDate(localDate)
        } else {
            bulletViewModel.setDate(LocalDate.MIN)
        }

        bulletViewModel.newDailyBullet(arguments?.getLong(Navigator.ARG_DATE_LONG))
        bulletViewModel.getDailyBulletState().observe(
                viewLifecycleOwner, Observer {
            it?.apply {
                when(this) {
                    is BulletModel.Saved -> {
                        targetFragment?.onActivityResult(Navigator.REQ_BULLET_ADD, Activity.RESULT_OK, Intent())
                        dismiss()
                    }
                }
            }
        })

        add_bullet_save_button.setOnClickListener {
            bulletViewModel.exit()
        }

        add_task_edit_text.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                bulletViewModel.setTitle(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        val localDate = LocalDate.ofEpochDay(date)

        date_image_view.setOnClickListener {
            val datePickerDialog = DatePickerDialog(context,
                    DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                        val newDate = LocalDate.of(year, monthOfYear, dayOfMonth)
                        setDate(newDate)
                        bulletViewModel.setDate(newDate)
                    }, localDate.year, localDate.monthValue, localDate.dayOfMonth)
            datePickerDialog.show()
        }
    }

    private fun setDate(localDate: LocalDate) {
        add_task_date_chip.visibility = View.VISIBLE
        add_task_date_chip.chipText =
                localDate.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))
        add_task_date_chip.setOnCloseIconClickListener {
            it.visibility = View.GONE
            bulletViewModel.setDate(LocalDate.MIN)
        }
    }

    //TODO state should stay in view model
    private fun setupAnimation() {

        var clicked = false
        val addImageViewOriginX = add_image_view.x / 4f
        val listImageViewOriginX = list_image_view.x / 4f
        val dateImageViewOriginX = date_image_view.x / 4f
        list_image_view.imageAlpha = 0
        date_image_view.imageAlpha = 0
        add_image_view.setOnClickListener {
            clicked = !clicked
            if (!clicked) {
                val backgroundColorAnimator = ObjectAnimator.ofArgb(add_image_view,
                        "backgroundTint", ContextCompat.getColor(context!!, android.R.color.black),
                        ContextCompat.getColor(context!!, R.color.colorAccent))
                backgroundColorAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Int
                    add_image_view.imageTintList = ColorStateList.valueOf(animatedValue)
                }
                val rotateAnimator = ObjectAnimator.ofFloat(add_image_view,
                        "rotation", 45f, 0f)
                val moveAnimator = ObjectAnimator.ofFloat(list_image_view,
                        "translationX", addImageViewOriginX - listImageViewOriginX)
                val moveAnimator2 = ObjectAnimator.ofFloat(date_image_view,
                        "translationX", addImageViewOriginX - dateImageViewOriginX)
                val animatorSet = AnimatorSet()
                val alphaAnimator = ValueAnimator.ofInt(255, 0)
                alphaAnimator.interpolator = DecelerateInterpolator()
                alphaAnimator.addUpdateListener {
                    list_image_view.imageAlpha = it.animatedValue as Int
                    date_image_view.imageAlpha = it.animatedValue as Int
                }
                animatorSet.playTogether(rotateAnimator, moveAnimator, moveAnimator2, backgroundColorAnimator, alphaAnimator)
                animatorSet.addListener(object: Animator.AnimatorListener{
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        list_image_view.isFocusable = false
                        date_image_view.isFocusable = false
                        list_image_view.isClickable = false
                        date_image_view.isClickable = false
                        date_image_view.visibility = View.INVISIBLE
                        list_image_view.visibility = View.INVISIBLE
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                    }

                })
                animatorSet.start()

            } else {
                val backgroundColorAnimator = ObjectAnimator.ofArgb(add_image_view,
                        "backgroundTint", ContextCompat.getColor(context!!, R.color.colorAccent),
                        ContextCompat.getColor(context!!, android.R.color.black))
                backgroundColorAnimator.addUpdateListener {
                    val animatedValue = it.animatedValue as Int
                    add_image_view.imageTintList = ColorStateList.valueOf(animatedValue)
                }
                val rotateAnimator = ObjectAnimator.ofFloat(add_image_view,
                        "rotation", 0f, 45f)
                val moveAnimator = ObjectAnimator.ofFloat(list_image_view,
                        "translationX", listImageViewOriginX - addImageViewOriginX)
                val moveAnimator2 = ObjectAnimator.ofFloat(date_image_view,
                        "translationX", dateImageViewOriginX - addImageViewOriginX)
                val alphaAnimator = ValueAnimator.ofInt(0, 255)
                alphaAnimator.interpolator = DecelerateInterpolator()
                alphaAnimator.addUpdateListener {
                    list_image_view.imageAlpha = it.animatedValue as Int
                    date_image_view.imageAlpha = it.animatedValue as Int
                }
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(rotateAnimator, moveAnimator, moveAnimator2, backgroundColorAnimator, alphaAnimator)
                animatorSet.addListener(object: Animator.AnimatorListener{
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        list_image_view.isEnabled = true
                        date_image_view.isEnabled = true
                        list_image_view.isFocusable = true
                        date_image_view.isFocusable = true
                        list_image_view.isClickable = true
                        date_image_view.isClickable = true
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                        date_image_view.visibility = View.VISIBLE
                        list_image_view.visibility = View.VISIBLE
                    }

                })
                animatorSet.start()
            }
        }
    }
}