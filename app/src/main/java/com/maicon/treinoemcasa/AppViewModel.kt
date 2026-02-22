package com.maicon.treinoemcasa

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maicon.treinoemcasa.data.AuthSyncRepository
import com.maicon.treinoemcasa.data.CloudBackupPayload
import com.maicon.treinoemcasa.data.ExerciseCatalog
import com.maicon.treinoemcasa.data.ProfileStore
import com.maicon.treinoemcasa.domain.EquipmentType
import com.maicon.treinoemcasa.domain.Exercise
import com.maicon.treinoemcasa.domain.ExperienceLevel
import com.maicon.treinoemcasa.domain.FocusArea
import com.maicon.treinoemcasa.domain.SciencePlanGenerator
import com.maicon.treinoemcasa.domain.TrainingPlan
import com.maicon.treinoemcasa.domain.UserProfile
import com.maicon.treinoemcasa.domain.WorkoutLogEntry
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExerciseFilters(
    val query: String = "",
    val focus: FocusArea? = null,
    val level: ExperienceLevel? = null,
    val equipment: EquipmentType? = null
)

data class AuthUiState(
    val cloudAvailable: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isAnonymous: Boolean = false,
    val uid: String? = null,
    val email: String? = null,
    val isBusy: Boolean = false,
    val statusMessage: String? = null,
    val lastSyncAt: String? = null
)

