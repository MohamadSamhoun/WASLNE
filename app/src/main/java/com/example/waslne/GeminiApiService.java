package com.example.waslne;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
public interface GeminiApiService {
    @Headers("Content-Type: application/json")
    @POST("v1beta/models/gemini-2.0-flash:generateContent") // Use the full relative path
    Call<GeminiResponse> sendMessageToGemini(
            @Body GeminiRequest request,
            @Query("key") String apiKey  // Correct the query parameter name to "key"
    );
}