package com.gavincode.bujo.presentation.ui.bullet

sealed class BulletModel {
    class Saved(): BulletModel()
    class Deleted(): BulletModel()
}