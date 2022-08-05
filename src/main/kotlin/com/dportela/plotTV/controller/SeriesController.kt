package com.dportela.plotTV.controller

import com.dportela.plotTV.helper.validateImdbId
import com.dportela.plotTV.model.applicational.Series
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

    @GetMapping
    fun getSeries(@RequestParam(name = "q") query: String) : ResponseEntity<Series> {
        MDC.put("request", UUID.randomUUID().toString())
        MDC.put("requestQuery", query)

        return ResponseEntity.ok(seriesService.getSeries(query))
    }
}