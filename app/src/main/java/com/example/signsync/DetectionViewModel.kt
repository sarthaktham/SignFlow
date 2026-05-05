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

    private val _sign = MutableStateFlow<String?>(null)
    val sign: StateFlow<String?> = _sign.asStateFlow()

    private val _confidence = MutableStateFlow(0.0)
    val confidence: StateFlow<Double> = _confidence.asStateFlow()

    private val _sentence = MutableStateFlow("")
    val sentence: StateFlow<String> = _sentence.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _hapticTrigger = MutableStateFlow(0)
    val hapticTrigger: StateFlow<Int> = _hapticTrigger.asStateFlow()

    private var lastSign: String? = null
    private var lastHapticTime = 0L

    fun predict(bitmap: Bitmap) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val base64 = withContext(Dispatchers.Default) {
                    bitmapToBase64(bitmap)
                }

                val response = withContext(Dispatchers.IO) {
                    ApiClient.api.predict(
                        PredictRequest("data:image/jpeg;base64,$base64")
                    )
                }

                val newSign = response.sign
                _sign.value = newSign
                _confidence.value = response.confidence

                val now = System.currentTimeMillis()
                if (newSign != null && newSign != lastSign && now - lastHapticTime > 600) {
                    _hapticTrigger.value += 1
                    lastHapticTime = now
                    lastSign = newSign
                }

                newSign?.let { buildSentence(it) }

            } catch (e: Exception) {
                _sign.value = null
                _error.value = "Error: ${e.message}"
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
        _hapticTrigger.value += 1 // Trigger vibration to confirm clearing
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