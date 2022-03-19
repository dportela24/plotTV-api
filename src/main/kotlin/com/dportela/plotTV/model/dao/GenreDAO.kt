package com.dportela.plotTV.model.dao

import javax.persistence.*

@Entity
@Table(name = "genre")
data class GenreDAO(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val genre: String,

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    val series: MutableList<SeriesDAO> = mutableListOf()
)