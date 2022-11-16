package com.example.examen10_11_22;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.examen10_11_22.entities.Imagen;
import com.example.examen10_11_22.entities.ImagenBase64;
import com.example.examen10_11_22.entities.Pelicula;
import com.example.examen10_11_22.factories.RetrofitFactory;
import com.example.examen10_11_22.services.ImagenBase64Service;
import com.example.examen10_11_22.services.PeliculaService;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearPeliculasActivity extends AppCompatActivity {

    private EditText etTitulo, etSinopsis;

    private Button btnTakePhoto;
    public ImageView ivImagen;

    public String encoded = "";

    public Imagen data;

    private final static int CAMERA_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_peliculas);

        etTitulo = findViewById(R.id.etTitulo);
        etSinopsis = findViewById(R.id.etSinopsis);

        btnTakePhoto = findViewById(R.id.btnTakePhoto);

        ivImagen = findViewById(R.id.ivImagen2);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")// -> Aquí va la URL sin el Path
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    abrirCamara();
                } //pedir permisos
                else{
                    requestPermissions(new String[] {Manifest.permission.CAMERA}, 100);//un número cualquiera
                }
            }
        });
    }

    public void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//con esto es lo mínimo necesario para abrir la cámara
        startActivityForResult(intent, CAMERA_REQUEST);//se le pone cualquier número, sirve como código de respeusta
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){// el CAMERA_REQUEST es para validar que sea una petición de abrir la cámara y el RESULT_OK es para validar que al abrir la cámara todo salio bien y no hubo errores
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivImagen.setImageBitmap(imageBitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            //Log.i("MAIN_APP", encoded);
        }
    }

    public void crear(View view){

        String titulo = etTitulo.getText().toString();
        String sinopsis = etSinopsis.getText().toString();

        Pelicula pelicula = new Pelicula();

        pelicula.titulo = titulo;
        pelicula.sinopsis = sinopsis;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImagenBase64 imagenBase64 = new ImagenBase64();
        imagenBase64.imagen = encoded;

        ImagenBase64Service service = retrofit.create(ImagenBase64Service.class);
        service.obtener(imagenBase64, "Client-ID 8bcc638875f89d9").enqueue(new Callback<Imagen>() {
            @Override
            public void onResponse(Call<Imagen> call, retrofit2.Response<Imagen> response) {
                data = response.body();
                if(response.isSuccessful()){
                    Log.i("MAIN_APP", "Llegue");
                    Log.i("MAIN_APP", new Gson().toJson(data));
                }
                else{
                    Log.i("MAIN_APP", response.message());
                }
                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl("https://6352ca44a9f3f34c3749009a.mockapi.io/")// -> Aquí va la URL sin el Path
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                pelicula.imagen = data.data.link;

                PeliculaService service1 = retrofit2.create(PeliculaService.class);
                service1.create(pelicula).enqueue(new Callback<Pelicula>() {
                    @Override
                    public void onResponse(Call<Pelicula> call, Response<Pelicula> response) {
                        Log.i("MAIN_APP", "Responde: "+response.code());
                    }

                    @Override
                    public void onFailure(Call<Pelicula> call, Throwable t) {
                        Log.i("MAIN_APP", "No se guardo la película");
                    }
                });
                Log.i("MAIN_APP", "Guardado exitosamente");
            }

            @Override
            public void onFailure(Call<Imagen> call, Throwable t) {
                Log.i("MAIN_APP", "Fallo a obtener datos");
            }
        });


    }
}