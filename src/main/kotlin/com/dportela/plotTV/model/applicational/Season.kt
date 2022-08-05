package com.dportela.plotTV.model.applicational

data class Season (
    val number: Int,
    val numberEpisodes: Int,
    val episodes: List<Episode>,
)