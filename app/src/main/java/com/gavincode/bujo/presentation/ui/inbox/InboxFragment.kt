package com.gavincode.bujo.presentation.ui.inbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gavincode.bujo.R
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.presentation.ui.Navigator
import com.gavincode.bujo.presentation.ui.bullet.BulletActivity
import com.gavincode.bujo.presentation.ui.main.*
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_inbox.*
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject


class InboxFragment: Fragment(), DailyListClickListener {

    companion object {
        fun getInstance(): InboxFragment {
            return InboxFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var dailyListViewModel: DailyListViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.inbox_toolbar)
        toolbar.title = getString(R.string.inbox)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
        bindViewModel()
        dailyListViewModel.setDate(LocalDate.MIN)
    }

    private fun bindView() {
        inbox_fab.setOnClickListener {
            handleAddClicked()
        }

        inbox_recycler_view.adapter = DailyListAdapter()
        (inbox_recycler_view.adapter as DailyListAdapter).dailyListOnClickListener = this
        inbox_recycler_view.layoutManager = LinearLayoutManager(context)
    }

    private fun handleAddClicked() {
        val addTaskFragment = AddBulletFragment.getInstance(LocalDate.MIN.toEpochDay())
        addTaskFragment.setTargetFragment(this, Navigator.REQ_BULLET_ADD)
        addTaskFragment.show(fragmentManager, "addTask")
    }

    private fun bindViewModel() {
        dailyListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DailyListViewModel::class.java)

        dailyListViewModel.bindUiModel()
                .observe(this, Observer {
                    it?.run { renderUi(this) }
                })
        dailyListViewModel.bindDate()
                .observe(this, Observer {
                    it?.run { dailyListViewModel.fetchLiveData() }
                })
    }

    private fun renderUi(dailyListUiModel: DailyListUiModel) {

        when(dailyListUiModel) {
            is DailyListUiModel.Empty -> onEmpty()
            is DailyListUiModel.DailyBullets -> onShowBullets(dailyListUiModel.dailyBullets)
        }
    }

    private fun onEmpty() {
        Timber.i("inbox is empty")
    }

    private fun onShowBullets(dailyBullets: List<DailyBullet>) {
        (inbox_recycler_view.adapter as DailyListAdapter).updateList(dailyBullets)
    }

    //DailyListClickListener
    override fun onClick(view: View, bulletId: String) {
        val intent = Intent(activity, BulletActivity::class.java)
        intent.putExtra(Navigator.ARG_BULLET_ID, bulletId)
        startActivityForResult(intent, Navigator.REQ_BULLET_EDIT)
    }
}

