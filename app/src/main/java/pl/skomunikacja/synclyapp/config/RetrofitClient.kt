package pl.skomunikacja.synclyapp.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.service.ApiCommentsService
import pl.skomunikacja.synclyapp.service.ApiFollowsService
import pl.skomunikacja.synclyapp.service.ApiFriendsService
import pl.skomunikacja.synclyapp.service.ApiLikesService
import pl.skomunikacja.synclyapp.service.ApiPostCollectionsService
import pl.skomunikacja.synclyapp.service.ApiPostsService
import pl.skomunikacja.synclyapp.service.ApiReportService
import pl.skomunikacja.synclyapp.service.ApiTagsService
import pl.skomunikacja.synclyapp.service.ApiUsersService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Post::class.java, PostDeserializer())
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://ns31075468.ip-51-77-53.eu:8443/syncly/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()

    val apiUsersService: ApiUsersService = retrofit.create(ApiUsersService::class.java)
    val apiPostsService: ApiPostsService = retrofit.create(ApiPostsService::class.java)
    val apiFollowsService: ApiFollowsService = retrofit.create(ApiFollowsService::class.java)
    val apiLikesService: ApiLikesService = retrofit.create(ApiLikesService::class.java)
    val apiTagsService: ApiTagsService = retrofit.create(ApiTagsService::class.java)
    val apiCommentsService: ApiCommentsService = retrofit.create(ApiCommentsService::class.java)
    val apiPostCollectionsService: ApiPostCollectionsService = retrofit.create(ApiPostCollectionsService::class.java)
    val apiFriendsService: ApiFriendsService = retrofit.create(ApiFriendsService::class.java)
    val apiReportsService: ApiReportService = retrofit.create(ApiReportService::class.java)
}