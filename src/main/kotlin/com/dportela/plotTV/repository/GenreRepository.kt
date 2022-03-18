package com.dportela.plotTV.repository

import com.dportela.plotTV.model.dao.GenreDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GenreRepository : JpaRepository<GenreDAO, Long> {
    fun findByGenre(genre: String) : GenreDAO?
}