package br.com.writeaway.di

import br.com.writeaway.domain.local.PreferencesContract
import br.com.writeaway.domain.local.PreferencesDataStore
import br.com.writeaway.domain.repositories.note.NoteRepository
import br.com.writeaway.domain.repositories.note.NoteRepositoryContract
import br.com.writeaway.domain.repositories.settings.SettingsRepository
import br.com.writeaway.domain.repositories.settings.SettingsRepositoryContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Singleton
    @Binds
    fun bindNoteRepository(noteRepository: NoteRepository): NoteRepositoryContract

    @Singleton
    @Binds
    fun bindSettingsRepository(settingsRepository: SettingsRepository) : SettingsRepositoryContract

    @Singleton
    @Binds
    fun bindPreferencesDataStore(preferencesDataStore: PreferencesDataStore): PreferencesContract
}