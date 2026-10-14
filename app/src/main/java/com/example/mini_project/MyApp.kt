package com.example.mini_project

import android.app.Application
import com.example.mini_project.data.repo.TransactionRepo
import com.example.mini_project.data.repo.TransactionRepoImpl
import com.google.firebase.firestore.FirebaseFirestore

class MyApp : Application() {
    lateinit var repo: TransactionRepo

    override fun onCreate() {
        super.onCreate()
        repo = TransactionRepoImpl(FirebaseFirestore.getInstance())
    }
}