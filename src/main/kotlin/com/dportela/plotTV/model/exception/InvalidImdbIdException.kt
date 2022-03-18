package com.dportela.plotTV.model.exception

class InvalidImdbIdException(
    override val message: String
) : RuntimeException(message)