package pl.skomunikacja.synclyapp.helpers

import pl.skomunikacja.synclyapp.model.Tag
import pl.skomunikacja.synclyapp.service.ApiTagsService

class ApiTagsHelper(
    private val api: ApiTagsService
) {

    suspend fun getAllTags(): List<Tag> =
        api.getAllTags()

    suspend fun searchTags(query: String): List<Tag> =
        api.searchTags(query)
}