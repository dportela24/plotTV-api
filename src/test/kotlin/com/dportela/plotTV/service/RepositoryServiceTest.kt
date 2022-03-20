package com.dportela.plotTV.service

import com.dportela.plotTV.helper.*
import com.dportela.plotTV.model.dao.SeriesDAO
import com.dportela.plotTV.repository.GenreRepository
import com.dportela.plotTV.repository.SeasonRepository
import com.dportela.plotTV.repository.SeriesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RepositoryServiceTest {
    val seriesRepository = mockk<SeriesRepository>()

    val seasonRepository = mockk<SeasonRepository>()

    val genreRepository  = mockk<GenreRepository>()

    val subject = RepositoryService(seriesRepository, seasonRepository, genreRepository)

    @Test
    fun `When it receives a series to save, converts it to DAO and saves it`() {
        val imdbId = generateImdbId()
        val series = generateSeries(imdbId = imdbId)
        val seriesDAOslot = slot<SeriesDAO>()

        val expectedDAO = SeriesDAO.fromApplicationModel(series).apply {
            series.genres.forEach {
                genre -> this.addGenre(generateGenreDAO(genre = genre))
            }
        }

        every { genreRepository.findByGenre(any()) } returns null
        every { seriesRepository.save(capture(seriesDAOslot)) } returnsArgument 0

        subject.saveSeries(series)

        verify(exactly = series.genres.size) { genreRepository.findByGenre(any()) }
        verify(exactly = 1) { seriesRepository.save(any()) }
        assertEquals(expectedDAO, seriesDAOslot.captured)
    }

    @Test
    fun `If genres already exist in database, reuses them`() {
        val imdbId = generateImdbId()
        val series = generateSeries(imdbId = imdbId)
        val numberGenres = series.genres.size
        val seriesDAOSlot = slot<SeriesDAO>()

        val databaseGenres = (0 until numberGenres).map {
            val genreDAO = generateGenreDAO(id = it.toLong(), genre = series.genres[it])
            every { genreRepository.findByGenre(series.genres[it]) } returns genreDAO
            genreDAO
        }

        every { seriesRepository.save(capture(seriesDAOSlot)) } returnsArgument 0

        subject.saveSeries(series)

        series.genres.forEach {
            verify(exactly = 1) { genreRepository.findByGenre(it) }
        }
        assertEquals(databaseGenres, seriesDAOSlot.captured.genres)
    }
}