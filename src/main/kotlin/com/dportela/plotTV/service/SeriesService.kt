package com.dportela.plotTV.service

import com.dportela.plotTV.gateway.ScrapperGateway
import com.dportela.plotTV.model.SearchByNameCacheEntry
import com.dportela.plotTV.model.Series
import com.dportela.plotTV.repository.SearchByNameCache
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.slf4j.MDCContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SeriesService(
    val repositoryService: RepositoryService,
    val searchByNameCache: SearchByNameCache,
    val scrapperGateway: ScrapperGateway
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    fun getSeriesById(imdbId: String): Series? {
        logger.info("Getting series $imdbId")
        val seriesDAO = repositoryService.findSeriesByImdbId(imdbId)

        return seriesDAO ?: let{
            logger.info("Series $imdbId does not exist. Going to fetch it.")
            scrapperGateway.fetchSeriesById(imdbId)?.also { repositoryService.saveSeries(it) }
        }
    }

    fun getSeriesByName(name: String): Series? {
        logger.info("Getting series $name")

        return repositoryService.findSeriesByName(name) ?: let {
            val cacheEntry = searchByNameCache.get(name)
            cacheEntry?.run { repositoryService.findSeriesByImdbId(this.imdbId) }
        } ?: let {
            logger.info("Could not find series with requested name $name. Going to fetch it.")
            scrapperGateway.fetchSeriesByName(name)?.also {
                runBlocking(MDCContext()) {
                    if (!repositoryService.existsByImdbId(it.imdbId)) {
                        logger.info("Series does not exist on database. Going to save it.")
                        repositoryService.saveSeries(it)
                    }

                    searchByNameCache.save(name, SearchByNameCacheEntry(imdbId = it.imdbId, title = it.name))
                }
            }
        }
    }

    fun autoComplete(searchInput: String) : List<String> = searchByNameCache.getTitlesByPatter(searchInput) ?: emptyList()
}