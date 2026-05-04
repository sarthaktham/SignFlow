package com.example.signsync

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// ── Request sent to Flask server ─────────────────────────────
data class PredictRequest(
    val image: String       // base64 encoded JPEG image
)

// ── Response received from Flask server ──────────────────────
data class PredictResponse(
    val sign: String?,      // detected sign or null if not confident
    val confidence: Double, // probability 0.0 to 1.0
    val error: String?      // error message or null
)

// ── Server status response ────────────────────────────────────
data class StatusResponse(
    val model_loaded: Boolean,
    val two_hands: Boolean
)

// ── API interface — Retrofit generates implementation ─────────
interface SignLanguageApi {

    @POST("api/predict")
    suspend fun predict(@Body request: PredictRequest): PredictResponse

    @GET("api/status")
    suspend fun getStatus(): StatusResponse
}