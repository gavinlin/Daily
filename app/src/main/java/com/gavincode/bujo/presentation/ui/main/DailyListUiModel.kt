package com.gavincode.bujo.presentation.ui.main

import com.gavincode.bujo.domain.DailyBullet


sealed class DailyListUiModel {
    class Idle: DailyListUiModel()
    class Empty: DailyListUiModel()
    class Loading: DailyListUiModel()
    class DailyBullets(val dailyBullets: List<DailyBullet>): DailyListUiModel()
}