package com.example.quevvse150165_lab10.api;

import com.example.quevvse150165_lab10.Model.Trainee;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TraineeService {
    String TRAINEES = "Hocvien";

    @GET(TRAINEES)
    Call<Trainee[]> getAllTrainees();

    @GET(TRAINEES + "/{id}")
    Call<Trainee> getAllTrainees(@Path("id") Object id);

    @POST(TRAINEES)
    Call<Trainee> createTrainees(@Body Trainee trainee);

    @PUT(TRAINEES + "/{id}")
    Call<Trainee> updateTrainees(@Path("id") Object id, @Body Trainee trainee);

    @DELETE(TRAINEES + "/{id}")
    Call<Trainee> deleteTrainees(@Path("id") Object id);

}
