package com.vp.list.model


data class SearchResult private constructor(
    val items: List<ListItem>,
    private val hasResponse: Boolean,
    val totalResult: Int
)
