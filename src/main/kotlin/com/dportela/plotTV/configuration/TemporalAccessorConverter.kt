package com.dportela.plotTV.configuration

import com.dportela.plotTV.helper.getParseDateFunctions
import com.dportela.plotTV.model.exception.ParsingErrorException
import java.time.temporal.TemporalAccessor
import javax.persistence.AttributeConverter

class TemporalAccessorConverter : AttributeConverter<TemporalAccessor, String> {
    override fun convertToDatabaseColumn(attribute: TemporalAccessor?): String? {
       return attribute?.toString()
    }

    override fun convertToEntityAttribute(dbData: String?): TemporalAccessor? {
        val input = dbData ?: return null

        getParseDateFunctions().forEach { fn ->
            try {
                return fn(input)
            }catch (_: Exception) {}
        }

        throw ParsingErrorException("Could not parse database episode airdate. Input - $input")
    }
}