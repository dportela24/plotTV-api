package com.dportela.plotTV.model.dao

import java.time.temporal.TemporalAccessor
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "episode")
data class EpisodeDAO (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val imdbId: String,
    val number: Int,
    val name: String,
    //val airdate: TemporalAccessor?,
    val ratingValue: Float?,
    val ratingCount: Int?,
    val summary: String?
)