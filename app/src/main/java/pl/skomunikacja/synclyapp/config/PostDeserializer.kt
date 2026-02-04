package pl.skomunikacja.synclyapp.config

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import pl.skomunikacja.synclyapp.model.post.LinkPost
import pl.skomunikacja.synclyapp.model.post.PhotoPost
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.model.post.PostType
import pl.skomunikacja.synclyapp.model.post.QuotePost
import pl.skomunikacja.synclyapp.model.post.TextPost
import pl.skomunikacja.synclyapp.model.post.VideoPost
import java.lang.reflect.Type

class PostDeserializer : JsonDeserializer<Post> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Post {
        val jsonObject = json.asJsonObject
        val postTypeString = jsonObject.get("postType").asString
        val postType = PostType.valueOf(postTypeString)

        return when (postType) {
            PostType.QUOTE -> context.deserialize(json, QuotePost::class.java)
            PostType.TEXT -> context.deserialize(json, TextPost::class.java)
            PostType.PHOTO -> context.deserialize(json, PhotoPost::class.java)
            PostType.VIDEO -> context.deserialize(json, VideoPost::class.java)
            PostType.LINK -> context.deserialize(json, LinkPost::class.java)
            else -> throw JsonParseException("Unknown postType: $postType")
        }
    }
}