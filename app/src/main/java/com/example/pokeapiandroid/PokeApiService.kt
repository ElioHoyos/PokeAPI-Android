package com.example.pokeapiandroid

import retrofit2.http.GET

interface PokeApiService {
    @GET("pokemon/ditto")
    suspend fun getDitto(): PokemonResponse

    companion object{
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }

}