package com.dportela.plotTV.model.dao

import com.dportela.plotTV.configuration.TemporalAccessorConverter
import com.dportela.plotTV.model.applicational.Episode
import java.time.temporal.TemporalAccessor
import javax.persistence.*

@Entity
@Table(name = "episode")
data class EpisodeDAO (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val imdbId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_key", nullable = false)
    var season: SeasonDAO? = null,

    @Column(length = 5000)
    val summary: String?,

    @Convert(converter = TemporalAccessorConverter::class)
    val airdate: TemporalAccessor? = null,

    val number: Int,
    val name: String,
    val ratingValue: Float?,
    val ratingCount: Int?,
) {
    fun toApplicationModel() = Episode(
        imdbId = imdbId,
        number = number,
        name = name,
        ratingValue = ratingValue,
        ratingCount = ratingCount,
        summary = summary,
        airdate = airdate
    )

    override fun toString(): String {
        return "EpisodeDAO(id=$id, imdbId='$imdbId', summary=$summary, airdate=$airdate, number=$number, name='$name', ratingValue=$ratingValue, ratingCount=$ratingCount)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EpisodeDAO

        if (id != other.id) return false
        if (imdbId != other.imdbId) return false
        if (summary != other.summary) return false
        if (airdate != other.airdate) return false
        if (number != other.number) return false
        if (name != other.name) return false
        if (ratingValue != other.ratingValue) return false
        if (ratingCount != other.ratingCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + imdbId.hashCode()
        result = 31 * result + (summary?.hashCode() ?: 0)
        result = 31 * result + (airdate?.hashCode() ?: 0)
        result = 31 * result + number
        result = 31 * result + name.hashCode()
        result = 31 * result + (ratingValue?.hashCode() ?: 0)
        result = 31 * result + (ratingCount ?: 0)
        return result
    }

    companion object{
        fun fromApplicationModel(episode: Episode) = EpisodeDAO(
            imdbId = episode.imdbId,
            number = episode.number,
            name = episode.name,
            ratingValue = episode.ratingValue,
            ratingCount = episode.ratingCount,
            summary = episode.summary,
            airdate = episode.airdate
        )
    }
}