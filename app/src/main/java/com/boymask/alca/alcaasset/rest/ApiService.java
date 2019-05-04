package com.boymask.alca.alcaasset.rest;

import com.boymask.alca.alcaasset.rest.beans.Check;
import com.boymask.alca.alcaasset.rest.beans.Checklist;
import com.boymask.alca.alcaasset.rest.beans.ChecklistIntervento;
import com.boymask.alca.alcaasset.rest.beans.ChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;
import com.boymask.alca.alcaasset.rest.beans.Utente;

import java.util.List;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("login/{user}/{password}")
    Single<Utente> login(@Path("user") String user, @Path("password") String password,
                         @Query("login") String apiKey);

    @GET("checklist/{assetId}")
    Single<ChecklistRestBean> getChecklist(@Path("assetId") String assetId, @Query("checklist") String apiKey);

    @GET("intervento/{interventoId}")
    Single<InterventoRestBean> getIntervento(@Path("interventoId") long interventoId, @Query("interventi") String apiKey);

    @GET("checklist/checksForIntervento/{interventoId}")
    Single<List<ChecklistIntervento>> getChecksForIntervento(@Path("interventoId") long interventoId, @Query("interventi") String apiKey);

    @GET("intervento/getnext/{rfid}")
    Single<InterventoRestBean> getNextIntervento(@Path("rfid") String rfid, @Query("interventi") String apiKey);

    @Headers("Content-Type: application/json")
    @POST("intervento/updateIntervento")
    Call<InterventoRestBean> updateIntervento(@Body InterventoRestBean bean);

    @POST("intervento/creaIntervento")
    Call<InterventoRestBean> creaIntervento(@Body InterventoRestBean bean);

    @Multipart
    @POST("upload/uploadAttachment")
    Call<Utente> uploadFile(@Part MultipartBody.Part filePart,@Query("id") long id);

    @Multipart
    @POST("upload/uploadAudio")
    Call<Utente> uploadAudio(@Part MultipartBody.Part filePart,@Query("id") long id);
}
