package br.com.writeaway.domain.local

import kotlinx.coroutines.flow.Flow

interface PreferencesContract {
    suspend fun setLayoutManagerMode(text: String)
    suspend fun getLayoutManagerMode() : Flow<String>
}