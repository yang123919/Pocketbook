package com.example.mini_project

import android.app.Application
import com.example.mini_project.data.repo.CategoryRepo
import com.example.mini_project.data.repo.CategoryRepoImpl
import com.example.mini_project.data.repo.TransactionRepo
import com.example.mini_project.data.repo.TransactionRepoImpl
import com.google.firebase.firestore.FirebaseFirestore

class MyApp : Application() {
    lateinit var repo: TransactionRepo
    lateinit var categoryRepo: CategoryRepo

    override fun onCreate() {
        super.onCreate()
        val db = FirebaseFirestore.getInstance()
        repo = TransactionRepoImpl(db)
        categoryRepo = CategoryRepoImpl(db)
    }
}