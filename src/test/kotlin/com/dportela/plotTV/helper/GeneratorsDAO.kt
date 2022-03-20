package com.dportela.plotTV.helper

import com.dportela.plotTV.configuration.TemporalAccessorConverter
import com.dportela.plotTV.model.dao.EpisodeDAO
import com.dportela.plotTV.model.dao.GenreDAO
import com.dportela.plotTV.model.dao.SeasonDAO
import com.dportela.plotTV.model.dao.SeriesDAO
import org.springframework.http.HttpStatus.Series
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.TemporalAccessor
import javax.persistence.*
import kotlin.random.Random.Default.nextFloat
import kotlin.random.Random.Default.nextInt

fun generateSeriesDAO(
    id: Long? = null,
    imdbId: String = generateImdbId(),
    genres: MutableList<GenreDAO> = mutableListOf(),
    summary: String? = "My series summary",
    name: String = "My series name",
    originalName: String? = "My series originalName",
    episodeDuration: Duration? = Duration.ofMinutes(1).plusMinutes(30),
    startYear: Int = nextInt(2000, 2010),
    endYear: Int? = nextInt(2010, 2020),
    ratingValue: Float? = nextFloat()*10,
    ratingCount: Int? = nextInt(1, 100000),
    posterURL: String? = "My series poster",
    numberSeasons: Int = nextInt(1, 4),
    seasons: MutableList<SeasonDAO> = (1..numberSeasons).map { generateSeasonDAO(number = it) }.toMutableList(),
    updatedAt: Instant = Instant.now()
) : SeriesDAO {
    val seriesDAO = SeriesDAO(
        id = id,
        imdbId = imdbId,
        summary = summary,
        name = name,
        originalName = originalName,
        episodeDuration = episodeDuration,
        startYear = startYear,
        endYear = endYear,
        ratingValue = ratingValue,
        ratingCount = ratingCount,
        posterURL = posterURL,
        numberSeasons = numberSeasons,
        updatedAt = updatedAt
    )

    if (genres.isEmpty()) listOf("Comedy", "Drama", "Romance").forEach { genres.add(generateGenreDAO(genre = it)) }

    genres.forEach { seriesDAO.addGenre(it) }
    seasons.forEach { seriesDAO.addSeason(it) }

    return seriesDAO
}

fun generateGenreDAO(
    id: Long? = null,
    genre: String = "Comedy",
    series: MutableList<SeriesDAO> = mutableListOf()
) = GenreDAO(
    id = id,
    genre = genre,
    series = series
)

fun generateSeasonDAO(
    id: Long? = null,
    series: SeriesDAO? = null,
    number: Int = nextInt(1, 25),
    numberEpisodes: Int = nextInt(1, 5),
    episodes: MutableList<EpisodeDAO> = (1..numberEpisodes).map { generateEpisodeDAO(number = it) }.toMutableList(),
) = SeasonDAO(
        id = id,
        series = series,
        number = number,
    ).apply {
        episodes.forEach { this.addEpisode(it) }
    }

fun generateEpisodeDAO(
    id: Long? = null,
    imdbId: String = generateImdbId(),
    season: SeasonDAO? = null,
    summary: String? = "My episode summary",
    airdate: TemporalAccessor? = LocalDate.now(),
    number: Int = nextInt(1, 25),
    name: String = "My episode name",
    ratingValue: Float? = nextFloat()*10,
    ratingCount: Int? = nextInt(0, 10000),
) = EpisodeDAO(
    id = id,
    imdbId = imdbId,
    season = season,
    summary = summary,
    airdate = airdate,
    number = number,
    name = name,
    ratingValue = ratingValue,
    ratingCount = ratingCount
)