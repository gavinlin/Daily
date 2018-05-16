package com.gavincode.bujo.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gavincode.bujo.presentation.ui.bullet.BulletViewModel
import com.gavincode.bujo.presentation.ui.main.DailyListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by gavinlin on 24/3/18.
 */

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BulletViewModel::class)
    abstract fun bindBulletViewModel(bulletViewModel: BulletViewModel)
            : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DailyListViewModel::class)
    abstract fun bindDailyListViewModel(dailyListViewModel: DailyListViewModel)
            : ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory)
            : ViewModelProvider.Factory
}