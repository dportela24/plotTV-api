package com.dportela.plotTV.model.dao

import com.dportela.plotTV.model.Season
import com.dportela.plotTV.model.Series
import java.time.Duration
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "series")
data class SeriesDAO (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val imdbId: String,

    @ManyToMany(cascade = [CascadeType.PERSIST])
    @JoinTable(
        name = "series_genre",
        joinColumns = [JoinColumn(name = "series_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: MutableList<GenreDAO> = mutableListOf(),

    @OneToMany(
        mappedBy = "series",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val seasons: MutableList<SeasonDAO> = mutableListOf(),

    @Column(length = 5000)
    val summary: String?,


    val name: String,
    val originalName: String?,
    val episodeDuration: Duration?,
    val startYear: Int,
    val endYear: Int?,
    val ratingValue: Float?,
    val ratingCount: Int?,
    val posterURL: String?,
    val numberSeasons: Int,
    val updatedAt: Instant
) {
    fun addSeason(seasonDAO: SeasonDAO) {
        seasons.add(seasonDAO)
        seasonDAO.series = this
    }

    fun addGenre(genreDAO: GenreDAO) {
        genres.add(genreDAO)
        genreDAO.series.add(this)
    }

    fun toApplicationModel() = Series(
        imdbId = imdbId,
        name = name,
        originalName = originalName,
        summary = summary,
        episodeDuration = episodeDuration,
        startYear = startYear,
        endYear = endYear,
        genres = genres.map { it.genre }.toSet(),
        ratingValue = ratingValue,
        ratingCount = ratingCount,
        posterURL = posterURL,
        numberSeasons = numberSeasons,
        seasons = seasons.map { it.toApplicationModel() },
    )

    companion object {
        fun fromApplicationModel(series: Series) = SeriesDAO(
            imdbId = series.imdbId,
            name = series.name,
            originalName = series.originalName,
            summary = series.summary,
            episodeDuration = series.episodeDuration,
            startYear = series.startYear,
            endYear = series.endYear,
            ratingValue = series.ratingValue,
            ratingCount = series.ratingCount,
            posterURL = series.posterURL,
            numberSeasons = series.numberSeasons,
            updatedAt = Instant.now(),
        ).apply {
            series.seasons.forEach{ season ->
                val seasonDAO = SeasonDAO.fromApplicationModel(season)
                this.addSeason(seasonDAO)
            }
        }
    }
}