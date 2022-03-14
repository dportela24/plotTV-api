package com.dportela.plotTV.service

import com.dportela.plotTV.model.Series
import com.dportela.plotTV.repository.SeriesRepository
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.random.Random

@Service
class SeriesService(
    val seriesRepository: SeriesRepository
) {
    fun createSeries() {
        val series = Series(
            imdbId = "tt" + Random.nextInt(10000000, 99999999),
            name = "My name " + Random.nextInt(0, 10),
            originalName = "My original name " + Random.nextInt(0, 10),
            summary = "My summary " + Random.nextInt(0, 10),
            episodeDuration = Duration.ofHours(Random.nextInt(1, 10).toLong()).plusMinutes(Random.nextInt(1, 10).toLong()),
            startYear = Random.nextInt(2000, 2010),
            endYear = Random.nextInt(2010, 2020),
            genres = setOf("Comedy", "Drama"),
            ratingValue = Random.nextFloat() *10,
            ratingCount = Random.nextInt(1, 1000000),
            posterURL = "My url" + Random.nextInt(0, 10),
            numberSeasons = Random.nextInt(1, 10),
            //seasons: Set<Season>
        )

        val seriesDAO = series.toDAO()

        seriesRepository.save(seriesDAO)
    }
}