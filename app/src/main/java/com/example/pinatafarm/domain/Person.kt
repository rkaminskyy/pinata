package com.example.pinatafarm.domain

import android.graphics.Rect

data class Person(
    val name: String,
    val img: String?,
    val faceBounds: List<Rect>?
)

fun Person.largestArea(): Int {
    if (faceBounds.isNullOrEmpty()) return 0

    val largestFaceBounds = this.faceBounds.sortedByDescending { it.width() * it.height() }.first()
    return largestFaceBounds.width() * largestFaceBounds.height()
}