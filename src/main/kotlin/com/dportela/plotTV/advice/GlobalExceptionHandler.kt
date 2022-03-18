package com.dportela.plotTV.advice

import com.dportela.plotTV.model.ErrorDetails
import com.dportela.plotTV.model.exception.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(value = [ConnectionErrorException::class,
        ScrappingUnavailableException::class,
        ParsingErrorException::class
    ])
    fun handleConnectionError(req: HttpServletRequest, ex: RuntimeException) : ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            ErrorDetails.ErrorCode.SCRAPPER_ERROR,
            "Scrapping error",
            "Could not connect to scrapper"
        )

        return ResponseEntity(errorDetails, HttpStatus.BAD_GATEWAY)
    }

    @ExceptionHandler(value = [NotATvSeriesException::class])
    fun handleNotATvSeries(req: HttpServletRequest, ex: NotATvSeriesException) : ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            ErrorDetails.ErrorCode.NOT_A_TV_SERIES_ERROR,
            "Provided title is not a TV series",
            ex.message
        )

        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [InvalidImdbIdException::class])
    fun handleInvalidImdbId(req: HttpServletRequest, ex: InvalidImdbIdException) : ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            ErrorDetails.ErrorCode.INVALID_IMDB_ID,
            "Invalid IMDb Id",
            ex.message
        )

        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleUnknownError(req: HttpServletRequest,
                           ex: RuntimeException) : ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            ErrorDetails.ErrorCode.UNEXPECTED_ERROR,
            "Unexpected Error",
            "An unexpected error occurred processing the request..."
        )

        return ResponseEntity(errorDetails, HttpStatus.SERVICE_UNAVAILABLE)
    }
}