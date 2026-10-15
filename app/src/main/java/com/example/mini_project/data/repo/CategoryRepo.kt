package com.example.mini_project.data.repo

import com.example.mini_project.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepo {
    fun getCategories(): Flow<List<Category>>

    suspend fun insertCategory(category: Category)
}