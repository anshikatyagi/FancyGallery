package edu.vt.cs5254.fancygallery.api

import edu.vt.cs5254.fancygallery.FlickrResponse
import retrofit2.http.GET

private const val API_KEY = "3ae26287096be65d294cfb132ff14e20"

interface FlickrApi {
    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=$API_KEY" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    suspend fun fetchPhotos(): FlickrResponse
}