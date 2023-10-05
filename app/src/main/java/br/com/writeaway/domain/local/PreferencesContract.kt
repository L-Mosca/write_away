package br.com.writeaway.domain.local

import kotlinx.coroutines.flow.Flow

interface PreferencesContract {

    suspend fun setTextSize(textSize: Float)
    suspend fun getTextSize(): Flow<Float>

    suspend fun setOrderMode(orderMode: Int)
    suspend fun getOrderMode(): Flow<Int>

    suspend fun setLayoutManagerMode(text: String)
    suspend fun getLayoutManagerMode(): Flow<String>
}