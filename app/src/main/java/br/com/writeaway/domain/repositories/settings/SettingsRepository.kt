package br.com.writeaway.domain.repositories.settings

import br.com.writeaway.domain.local.PreferencesContract
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepository @Inject constructor(private val dataStore: PreferencesContract) :
    SettingsRepositoryContract {

    override suspend fun fetchTextSize(): Flow<Float> = dataStore.getTextSize()
    override suspend fun setTextSize(textSize: Float) = dataStore.setTextSize(textSize)

    override suspend fun fetchOrder(): Flow<Int> = dataStore.getOrderMode()
    override suspend fun setOrder(orderType: Int) = dataStore.setOrderMode(orderType)

    override suspend fun fetchLayoutManager() = dataStore.getLayoutManagerMode()
    override suspend fun setLayoutManager(text: String) = dataStore.setLayoutManagerMode(text)
}