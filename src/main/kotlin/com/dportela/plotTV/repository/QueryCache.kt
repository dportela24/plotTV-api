package com.dportela.plotTV.repository

import com.dportela.plotTV.model.dao.TitleMatchingCacheDAO
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class QueryCache(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val keyPrefix = "SearchByNameCache:"

    fun save(searchInput: String, imdbdId: String) =
        redisTemplate.opsForValue().set(
            getKey(searchInput),
            imdbdId,
        )

    fun get(searchInput: String) =
        redisTemplate.opsForValue().get(getKey(searchInput))

    fun getKey(searchInput: String) = keyPrefix + searchInput
}