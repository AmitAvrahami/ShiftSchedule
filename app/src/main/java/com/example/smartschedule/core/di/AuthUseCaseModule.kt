package com.example.smartschedule.di

import com.example.smartschedule.core.data.datastore.UserPreferences
import com.example.smartschedule.feature.auth.domain.repository.AuthRepository
import com.example.smartschedule.feature.auth.domain.usecase.LoginUseCase
import com.example.smartschedule.feature.auth.domain.usecase.LogoutUseCase
import com.example.smartschedule.feature.auth.domain.usecase.SignupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {

    /**
     * מספק את ה-LoginUseCase.
     * הוא דורש את AuthRepository (שמוזרק ע"י AuthRepositoryModule).
     */
    @Provides
    @Singleton
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        // (אם ה-LoginUseCase שלך דורש UserRepository או Validations, צריך להזריק גם אותם כאן)
    ): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    /**
     * מספק את ה-SignupUseCase.
     */
    @Provides
    @Singleton
    fun provideSignupUseCase(
        authRepository: AuthRepository,
        // ... (הזרקות נוספות להרשמה, למשל: UserRepository לשמירת User Data)
    ): SignupUseCase {
        return SignupUseCase(authRepository)
    }

    /**
     * מספק את ה-LogoutUseCase (משתמש ב-SessionManager לניקוי נתונים מקומיים).
     */
    @Provides
    @Singleton
    fun provideLogoutUseCase(
        authRepository: AuthRepository,
        userSessionManager: UserPreferences
    ): LogoutUseCase {
        return LogoutUseCase(authRepository, userSessionManager)
    }
}