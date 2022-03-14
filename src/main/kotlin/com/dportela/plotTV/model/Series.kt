package com.dportela.plotTV.model

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
    //val seasons: Set<Season>
) {
    fun toDAO() = SeriesDAO(
        imdbId = imdbId,
        name = name,
        originalName = originalName,
        summary = summary,
        episodeDuration = episodeDuration,
        startYear = startYear,
        endYear = endYear,
        //genres = genres,
        ratingValue = ratingValue,
        ratingCount = ratingCount,
        posterURL = posterURL,
        numberSeasons = numberSeasons,
        updatedAt = Instant.now()
    )
}