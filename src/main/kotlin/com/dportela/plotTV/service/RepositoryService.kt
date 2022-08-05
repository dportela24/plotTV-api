package com.dportela.plotTV.service

import com.dportela.plotTV.model.applicational.Series
import com.dportela.plotTV.model.dao.GenreDAO
import com.dportela.plotTV.model.dao.SeriesDAO
import com.dportela.plotTV.repository.GenreRepository
import com.dportela.plotTV.repository.SeriesRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RepositoryService(
    val seriesRepository: SeriesRepository,
    val genreRepository: GenreRepository
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    fun saveSeries(series: Series) {
        logger.info("Getting series DAO")
        val seriesDAO = SeriesDAO.fromApplicationModel(series).also { seriesDAO ->
            series.genres.map { genre ->
                val genreDAO = genreRepository.findByGenre(genre) ?: GenreDAO(genre = genre)
                seriesDAO.addGenre(genreDAO)
            }
        }

        logger.info("Saving series")
        seriesRepository.save(seriesDAO)
        logger.info("Series saved")
    }

    fun findSeriesByImdbId(imdbId: String) = seriesRepository.findByImdbId(imdbId)?.toApplicationModel()

    fun existsByImdbId(imdbId: String) = seriesRepository.existsByImdbId(imdbId)

    fun findSeriesByName(name: String) = seriesRepository.findByName(name)?.toApplicationModel()
}