package com.example.smartschedule.di

import com.example.smartschedule.data.repository.AuthRepository
import com.example.smartschedule.data.repository.AuthRepositoryImpl
import com.example.smartschedule.domain.usecase.auth.LoginUseCase
import com.example.smartschedule.domain.usecase.auth.validation.ValidateEmailUseCase
import com.example.smartschedule.domain.usecase.auth.validation.ValidateFullNameUseCase
import com.example.smartschedule.domain.usecase.auth.validation.ValidateNationalIdUseCase
import com.example.smartschedule.domain.usecase.auth.validation.ValidatePasswordUseCase
import com.example.smartschedule.domain.usecase.auth.validation.ValidateSignUpInputUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()


    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth,fireStore: FirebaseFirestore): AuthRepository {
        return AuthRepositoryImpl(
            firebaseAuth = firebaseAuth,
            fireStore = fireStore
        )
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideValidateEmailUseCase(): ValidateEmailUseCase {
        return ValidateEmailUseCase()
    }

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase(): ValidatePasswordUseCase {
        return ValidatePasswordUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateFullNameUseCase(): ValidateFullNameUseCase {
        return ValidateFullNameUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateNationalIdUseCase(): ValidateNationalIdUseCase {
        return ValidateNationalIdUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateSignUpInputUseCase(
        validateFullName: ValidateFullNameUseCase,
        validateNationalId: ValidateNationalIdUseCase,
        validateEmail: ValidateEmailUseCase,
        validatePassword: ValidatePasswordUseCase
    ): ValidateSignUpInputUseCase {
        return ValidateSignUpInputUseCase(
            validateFullName,
            validateNationalId,
            validateEmail,
            validatePassword
        )
    }
}
