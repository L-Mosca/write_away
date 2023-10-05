package br.com.writeaway.domain.repositories.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepositoryContract {

    suspend fun fetchTextSize(): Flow<Float>
    suspend fun setTextSize(textSize: Float)

    suspend fun fetchOrder(): Flow<Int>
    suspend fun setOrder(orderType: Int)

    suspend fun fetchLayoutManager(): Flow<String>
    suspend fun setLayoutManager(text: String)
}