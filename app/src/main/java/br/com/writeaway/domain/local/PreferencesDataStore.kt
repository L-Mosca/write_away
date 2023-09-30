package br.com.writeaway.domain.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import br.com.writeaway.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.Exception
import java.util.prefs.Preferences
import javax.inject.Inject

class PreferencesDataStore @Inject constructor(@ApplicationContext val context: Context) :
    PreferencesContract {


    companion object {
        private const val PREFERENCES_NAME =
            "${BuildConfig.NAME}.${BuildConfig.FLAVOR}.DataStore.WriteAway"
        val layoutManagerKey = stringPreferencesKey(name = "$PREFERENCES_NAME.LayoutManagerMode")
    }

    private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = context.dataStore

    override suspend fun setLayoutManagerMode(text: String) {
        dataStore.edit { pref ->
            pref[layoutManagerKey] = text
        }
    }

    override suspend fun getLayoutManagerMode(): Flow<String> {
        return dataStore.data.catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            val layoutManager = pref[layoutManagerKey] ?: "Não tem Nada"
            layoutManager
        }
    }

}