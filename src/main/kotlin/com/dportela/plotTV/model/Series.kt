package com.dportela.plotTV.model

import com.dportela.plotTV.model.dao.GenreDAO
import com.dportela.plotTV.model.dao.SeriesDAO
import java.time.Duration
import java.time.Instant

data class Series (
    val imdbId: String,
    val name: String,
    val originalName: String?,
    val summary: String?,
    val episodeDuration: Duration?,
    val startYear: Int,
    val endYear: Int?,
    val genres: Set<String>,
    val ratingValue: Float?,
    val ratingCount: Int?,
    val posterURL: String?,
    val numberSeasons: Int,
    val seasons: List<Season>
)