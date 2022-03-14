package com.dportela.plotTV.repository

import com.dportela.plotTV.model.dao.EpisodeDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EpisodeRepository : JpaRepository<EpisodeDAO, Long> {}