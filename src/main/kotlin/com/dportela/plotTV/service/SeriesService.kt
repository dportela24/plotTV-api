package com.dportela.plotTV.service

import com.dportela.plotTV.model.Series
import com.dportela.plotTV.model.dao.GenreDAO
import com.dportela.plotTV.model.exception.ConnectionErrorException
import com.dportela.plotTV.repository.GenreRepository
import com.dportela.plotTV.repository.SeriesRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.net.ConnectException

@Service
class SeriesService(
    val seriesRepository: SeriesRepository,
    val genreRepository: GenreRepository,
    val scrapperService: ScrapperService
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    fun createSeries(imdbId: String) : Series? {
        val series = scrapperService.fetchSeries(imdbId)

        series?.toDAO()?.also { seriesDAO ->
            series.genres.map { genre ->
                val genreDAO = genreRepository.findByGenre(genre) ?: GenreDAO(genre = genre)
                seriesDAO.genres.add(genreDAO)
            }

            seriesRepository.save(seriesDAO)
        }

        return series
    }

    fun getSeries(imdbId: String): Series? {
        val seriesDAO = seriesRepository.findByImdbId(imdbId)

        return seriesDAO?.let { Series.fromDAO(it) } ?: createSeries(imdbId)
    }
}