package cl.tuapp.dogapp.model

import com.google.gson.annotations.SerializedName

data class DogImageResponse(
    @SerializedName("message")
    val imageUrl: String,
    @SerializedName("status")
    val status: String
)