data class AppUiState(
    val profile: UserProfile = UserProfile(),
    val exercises: List<Exercise> = ExerciseCatalog.allExercises,
    val filteredExercises: List<Exercise> = ExerciseCatalog.allExercises,
    val filters: ExerciseFilters = ExerciseFilters(),
    val plan: TrainingPlan? = null,
    val planGeneratedAt: String? = null,
    val streakDays: Int = 0,
    val completedWorkouts: Int = 0,
    val lastCompletedDate: String? = null,
    val workoutLogs: List<WorkoutLogEntry> = emptyList(),
    val auth: AuthUiState = AuthUiState()
)

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val profileStore = ProfileStore(application.applicationContext)
    private val generator = SciencePlanGenerator(ExerciseCatalog.allExercises)
    private val authSyncRepository = AuthSyncRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        observeLocalData()
        observeAuthSession()
    }

    private fun observeLocalData() {
        viewModelScope.launch {
            profileStore.profileFlow.collect { profile ->
                _uiState.update { current ->
                    val updated = current.copy(profile = profile)
                    val withFilters = updated.copy(
                        filteredExercises = applyFilters(updated.exercises, updated.filters)
                    )
                    restorePlanIfNeeded(withFilters)
                }
            }
        }

        viewModelScope.launch {
            profileStore.streakFlow.collect { streak ->
                _uiState.update { it.copy(streakDays = streak) }
            }
        }

        viewModelScope.launch {
            profileStore.completedWorkoutsFlow.collect { completed ->
                _uiState.update { it.copy(completedWorkouts = completed) }
            }
        }

        viewModelScope.launch {
            profileStore.lastCompletedDateFlow.collect { date ->
                _uiState.update { it.copy(lastCompletedDate = date) }
            }
        }

        viewModelScope.launch {
            profileStore.planGeneratedAtFlow.collect { date ->
                _uiState.update { current ->
                    val withDate = current.copy(planGeneratedAt = date)
                    restorePlanIfNeeded(withDate)
                }
            }
        }

        viewModelScope.launch {
            profileStore.workoutLogsFlow.collect { logs ->
                _uiState.update { it.copy(workoutLogs = logs) }
            }
        }
    }

    private fun observeAuthSession() {
        viewModelScope.launch {
            authSyncRepository.sessionFlow.collect { session ->
                _uiState.update { current ->
                    current.copy(
                        auth = current.auth.copy(
                            cloudAvailable = session.cloudAvailable,
                            isAuthenticated = !session.uid.isNullOrBlank(),
                            isAnonymous = session.isAnonymous,
                            uid = session.uid,
                            email = session.email
                        )
                    )
                }
            }
        }
    }

    fun saveProfile(profile: UserProfile) {
        _uiState.update { current ->
            current.copy(
                profile = profile,
                filteredExercises = applyFilters(current.exercises, current.filters)
            )
        }
        viewModelScope.launch {
            profileStore.saveProfile(profile)
            autoBackup("Perfil salvo e sincronizado.")
        }
    }

    fun generatePlan() {
        val state = _uiState.value
        val plan = generator.generate(state.profile)
        val date = plan.generatedAt.toString()

        _uiState.update {
            it.copy(
                plan = plan,
                planGeneratedAt = date,
                auth = it.auth.copy(statusMessage = "Plano de 28 dias gerado.")
            )
        }

        viewModelScope.launch {
            profileStore.setPlanGeneratedAt(date)
            autoBackup("Plano atualizado na nuvem.")
        }
    }

    fun markTodayWorkoutDone() {
        val today = LocalDate.now().toString()
        val current = _uiState.value
        val progressed = registerTrainingDay(current, today)

        if (progressed == null) {
            _uiState.update {
                it.copy(auth = it.auth.copy(statusMessage = "Treino de hoje já está marcado."))
            }
            return
        }

        _uiState.value = progressed.copy(
            auth = progressed.auth.copy(statusMessage = "Treino registrado. Streak: ${progressed.streakDays} dia(s).")
        )

        viewModelScope.launch {
            persistProgressState(progressed)
            autoBackup("Progresso diário sincronizado.")
        }
    }

    fun addWorkoutLog(
        exerciseId: String,
        completedSets: Int,
        completedReps: Int,
        loadKg: Double,
        rpe: Int
    ) {
        if (exerciseId.isBlank()) {
            pushStatus("Selecione um exercício para registrar a sessão.")
            return
        }

        if (completedSets <= 0 || completedReps <= 0) {
            pushStatus("Séries e repetições devem ser maiores que zero.")
            return
        }

        if (loadKg < 0.0) {
            pushStatus("Carga não pode ser negativa.")
            return
        }

        val normalizedRpe = rpe.coerceIn(1, 10)
        val nowDate = LocalDate.now().toString()

        val entry = WorkoutLogEntry(
            id = "${nowDate}_${System.currentTimeMillis()}_${exerciseId}",
            date = nowDate,
            exerciseId = exerciseId,
            completedSets = completedSets,
            completedReps = completedReps,
            loadKg = loadKg,
            rpe = normalizedRpe
        )

        val current = _uiState.value
        val logs = (current.workoutLogs + entry).sortedByDescending { it.date }
        val progressed = registerTrainingDay(current.copy(workoutLogs = logs), nowDate)
        val nextState = (progressed ?: current.copy(workoutLogs = logs)).copy(
            auth = current.auth.copy(
                statusMessage = "Sessão registrada: ${findExerciseById(exerciseId)?.name ?: exerciseId}."
            )
        )

        _uiState.value = nextState

        viewModelScope.launch {
            persistProgressState(nextState)
            autoBackup("Histórico de sessões sincronizado.")
        }
    }

    fun resetProgress() {
        _uiState.update {
            it.copy(
                streakDays = 0,
                completedWorkouts = 0,
                lastCompletedDate = null,
                workoutLogs = emptyList(),
                auth = it.auth.copy(statusMessage = "Progresso local resetado.")
            )
        }

        viewModelScope.launch {
            persistProgressState(_uiState.value)
            autoBackup("Progresso resetado e sincronizado.")
        }
    }

    fun incrementStreak() = markTodayWorkoutDone()

    fun resetStreak() = resetProgress()

    fun signIn(email: String, password: String) {
        executeAuthAction {
            authSyncRepository.signIn(email, password)
                .onSuccess {
                    pushStatus("Login realizado com sucesso.")
                }
                .onFailure {
                    pushStatus(it.message ?: "Falha ao entrar.")
                }
        }
    }

    fun signUp(email: String, password: String) {
        executeAuthAction {
            authSyncRepository.signUp(email, password)
                .onSuccess {
                    pushStatus("Conta criada com sucesso.")
                }
                .onFailure {
                    pushStatus(it.message ?: "Falha ao criar conta.")
                }
        }
    }

    fun signInGuest() {
        executeAuthAction {
            authSyncRepository.signInAnonymously()
                .onSuccess {
                    pushStatus("Sessão anônima iniciada.")
                }
                .onFailure {
                    pushStatus(it.message ?: "Falha ao iniciar sessão anônima.")
                }
        }
    }

    fun signOut() {
        authSyncRepository.signOut()
        _uiState.update {
            it.copy(
                auth = it.auth.copy(
                    isAuthenticated = false,
                    isAnonymous = false,
                    uid = null,
                    email = null,
                    statusMessage = "Sessão encerrada."
                )
            )
        }
    }

    fun backupToCloud() {
        viewModelScope.launch {
            setAuthBusy(true)
            val result = authSyncRepository.backup(currentPayload())
            setAuthBusy(false)
            result
                .onSuccess {
                    val now = LocalDate.now().toString()
                    _uiState.update {
                        it.copy(
                            auth = it.auth.copy(
                                statusMessage = "Backup em nuvem concluído.",
                                lastSyncAt = now
                            )
                        )
                    }
                }
                .onFailure {
                    pushStatus(it.message ?: "Falha no backup.")
                }
        }
    }

    fun restoreFromCloud() {
        viewModelScope.launch {
            setAuthBusy(true)
            val result = authSyncRepository.restore()
            setAuthBusy(false)

            result
                .onSuccess { payload ->
                    profileStore.saveSnapshot(
                        profile = payload.profile,
                        streakDays = payload.streakDays,
                        completedWorkouts = payload.completedWorkouts,
                        lastCompletedDate = payload.lastCompletedDate,
                        planGeneratedAt = payload.planGeneratedAt,
                        workoutLogs = payload.workoutLogs
                    )

                    val plan = payload.planGeneratedAt
                        ?.let { date ->
                            val generated = generator.generate(payload.profile)
                            generated.copy(generatedAt = parseDateOrToday(date))
                        }

                    _uiState.update {
                        it.copy(
                            profile = payload.profile,
                            plan = plan,
                            planGeneratedAt = payload.planGeneratedAt,
                            streakDays = payload.streakDays,
                            completedWorkouts = payload.completedWorkouts,
                            lastCompletedDate = payload.lastCompletedDate,
                            workoutLogs = payload.workoutLogs,
                            auth = it.auth.copy(
                                statusMessage = "Backup restaurado com sucesso.",
                                lastSyncAt = LocalDate.now().toString()
                            )
                        )
                    }
                }
                .onFailure {
                    pushStatus(it.message ?: "Falha ao restaurar backup.")
                }
        }
    }

    fun setQuery(query: String) {
        updateFilters(_uiState.value.filters.copy(query = query))
    }

    fun setFocusFilter(focus: FocusArea?) {
        updateFilters(_uiState.value.filters.copy(focus = focus))
    }

    fun setLevelFilter(level: ExperienceLevel?) {
        updateFilters(_uiState.value.filters.copy(level = level))
    }

    fun setEquipmentFilter(equipment: EquipmentType?) {
        updateFilters(_uiState.value.filters.copy(equipment = equipment))
    }

    fun clearFilters() {
        updateFilters(ExerciseFilters())
    }

    fun findExerciseById(id: String): Exercise? {
        return _uiState.value.exercises.firstOrNull { it.id == id }
    }

    private fun restorePlanIfNeeded(state: AppUiState): AppUiState {
        if (state.plan != null || state.planGeneratedAt.isNullOrBlank()) {
            return state
        }

        val generated = generator.generate(state.profile)
        return state.copy(
            plan = generated.copy(generatedAt = parseDateOrToday(state.planGeneratedAt))
        )
    }

    private fun updateFilters(filters: ExerciseFilters) {
        _uiState.update { current ->
            current.copy(
                filters = filters,
                filteredExercises = applyFilters(current.exercises, filters)
            )
        }
    }

    private fun applyFilters(
        source: List<Exercise>,
        filters: ExerciseFilters
    ): List<Exercise> {
        return source.filter { exercise ->
            val matchQuery =
                filters.query.isBlank() || exercise.name.contains(filters.query, ignoreCase = true)
            val matchFocus = filters.focus == null || filters.focus in exercise.focusAreas
            val matchLevel = filters.level == null || exercise.level == filters.level
            val matchEquipment = filters.equipment == null || filters.equipment in exercise.equipment

            matchQuery && matchFocus && matchLevel && matchEquipment
        }
    }

    private fun parseDateOrToday(raw: String?): LocalDate {
        return runCatching { LocalDate.parse(raw) }.getOrElse { LocalDate.now() }
    }

    private fun registerTrainingDay(state: AppUiState, date: String): AppUiState? {
        if (state.lastCompletedDate == date) return null

        val previousDay = runCatching { LocalDate.parse(date).minusDays(1).toString() }.getOrNull()
        val nextStreak = if (state.lastCompletedDate == previousDay) {
            state.streakDays + 1
        } else {
            1
        }

        return state.copy(
            streakDays = nextStreak,
            completedWorkouts = state.completedWorkouts + 1,
            lastCompletedDate = date
        )
    }

    private suspend fun persistProgressState(state: AppUiState) {
        profileStore.setStreak(state.streakDays)
        profileStore.setCompletedWorkouts(state.completedWorkouts)
        profileStore.setLastCompletedDate(state.lastCompletedDate)
        profileStore.saveWorkoutLogs(state.workoutLogs)
    }

    private suspend fun autoBackup(successMessage: String) {
        val state = _uiState.value
        if (!state.auth.isAuthenticated) return

        authSyncRepository.backup(currentPayload())
            .onSuccess {
                _uiState.update {
                    it.copy(
                        auth = it.auth.copy(
                            statusMessage = successMessage,
                            lastSyncAt = LocalDate.now().toString()
                        )
                    )
                }
            }
            .onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        auth = state.auth.copy(
                            statusMessage = "Sync pendente: ${error.message ?: "erro de conexão"}"
                        )
                    )
                }
            }
    }

    private fun currentPayload(): CloudBackupPayload {
        val state = _uiState.value
        return CloudBackupPayload(
            profile = state.profile,
            streakDays = state.streakDays,
            completedWorkouts = state.completedWorkouts,
            lastCompletedDate = state.lastCompletedDate,
            planGeneratedAt = state.planGeneratedAt,
            workoutLogs = state.workoutLogs
        )
    }

    private fun executeAuthAction(action: suspend () -> Unit) {
        viewModelScope.launch {
            setAuthBusy(true)
            try {
                action()
            } finally {
                setAuthBusy(false)
            }
        }
    }

    private fun setAuthBusy(isBusy: Boolean) {
        _uiState.update {
            it.copy(auth = it.auth.copy(isBusy = isBusy))
        }
    }

    private fun pushStatus(message: String) {
        _uiState.update {
            it.copy(auth = it.auth.copy(statusMessage = message))
        }
    }
}
