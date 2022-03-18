package com.dportela.plotTV.model.exception

class ParsingErrorException(
    override val message: String
) : RuntimeException(message)