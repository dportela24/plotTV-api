package com.dportela.plotTV.helper

import com.dportela.plotTV.model.Episode
import com.dportela.plotTV.model.ErrorDetails
import com.dportela.plotTV.model.Season
import com.dportela.plotTV.model.Series
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.TemporalAccessor
import kotlin.random.Random

fun generateImdbId() = "tt" + Random.nextInt(10000000, 99999999)

fun generateName() = "My series ${Random.nextInt(1, 101)}"

fun generateSeries(
    imdbId: String = generateImdbId(),
    name: String = "My series ${Random.nextInt(1, 101)}",
    originalName: String? = null,
    summary: String? = "My summary ${Random.nextInt(1, 101)}",
    episodeDuration: Duration? = Duration.ofMinutes(Random.nextLong(20, 90)),
    startYear: Int = Random.nextInt(1960, 2020),
    endYear: Int? = null,
    genres: List<String> = listOf("Comedy", "Drama", "Romance"),
    ratingValue: Float? = Random.nextFloat() * 10,
    ratingCount: Int? = Random.nextInt(1, 1000001),
    posterURL: String? = "My poster ${Random.nextInt(1, 101)}",
    numberSeasons: Int = Random.nextInt(1, 4),
    seasons: List<Season> = (1..numberSeasons).map { generateSeason() }
) = Series(
    imdbId = imdbId,
    name = name,
    originalName = originalName,
    summary = summary,
    episodeDuration = episodeDuration,
    startYear = startYear,
    endYear = endYear,
    genres = genres,
    ratingValue = ratingValue,
    ratingCount = ratingCount,
    posterURL = posterURL,
    numberSeasons = numberSeasons,
    seasons = seasons
)

fun generateSeason(
    number: Int = Random.nextInt(1, 16),
    numberEpisodes: Int = Random.nextInt(1, 4),
    episodes: List<Episode> = (1..numberEpisodes).map { generateEpisode(number = it) }
) = Season(
    number = number,
    numberEpisodes = numberEpisodes,
    episodes = episodes
)

fun generateEpisode(
    imdbId: String = generateImdbId(),
    number: Int = Random.nextInt(1, 25),
    name: String = "My episode ${Random.nextInt(1, 101)}",
    airdate: TemporalAccessor? = LocalDate.of(Random.nextInt(1960, 2021), Random.nextInt(1, 13), Random.nextInt(1, 29)),
    ratingValue: Float? = Random.nextFloat() * 10,
    ratingCount: Int? = Random.nextInt(1, 1000001),
    summary: String? = "My summary ${Random.nextInt(1, 101)}"
) = Episode(
    imdbId = imdbId,
    number = number,
    name = name,
    airdate = airdate,
    ratingValue = ratingValue,
    ratingCount = ratingCount,
    summary = summary
)

fun generateScrapperErrorResponse(errorCode: String, errorMessage: String = "Error message for $errorCode") = ErrorDetails(
    errorCode = errorCode,
    errorType = "Error type for $errorCode",
    errorMessage = errorMessage
)