package com.dportela.plotTV.service

import com.dportela.plotTV.model.Series
import com.dportela.plotTV.model.dao.GenreDAO
import com.dportela.plotTV.model.dao.SeriesDAO
import com.dportela.plotTV.repository.GenreRepository
import com.dportela.plotTV.repository.SeasonRepository
import com.dportela.plotTV.repository.SeriesRepository
import org.springframework.stereotype.Service

@Service
class RepositoryService(
    val seriesRepository: SeriesRepository,
    val seasonRepository: SeasonRepository,
    val genreRepository: GenreRepository
) {
    fun saveSeries(series: Series) {
        SeriesDAO.fromApplicationModel(series).also { seriesDAO ->
            series.genres.map { genre ->
                val genreDAO = genreRepository.findByGenre(genre) ?: GenreDAO(genre = genre)
                seriesDAO.addGenre(genreDAO)
            }
            seriesRepository.save(seriesDAO)
        }
    }

    fun findSeriesByImdbId(imdbId: String) = seriesRepository.findByImdbId(imdbId)?.toApplicationModel()
}