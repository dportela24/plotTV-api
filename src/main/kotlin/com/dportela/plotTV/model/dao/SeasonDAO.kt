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

    val number: Int,
    val numberEpisodes: Int
    //val episodes: Set<Episode>
) {
    fun toApplicationModel() = Season(
        number = number,
        numberEpisodes = numberEpisodes,
        //episodes = emptySet()
    )

    companion object{
        fun fromApplicationModel(season: Season) = SeasonDAO(
            number = season.number,
            numberEpisodes = season.numberEpisodes
        )
    }
}