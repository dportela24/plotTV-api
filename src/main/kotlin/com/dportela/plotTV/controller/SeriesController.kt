package com.dportela.plotTV.controller

import com.dportela.plotTV.helper.validateImdbId
import com.dportela.plotTV.model.Series
import com.dportela.plotTV.model.exception.InvalidImdbIdException
import com.dportela.plotTV.service.SeriesService
import org.slf4j.MDC
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/series")
class SeriesController(
    val seriesService: SeriesService
) {
    @GetMapping("/id/{imdbId}")
    fun getSeriesById(@PathVariable imdbId: String) : ResponseEntity<Series>? {
        if (!validateImdbId(imdbId)) throw InvalidImdbIdException(imdbId)
        MDC.put("request", UUID.randomUUID().toString())
        MDC.put("imdbId", imdbId)

        val series = seriesService.getSeriesById(imdbId)
        
        return series?.let{ ResponseEntity.ok(it) }
    }

    @GetMapping("/name/{requestName}")
    fun getSeriesByName(@PathVariable requestName: String) : ResponseEntity<Series>? {
        val name = requestName.replace("+", " ")
        MDC.put("request", UUID.randomUUID().toString())
        MDC.put("request_name", name)

        val series = seriesService.getSeriesByName(name)

        return series?.let{ ResponseEntity.ok(it) }
    }

    @GetMapping("/autocomplete")
    fun getAutocomplete(@RequestParam(name = "q") searchInput: String) : ResponseEntity<List<String>> {
        return ResponseEntity.ok(seriesService.autoComplete(searchInput))
    }
}