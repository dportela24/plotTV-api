package com.dportela.plotTV.repository

import com.dportela.plotTV.model.dao.SeriesDAO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeriesRepository : JpaRepository<SeriesDAO, Long> {}