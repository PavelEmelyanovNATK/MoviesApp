package com.emelyanov.moviesapp.shared.domain.services.stringextractor

import android.content.Context

class StringExtractor(
    private val context: Context
) : IStringExtractor {
    override fun getString(resId: Int): String = context.getString(resId)
}