package com.example.cachecheck.data.remote

import com.example.cachecheck.data.remote.dto.PostResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url

class PostsServiceImpl(
    private val client: HttpClient
) : PostsService {
    override suspend fun getPosts(): List<PostResponse> {
        return try {
            client.get { url(HttpRoutes.POSTS) }
        } catch (e: RedirectResponseException) {
            emptyList()
        } catch (e: ClientRequestException) {
            emptyList()
        }
    }
}