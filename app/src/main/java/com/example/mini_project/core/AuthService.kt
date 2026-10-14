package com.example.mini_project.core

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

/** All Firebase Authentication calls live here. */
object AuthService {
    private val auth = FirebaseAuth.getInstance()

    val uid: String? get() = auth.currentUser?.uid

    /** Name entered at registration, or the part of the email before the @. */
    val displayName: String
        get() {
            val user = auth.currentUser
            return user?.displayName?.takeIf { it.isNotBlank() }
                ?: user?.email?.substringBefore('@')
                ?: "Friend"
        }

    fun isUserLoggedIn() = auth.currentUser != null

    fun login(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.message)
            }
    }

    fun register(name: String, email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                // Check success FIRST; reading task.result on a failed task throws
                if (!task.isSuccessful) {
                    onResult(false, task.exception?.message)
                    return@addOnCompleteListener
                }

                val user = auth.currentUser
                if (user == null) {
                    onResult(false, "Registration failed")
                    return@addOnCompleteListener
                }

                // Save the name so Home can greet the user
                val profile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
                user.updateProfile(profile).addOnCompleteListener { onResult(true, null) }
            }
    }

    fun logout() {
        auth.signOut()
    }
}