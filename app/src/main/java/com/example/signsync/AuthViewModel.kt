package com.example.signsync

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState



    private val _feedbackState = MutableStateFlow<FeedbackState>(FeedbackState.Idle)
    val feedbackState: StateFlow<FeedbackState> = _feedbackState


    // Inside your AuthViewModel class
    private val _displayName = mutableStateOf("User")
    val displayName = _displayName

    fun updateDisplayName(newName: String) {
        _displayName.value = newName
    }

    init {
        // Auto-login check on initialization
        auth.currentUser?.let {
            _authState.value = AuthState.Success(it)
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.user?.let {
                    _authState.value = AuthState.Success(it)
                } ?: run {
                    _authState.value = AuthState.Error("Login failed. Try again.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("All fields are required")
            return
        }
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                result.user?.let {
                    _authState.value = AuthState.Success(it)
                } ?: run {
                    _authState.value = AuthState.Error("Sign up failed. Try again.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }


    // Add this inside your AuthViewModel class
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                result.user?.let {
                    _authState.value = AuthState.Success(it)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Google Sign-In failed")
            }
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.LoggedOut
    }

    fun submitFeedback(name: String, message: String) {
        if (name.isBlank() || message.isBlank()) {
            _feedbackState.value = FeedbackState.Error("Name and message cannot be empty")
            return
        }
        viewModelScope.launch {
            _feedbackState.value = FeedbackState.Loading
            try {
                val feedback = hashMapOf(
                    "name" to name,
                    "message" to message,
                    "email" to (auth.currentUser?.email ?: "anonymous"),
                    "timestamp" to System.currentTimeMillis()
                )
                firestore.collection("feedback").add(feedback).await()
                _feedbackState.value = FeedbackState.Success
            } catch (e: Exception) {
                _feedbackState.value = FeedbackState.Error(e.message ?: "Failed to submit")
            }
        }
    }

    fun resetFeedbackState() {
        _feedbackState.value = FeedbackState.Idle
    }
}