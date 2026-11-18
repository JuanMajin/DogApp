package cl.tuapp.dogapp.data

import cl.tuapp.dogapp.model.BreedsResponse
import cl.tuapp.dogapp.model.DogImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): Response<BreedsResponse>

    @GET("breed/{breed}/images/random")
    suspend fun getRandomImageByBreed(
        @Path("breed", encoded = true) breed: String
    ): Response<DogImageResponse>

    @GET("breeds/image/random")
    suspend fun getRandomDogImage(): Response<DogImageResponse>
}