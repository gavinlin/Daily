package com.gavincode.bujo.presentation.ui.bullet

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.view.*
import com.gavincode.bujo.R
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.presentation.ui.Navigator
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_bullet.*
import kotlinx.android.synthetic.main.view_bullet_content.*
import javax.inject.Inject

/**
 * Created by gavinlin on 20/3/18.
 */

class BulletFragment: Fragment() {

    companion object {

        fun newInstance(bulletId: String, date: Long): Fragment {
            val bundle = Bundle()
            bundle.putString(Navigator.ARG_BULLET_ID, bulletId)
            bundle.putLong(Navigator.ARG_DATE_LONG, date)
            val fragment = BulletFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bulletViewModel: BulletViewModel
    lateinit var contentViewHelper: ContentViewHelper
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        contentViewHelper = ContentViewHelper()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bullet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bulletViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BulletViewModel::class.java)
        bindViewModel()
        prepareView()
    }

    private fun prepareView() {
        bullet_menu.setOnClickListener {
            handleMenuClicked()
        }

        bullet_add.setOnClickListener {
            handleAddClicked()
        }
    }

    private fun setContentViewTouchDelegate() {
        bullet_content_parent_layout.post {
            val actualArea = Rect()
            bullet_content_parent_layout.getHitRect(actualArea)
            val touchDelegate = TouchDelegate(actualArea, bullet_content_edit_text)
            bullet_content_parent_layout.touchDelegate = touchDelegate
        }
    }

    private fun bindViewModel() {
        bulletViewModel.getDailyBullet()
                .observe(this, Observer{
                    it?.apply { render(this) }
                })

        bulletViewModel.getDailyBulletState()
                .observe(this, Observer {
                    it?.apply { handleBulletStateChanged(this) }
                })

        arguments?.getString(Navigator.ARG_BULLET_ID)?.apply {
            if (this.isNullOrBlank()) {
                bulletViewModel.newDailyBullet(arguments?.getLong(Navigator.ARG_DATE_LONG))
            } else {
                bulletViewModel
                        .fetchDailyBullet(arguments?.getString(Navigator.ARG_BULLET_ID) ?: "")
            }
        }
    }

    private fun handleBulletStateChanged(bulletModel: BulletModel) {
        when (bulletModel) {
            is BulletModel.Saved -> { onSaved() }
            is BulletModel.Deleted -> { onDeleted() }
        }
    }

    private fun onDeleted () {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun onSaved() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun render(dailyBullet: DailyBullet) {
        bullet_title.setText(dailyBullet.title)
        if (bullet_title.text.isNullOrBlank()) {
            bullet_title.requestFocus()
        }
        if (!dailyBullet.isList) {
            contentViewHelper.setContentView(PlainContentView(bullet_content_parent_layout))
            contentViewHelper.setData(dailyBullet.content)
        } else {
            contentViewHelper.setContentView(ListContentView(bullet_content_parent_layout))
            contentViewHelper.setData(dailyBullet.content)
        }
    }

    fun handleBack() {
        bulletViewModel.getDailyBullet().value?.title = bullet_title.text.toString()
        bulletViewModel.getDailyBullet().value?.content = contentViewHelper.getData()

        bulletViewModel.exit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId) {
                android.R.id.home -> {
                    handleBack()
                }
                else -> {}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleMenuClicked() {
        activity?.apply {
            val bottomSheetDialog = BottomSheetDialog(this)
            val sheetView = layoutInflater.inflate(R.layout.dialog_bullet_edit, null)
            bottomSheetDialog.setContentView(sheetView)
            sheetView.findViewById<View>(R.id.dialog_bottom_sheet_delete)
                    .setOnClickListener {
                        bottomSheetDialog.dismiss()
                        bulletViewModel.delete()
                        finish()
                    }
            bottomSheetDialog.show()
        }
    }

    private fun handleAddClicked() {
        activity?.apply {
            val bottomSheetDialog = BottomSheetDialog(this)
            val sheetView = layoutInflater.inflate(R.layout.dialog_bullet_add, null)
            bottomSheetDialog.setContentView(sheetView)
            sheetView.findViewById<View>(R.id.dialog_bottom_sheet_list)
                    .setOnClickListener {
                        bottomSheetDialog.dismiss()
                        bulletViewModel.changeCheck(contentViewHelper.getData())
                    }
            bottomSheetDialog.show()
        }
    }
}