package com.maicon.treinoemcasa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maicon.treinoemcasa.data.AuthSyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

/**
 * AppViewModel mínimo e independente para permitir compilação do projeto
 * quando a integração com Firebase estiver desabilitada.
 *
 * Objetivos:
 * - Fornecer uma API estável que as screens possam usar (status do usuário,
 *   backup/restore, geração de plano simples, registrar treino).
 * - Não depende de implementações Firebase (usa AuthSyncRepository stub).
 * - Serve como base para depois integrar a lógica real do domínio.
 */
class AppViewModel(
    private val authSyncRepository: AuthSyncRepository = AuthSyncRepository()
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val cloudAvailable: Boolean = false,
        val uid: String? = null,
        val email: String? = null,
        val isAnonymous: Boolean = true,

        // campos de perfil simples - substitua por seu modelo real posteriormente
        val profileName: String? = null,
        val streakDays: Int = 0,
        val completedWorkouts: Int = 0,
        val lastCompletedDate: String? = null,
        val planGeneratedAt: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // lista de exercícios (genérica). Substitua pelo tipo real Exercise quando disponível.
    private val _exercises = MutableStateFlow<List<Any>>(emptyList())
    val exercises: StateFlow<List<Any>> = _exercises.asStateFlow()

    init {
        // se o AuthSyncRepository expuser sessionFlow, poderíamos subscrever aqui.
        // O stub atual tem sessionFlow; mas como é Any?, vamos evitar acessos perigosos.
    }

    /**
     * Tenta logar com email/senha usando o stub de AuthSyncRepository.
     * Atualiza o estado da UI conforme o resultado.
     */
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val res = try {
                authSyncRepository.signIn(email, password)
            } catch (t: Throwable) {
                Result.failure<Unit>(t)
            }

            if (res.isSuccess) {
                // stub: apenas marca usuário não-anônimo e seta email
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAnonymous = false,
                    email = email,
                    uid = _uiState.value.uid ?: "local-user"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = res.exceptionOrNull()?.message ?: "Erro ao autenticar"
                )
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val res = try { authSyncRepository.signUp(email, password) } catch (t: Throwable) { Result.failure<Unit>(t) }
            if (res.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false, isAnonymous = false, email = email)
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, error = res.exceptionOrNull()?.message)
            }
        }
    }

    fun signInAnonymously() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val res = try { authSyncRepository.signInAnonymously() } catch (t: Throwable) { Result.failure<Unit>(t) }
            if (res.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false, isAnonymous = true, uid = "anon-${Instant.now().epochSecond}")
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, error = res.exceptionOrNull()?.message)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authSyncRepository.signOut()
            _uiState.value = UiState() // limpa o estado para modo offline
        }
    }

    /**
     * Backup local/cloud. O stub do AuthSyncRepository tem backup() sem argumentos.
     * Chamadas no código antigo que passavam argumentos foram removidas; se houver
     * necessidade de suportar parâmetros, adicione sobrecarga compatível.
     */
    fun backup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val res = try { authSyncRepository.backup() } catch (t: Throwable) { Result.failure<Unit>(t) }
            _uiState.value = _uiState.value.copy(isLoading = false, error = res.exceptionOrNull()?.message)
        }
    }

    fun restore() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val res = try { authSyncRepository.restore() } catch (t: Throwable) { Result.failure<Unit>(t) }
            _uiState.value = _uiState.value.copy(isLoading = false, error = res.exceptionOrNull()?.message)
        }
    }

    /**
     * Gera um plano simples (placeholder). Substitua por PlanGenerator quando disponível.
     */
    fun generatePlan() {
        viewModelScope.launch {
            val now = Instant.now().toString()
            _uiState.value = _uiState.value.copy(planGeneratedAt = now, lastCompletedDate = _uiState.value.lastCompletedDate)
        }
    }

    /**
     * Marca um treino como completo (incrementa contadores simples)
     */
    fun markWorkoutCompleted() {
        viewModelScope.launch {
            val curr = _uiState.value
            _uiState.value = curr.copy(
                completedWorkouts = curr.completedWorkouts + 1,
                streakDays = curr.streakDays + 1,
                lastCompletedDate = Instant.now().toString()
            )
        }
    }

    /**
     * Atualiza um nome de perfil simples.
     */
    fun updateProfileName(name: String) {
        _uiState.value = _uiState.value.copy(profileName = name)
    }

    /**
     * Substitui a lista de exercícios (placeholder).
     */
    fun setExercises(list: List<Any>) {
        _exercises.value = list
    }
}
