package com.dportela.plotTV.model

data class Season (
    val number: Int,
    val numberEpisodes: Int,
    val episodes: Set<Episode>
)