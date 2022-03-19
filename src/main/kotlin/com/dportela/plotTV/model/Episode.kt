package com.dportela.plotTV.model

import com.dportela.plotTV.configuration.TemporalAccessorDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.temporal.TemporalAccessor

data class Episode (
    val imdbId: String,
    val number: Int,
    val name: String,
    @JsonDeserialize(using = TemporalAccessorDeserializer::class)
    val airdate: TemporalAccessor?,
    val ratingValue: Float?,
    val ratingCount: Int?,
    val summary: String?
)