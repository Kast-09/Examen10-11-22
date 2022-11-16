package com.example.examen10_11_22.services;

import com.example.examen10_11_22.entities.Pelicula;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PeliculaService {
    @GET("peliculas")
    Call<List<Pelicula>> get();

    @POST("peliculas")
    Call<Pelicula> create(@Body Pelicula pelicula);
}
