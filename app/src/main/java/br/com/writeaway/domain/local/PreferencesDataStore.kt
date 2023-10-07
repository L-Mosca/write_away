package br.com.writeaway.domain.local

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.writeaway.BuildConfig
import br.com.writeaway.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesDataStore @Inject constructor(@ApplicationContext val context: Context) :
    PreferencesContract {

    companion object {
        private const val PREFERENCES_NAME =
            "${BuildConfig.NAME}.${BuildConfig.FLAVOR}.DataStore.WriteAway"

        private val textSizeKey = floatPreferencesKey(name = "$PREFERENCES_NAME.TextSizeKey")
        private val orderKey = intPreferencesKey(name = "$PREFERENCES_NAME.OrderKey")
        private val layoutManagerKey =
            stringPreferencesKey(name = "$PREFERENCES_NAME.LayoutManagerKey")
    }

    private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = context.dataStore


    override suspend fun setTextSize(textSize: Float) {
        dataStore.edit { pref ->
            pref[textSizeKey] = textSize
        }
    }

    override suspend fun getTextSize(): Flow<Float> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            val textSize = pref[textSizeKey] ?: 14f
            textSize
        }

    }

    override suspend fun setOrderMode(orderMode: Int) {
        dataStore.edit { pref ->
            pref[orderKey] = orderMode
        }
    }

    override suspend fun getOrderMode(): Flow<Int> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            val orderMode = pref[orderKey] ?: -1
            orderMode
        }
    }

    override suspend fun setLayoutManagerMode(text: String) {
        dataStore.edit { pref ->
            pref[layoutManagerKey] = text
        }
    }

    override suspend fun getLayoutManagerMode(): Flow<String> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[layoutManagerKey] ?: ContextCompat.getString(context, R.string.viewInList)
        }
    }

}