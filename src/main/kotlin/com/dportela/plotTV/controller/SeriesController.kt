package com.dportela.plotTV.controller

import com.dportela.plotTV.model.Series
import com.dportela.plotTV.service.SeriesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/series")
class SeriesController(
    val seriesService: SeriesService
) {
    //@GetMapping("/id/{imdbId}")
    //fun getSeriesByImdbId(@PathVariable imdbId: String) : ResponseEntity<Series> {
    //    seriesService.
    //}

    @GetMapping("/create")
    fun createSeries() : ResponseEntity<String> {
        seriesService.createSeries()
        return ResponseEntity.ok("Series Created")
    }
}