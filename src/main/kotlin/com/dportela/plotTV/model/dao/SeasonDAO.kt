package com.dportela.plotTV.model.dao

import com.dportela.plotTV.model.applicational.Season
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
    var numberEpisodes: Int = 0
) {
    fun addEpisode(episodeDAO: EpisodeDAO) {
        episodes.add(episodeDAO)
        numberEpisodes++
        episodeDAO.season = this
    }

    fun toApplicationModel() = Season(
        number = number,
        numberEpisodes = numberEpisodes,
        episodes = episodes.map { it.toApplicationModel() }
    )

    override fun toString(): String {
        return "SeasonDAO(id=$id, episodes=$episodes, number=$number, numberEpisodes=$numberEpisodes)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SeasonDAO

        if (id != other.id) return false
        if (episodes != other.episodes) return false
        if (number != other.number) return false
        if (numberEpisodes != other.numberEpisodes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + episodes.hashCode()
        result = 31 * result + number
        result = 31 * result + numberEpisodes
        return result
    }

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