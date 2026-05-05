package com.example.signsync.viewmodel

import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signsync.ApiClient
import com.example.signsync.PredictRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class DetectionViewModel : ViewModel() {

    // ── Detected sign letter or word ──────────────────────────
    private val _sign = MutableStateFlow<String?>(null)
    val sign: StateFlow<String?> = _sign.asStateFlow()

    // ── Confidence 0.0 to 1.0 ────────────────────────────────
    private val _confidence = MutableStateFlow(0.0)
    val confidence: StateFlow<Double> = _confidence.asStateFlow()

    // ── Sentence built from signs ─────────────────────────────
    private val _sentence = MutableStateFlow("")
    val sentence: StateFlow<String> = _sentence.asStateFlow()

    // ── Loading while waiting for server ─────────────────────
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ── Error message ─────────────────────────────────────────
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // ── Haptic trigger — increments every sign detection ──────
    // LaunchedEffect watches this — fires vibration every time
    private val _hapticTrigger = MutableStateFlow(0)
    val hapticTrigger: StateFlow<Int> = _hapticTrigger.asStateFlow()

    fun predict(bitmap: Bitmap) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Encode on background thread — CPU intensive
                val base64 = withContext(Dispatchers.Default) {
                    bitmapToBase64(bitmap)
                }

                // Network call on IO thread
                val response = withContext(Dispatchers.IO) {
                    ApiClient.api.predict(
                        PredictRequest("data:image/jpeg;base64,$base64")
                    )
                }

                _sign.value = response.sign
                _confidence.value = response.confidence

                // ── Trigger haptic every time a sign is detected
                if (response.sign != null) {
                    _hapticTrigger.value += 1
                }

                response.sign?.let { buildSentence(it) }

            } catch (e: Exception) {
                _sign.value = null
                _error.value = when {
                    e.message?.contains("refused") == true ->
                        "Cannot connect. Is python app.py running?"
                    e.message?.contains("timeout") == true ->
                        "Timeout. Check your Wi-Fi connection."
                    e.message?.contains("failed to connect") == true ->
                        "Wrong IP address in ApiClient.kt"
                    else ->
                        "Error: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun buildSentence(sign: String) {
        _sentence.value += when {
            sign == "Space"  -> " "
            sign.length == 1 -> sign
            else             -> "$sign "
        }
    }

    fun clearSentence() {
        _sentence.value = ""
    }

    fun clearError() {
        _error.value = null
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
    }
}