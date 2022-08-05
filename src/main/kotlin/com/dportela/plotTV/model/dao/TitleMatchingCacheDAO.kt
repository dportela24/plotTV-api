package com.dportela.plotTV.model.dao

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
data class TitleMatchingCacheDAO(
    val imdbId: String,
    val title: String
)