package com.dportela.plotTV.model.exception

class InvalidImdbIdException(
    val imdbId: String
) : RuntimeException("Requested $imdbId is not a valid IMDb id")