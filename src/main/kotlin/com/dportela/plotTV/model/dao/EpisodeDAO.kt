package com.dportela.plotTV.model.dao

import com.dportela.plotTV.configuration.TemporalAccessorConverter
import com.dportela.plotTV.model.Episode
import com.dportela.plotTV.model.Season
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