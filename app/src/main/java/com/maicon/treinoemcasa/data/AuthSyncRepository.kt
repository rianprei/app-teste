package com.maicon.treinoemcasa.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthSyncRepository(
    private val any1: Any? = null,
    private val any2: Any? = null
) {

    private val _sessionFlow = MutableStateFlow(
        AuthSession(
            cloudAvailable = false,
            uid = null,
            email = null,
            isAnonymous = false
        )
    )
    val sessionFlow: StateFlow<AuthSession> = _sessionFlow

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return Result.failure(UnsupportedOperationException("Auth desativado"))
    }

    suspend fun signUp(email: String, password: String): Result<Unit> {
        return Result.failure(UnsupportedOperationException("Auth desativado"))
    }

    suspend fun signInAnonymously(): Result<Unit> {
        return Result.failure(UnsupportedOperationException("Auth desativado"))
    }

    suspend fun backup(payload: CloudBackupPayload): Result<Unit> {
        return Result.failure(UnsupportedOperationException("Backup em nuvem desativado"))
    }

    suspend fun restore(): Result<CloudBackupPayload> {
        return Result.failure(UnsupportedOperationException("Restauração em nuvem desativada"))
    }

    fun signOut() {
        _sessionFlow.value = AuthSession(
            cloudAvailable = false,
            uid = null,
            email = null,
            isAnonymous = false
        )
    }
}
