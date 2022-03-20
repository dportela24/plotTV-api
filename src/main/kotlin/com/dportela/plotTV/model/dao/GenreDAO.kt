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
) {
    override fun toString(): String {
        return "GenreDAO(id=$id, genre='$genre')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenreDAO

        if (id != other.id) return false
        if (genre != other.genre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + genre.hashCode()
        return result
    }


}