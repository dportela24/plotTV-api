package com.dportela.plotTV.model.dao

import com.dportela.plotTV.model.applicational.Series
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
        genres = genres.map { it.genre },
        ratingValue = ratingValue,
        ratingCount = ratingCount,
        posterURL = posterURL,
        numberSeasons = numberSeasons,
        seasons = seasons.map { it.toApplicationModel() },
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SeriesDAO

        if (id != other.id) return false
        if (imdbId != other.imdbId) return false
        if (genres != other.genres) return false
        if (seasons != other.seasons) return false
        if (summary != other.summary) return false
        if (name != other.name) return false
        if (originalName != other.originalName) return false
        if (episodeDuration != other.episodeDuration) return false
        if (startYear != other.startYear) return false
        if (endYear != other.endYear) return false
        if (ratingValue != other.ratingValue) return false
        if (ratingCount != other.ratingCount) return false
        if (posterURL != other.posterURL) return false
        if (numberSeasons != other.numberSeasons) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + imdbId.hashCode()
        result = 31 * result + genres.hashCode()
        result = 31 * result + seasons.hashCode()
        result = 31 * result + (summary?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + (originalName?.hashCode() ?: 0)
        result = 31 * result + (episodeDuration?.hashCode() ?: 0)
        result = 31 * result + startYear
        result = 31 * result + (endYear ?: 0)
        result = 31 * result + (ratingValue?.hashCode() ?: 0)
        result = 31 * result + (ratingCount ?: 0)
        result = 31 * result + (posterURL?.hashCode() ?: 0)
        result = 31 * result + numberSeasons
        return result
    }


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