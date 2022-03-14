package com.dportela.plotTV.model.dao

import com.dportela.plotTV.model.Episode
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "season")
data class SeasonDAO (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val number: Int,
    val numberEpisodes: Int,
    //val episodes: Set<Episode>
)