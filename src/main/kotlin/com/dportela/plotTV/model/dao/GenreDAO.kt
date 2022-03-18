package com.dportela.plotTV.model.dao

import javax.persistence.*

@Entity
@Table(name = "genre")
data class GenreDAO(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val genre: String,

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    val series: List<SeriesDAO> = mutableListOf()
)