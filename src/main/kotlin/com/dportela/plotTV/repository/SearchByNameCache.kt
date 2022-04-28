package com.dportela.plotTV.repository

import com.dportela.plotTV.model.SearchByNameCacheEntry
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class SearchByNameCache(
    private val redisTemplate: RedisTemplate<String, SearchByNameCacheEntry>
) {
    private val keyPrefix = "SearchByNameCache:"

    fun save(searchInput: String, result: SearchByNameCacheEntry) =
        redisTemplate.opsForValue().set(
            getKey(searchInput),
            result,

        )

    fun get(searchInput: String) =
        redisTemplate.opsForValue().get(getKey(searchInput))

    fun getTitlesByPatter(searchInput: String) : List<String>? {
        val keys = redisTemplate.keys(getKeyPatern(searchInput))
        val values = redisTemplate.opsForValue().multiGet(keys)

        return values?.map { it.title }?.distinct()
    }

    fun getKey(searchInput: String) = keyPrefix + searchInput

    fun getKeyPatern(searchInput: String) = "$keyPrefix$searchInput*"
}