package com.dportela.plotTV.helper

import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

fun getParseDateFunctions() : Set<(String) -> TemporalAccessor> = setOf(
    { input -> LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-d")) },
    { input -> YearMonth.parse(input, DateTimeFormatter.ofPattern("yyyy-MM")) },
    { input -> Year.parse(input, DateTimeFormatter.ofPattern("yyyy")) }
)

fun validateImdbId(imdbId: String) = Regex("^tt\\d{7,8}\$").matches(imdbId)