package com.dportela.plotTV.repository

import com.dportela.plotTV.model.dao.SeasonDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeasonRepository : JpaRepository<SeasonDAO, Long>