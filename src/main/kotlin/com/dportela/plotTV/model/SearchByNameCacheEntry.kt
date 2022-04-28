package com.dportela.plotTV.model

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
data class SearchByNameCacheEntry(
    val imdbId: String,
    val title: String
)