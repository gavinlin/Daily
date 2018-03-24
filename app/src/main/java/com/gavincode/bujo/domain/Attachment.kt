package com.gavincode.bujo.domain

/**
 * Created by gavinlin on 24/3/18.
 */

data class Attachment (
        val id: String,
        val uriPath: String,
        val name: String,
        val size: Long,
        val mimeType: String
)