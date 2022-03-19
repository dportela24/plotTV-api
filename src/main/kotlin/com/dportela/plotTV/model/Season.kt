package com.dportela.plotTV.model

import com.dportela.plotTV.model.dao.SeasonDAO
import com.dportela.plotTV.model.dao.SeriesDAO

data class Season (
    val number: Int,
    val numberEpisodes: Int,
    val episodes: List<Episode>,
)