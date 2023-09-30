package br.com.writeaway.di

import br.com.writeaway.domain.local.PreferencesContract
import br.com.writeaway.domain.local.PreferencesDataStore
import br.com.writeaway.domain.repositories.NoteRepository
import br.com.writeaway.domain.repositories.NoteRepositoryContract
import dagger.Binds
import dagger.Module
import dagger.Provides
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
    fun bindPreferencesDataStore(preferencesDataStore: PreferencesDataStore): PreferencesContract
}