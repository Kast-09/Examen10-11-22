package com.example.examen10_11_22.services;

import com.example.examen10_11_22.entities.Imagen;
import com.example.examen10_11_22.entities.ImagenBase64;
import com.example.examen10_11_22.entities.Pelicula;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ImagenBase64Service {
    @POST("image")
    Call<Imagen> obtener(@Body ImagenBase64 imagenBase64, @Header("Authorization") String authHeader);
}
