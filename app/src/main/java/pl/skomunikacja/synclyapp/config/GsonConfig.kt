package pl.skomunikacja.synclyapp.config

import com.squareup.moshi.Moshi
import pl.skomunikacja.synclyapp.model.post.PhotoPost
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.model.post.QuotePost
import pl.skomunikacja.synclyapp.model.post.VideoPost
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import pl.skomunikacja.synclyapp.model.post.LinkPost
import pl.skomunikacja.synclyapp.model.post.TextPost

object GsonConfig {
    val moshi: Moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(Post::class.java, "postType")
                .withSubtype(QuotePost::class.java, "QUOTE")
                .withSubtype(PhotoPost::class.java, "PHOTO")
                .withSubtype(VideoPost::class.java, "VIDEO")
                .withSubtype(TextPost::class.java, "TEXT")
                .withSubtype(LinkPost::class.java, "LINK")
        )
        .add(KotlinJsonAdapterFactory())
        .build()
}