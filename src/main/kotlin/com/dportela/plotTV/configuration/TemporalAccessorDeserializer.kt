package com.dportela.plotTV.configuration

import com.dportela.plotTV.helper.getParseDateFunctions
import com.dportela.plotTV.model.exception.ParsingErrorException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor


class TemporalAccessorDeserializer : JsonDeserializer<TemporalAccessor>() {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): TemporalAccessor? {
        val airdate = jp.text ?: return null

        getParseDateFunctions().forEach { fn ->
            try {
                return fn(airdate)
            }catch (_: Exception) {}
        }

        throw ParsingErrorException("Could not parse scrapper's episode airdate. Input - $airdate")
    }
}