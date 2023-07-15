package br.com.writeaway.di

import br.com.writeaway.domain.repositories.NoteRepository
import br.com.writeaway.domain.repositories.NoteRepositoryContract
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
}