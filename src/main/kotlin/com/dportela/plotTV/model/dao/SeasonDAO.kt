package com.dportela.plotTV.model.dao

import com.dportela.plotTV.model.Episode
import com.dportela.plotTV.model.Season
import com.dportela.plotTV.model.Series
import javax.persistence.*

@Entity
@Table(name = "season")
data class SeasonDAO (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_key", nullable = false)
    var series: SeriesDAO? = null,

    @OneToMany(
        mappedBy = "season",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val episodes: MutableList<EpisodeDAO> = mutableListOf(),

    val number: Int,
    val numberEpisodes: Int
) {
    fun addEpisode(episodeDAO: EpisodeDAO) {
        episodes.add(episodeDAO)
        episodeDAO.season = this
    }

    fun toApplicationModel() = Season(
        number = number,
        numberEpisodes = numberEpisodes,
        episodes = episodes.map { it.toApplicationModel() }
    )

    companion object{
        fun fromApplicationModel(season: Season) = SeasonDAO(
            number = season.number,
            numberEpisodes = season.numberEpisodes
        ).apply {
            season.episodes.forEach{ episode ->
                val episodeDAO = EpisodeDAO.fromApplicationModel(episode)
                this.addEpisode(episodeDAO)
            }
        }
    }
}