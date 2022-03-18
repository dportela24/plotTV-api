package com.dportela.plotTV.controller

import com.dportela.plotTV.model.Season
import com.dportela.plotTV.model.Series
import com.dportela.plotTV.service.SeriesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/series")
class SeriesController(
    val seriesService: SeriesService
) {
    @GetMapping("/{imdbId}")
    fun getSeries(@PathVariable imdbId: String) : ResponseEntity<Series>? {
        val series = seriesService.getSeries(imdbId)
        return series?.let{ ResponseEntity.ok(it) }
    }
}