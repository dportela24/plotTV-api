package com.dportela.plotTV.model.dao

import com.dportela.plotTV.model.Season
import java.time.Duration
import java.time.Instant
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "series")
data class SeriesDAO (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    val imdbId: String,
    val name: String,
    val originalName: String?,
    val summary: String?,
    val episodeDuration: Duration?,
    val startYear: Int,
    val endYear: Int?,
    @ManyToMany(cascade = [CascadeType.PERSIST])
    @JoinTable(
        name = "series_genre",
        joinColumns = [JoinColumn(name = "series_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: MutableList<GenreDAO> = mutableListOf(),
    val ratingValue: Float?,
    val ratingCount: Int?,
    val posterURL: String?,
    val numberSeasons: Int,
    //val seasons: Set<Season>
    val updatedAt: Instant
)