package com.dportela.plotTV.model.applicational

import java.time.Duration

data class Series (
    val imdbId: String,
    val name: String,
    val originalName: String?,
    val summary: String?,
    val episodeDuration: Duration?,
    val startYear: Int,
    val endYear: Int?,
    val genres: List<String>,
    val ratingValue: Float?,
    val ratingCount: Int?,
    val posterURL: String?,
    val numberSeasons: Int,
    val seasons: List<Season>
)