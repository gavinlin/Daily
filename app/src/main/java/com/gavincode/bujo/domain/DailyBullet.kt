package com.gavincode.bujo.domain

/**
 * Created by gavinlin on 7/3/18.
 */

data class DailyBullet (
        val id: String,
        val title: String,
        val content: String,
        val ticked: String,
        val date: Long,
        val bullet: Int,
        val images: List<String>?)
