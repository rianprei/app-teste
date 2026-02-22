package com.maicon.treinoemcasa.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.maicon.treinoemcasa.domain.BodyType
import com.maicon.treinoemcasa.domain.EquipmentType
import com.maicon.treinoemcasa.domain.ExperienceLevel
import com.maicon.treinoemcasa.domain.TrainingGoal
import com.maicon.treinoemcasa.domain.TrainingStyle
import com.maicon.treinoemcasa.domain.UserProfile
import com.maicon.treinoemcasa.domain.WorkoutLogEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileStore by preferencesDataStore(name = "treino_profile")

class ProfileStore(private val context: Context) {
    private object Keys {
        val HEIGHT = intPreferencesKey("height_cm")
        val WEIGHT = intPreferencesKey("weight_kg")
        val BODY_TYPE = stringPreferencesKey("body_type")
        val GOAL = stringPreferencesKey("goal")
        val STYLE = stringPreferencesKey("style")
        val LEVEL = stringPreferencesKey("level")
        val DAYS_PER_WEEK = intPreferencesKey("days_per_week")
        val EQUIPMENT = stringPreferencesKey("equipment")
        val STREAK = intPreferencesKey("streak")
        val COMPLETED_WORKOUTS = intPreferencesKey("completed_workouts")
        val LAST_COMPLETED_DATE = stringPreferencesKey("last_completed_date")
        val PLAN_GENERATED_AT = stringPreferencesKey("plan_generated_at")
        val WORKOUT_LOGS = stringPreferencesKey("workout_logs")
    }

    val profileFlow: Flow<UserProfile> = context.profileStore.data.map { prefs ->
        val default = UserProfile()
        UserProfile(
            heightCm = prefs[Keys.HEIGHT] ?: default.heightCm,
            weightKg = prefs[Keys.WEIGHT] ?: default.weightKg,
            bodyType = enumOrDefault(prefs[Keys.BODY_TYPE], default.bodyType),
            goal = enumOrDefault(prefs[Keys.GOAL], default.goal),
            style = enumOrDefault(prefs[Keys.STYLE], default.style),
            level = enumOrDefault(prefs[Keys.LEVEL], default.level),
            daysPerWeek = (prefs[Keys.DAYS_PER_WEEK] ?: default.daysPerWeek).coerceIn(3, 7),
            availableEquipment = parseEquipment(prefs[Keys.EQUIPMENT])
                .ifEmpty { default.availableEquipment }
        )
    }

    val streakFlow: Flow<Int> = context.profileStore.data.map { prefs ->
        (prefs[Keys.STREAK] ?: 0).coerceAtLeast(0)
    }

    val completedWorkoutsFlow: Flow<Int> = context.profileStore.data.map { prefs ->
        (prefs[Keys.COMPLETED_WORKOUTS] ?: 0).coerceAtLeast(0)
    }

    val lastCompletedDateFlow: Flow<String?> = context.profileStore.data.map { prefs ->
        prefs[Keys.LAST_COMPLETED_DATE]
    }

    val planGeneratedAtFlow: Flow<String?> = context.profileStore.data.map { prefs ->
        prefs[Keys.PLAN_GENERATED_AT]
    }

    val workoutLogsFlow: Flow<List<WorkoutLogEntry>> = context.profileStore.data.map { prefs ->
        parseWorkoutLogs(prefs[Keys.WORKOUT_LOGS])
    }

    suspend fun saveProfile(profile: UserProfile) {
        context.profileStore.edit { prefs ->
            prefs[Keys.HEIGHT] = profile.heightCm
            prefs[Keys.WEIGHT] = profile.weightKg
            prefs[Keys.BODY_TYPE] = profile.bodyType.name
            prefs[Keys.GOAL] = profile.goal.name
            prefs[Keys.STYLE] = profile.style.name
            prefs[Keys.LEVEL] = profile.level.name
            prefs[Keys.DAYS_PER_WEEK] = profile.daysPerWeek
            prefs[Keys.EQUIPMENT] = profile.availableEquipment.joinToString(separator = ",") { it.name }
        }
    }

    suspend fun setStreak(days: Int) {
        context.profileStore.edit { prefs ->
            prefs[Keys.STREAK] = days.coerceAtLeast(0)
        }
    }

    suspend fun setCompletedWorkouts(count: Int) {
        context.profileStore.edit { prefs ->
            prefs[Keys.COMPLETED_WORKOUTS] = count.coerceAtLeast(0)
        }
    }

    suspend fun setLastCompletedDate(date: String?) {
        context.profileStore.edit { prefs ->
            if (date.isNullOrBlank()) {
                prefs.remove(Keys.LAST_COMPLETED_DATE)
            } else {
                prefs[Keys.LAST_COMPLETED_DATE] = date
            }
        }
    }

    suspend fun setPlanGeneratedAt(date: String?) {
        context.profileStore.edit { prefs ->
            if (date.isNullOrBlank()) {
                prefs.remove(Keys.PLAN_GENERATED_AT)
            } else {
                prefs[Keys.PLAN_GENERATED_AT] = date
            }
        }
    }

    suspend fun saveSnapshot(
        profile: UserProfile,
        streakDays: Int,
        completedWorkouts: Int,
        lastCompletedDate: String?,
        planGeneratedAt: String?,
        workoutLogs: List<WorkoutLogEntry> = emptyList()
    ) {
        context.profileStore.edit { prefs ->
            prefs[Keys.HEIGHT] = profile.heightCm
            prefs[Keys.WEIGHT] = profile.weightKg
            prefs[Keys.BODY_TYPE] = profile.bodyType.name
            prefs[Keys.GOAL] = profile.goal.name
            prefs[Keys.STYLE] = profile.style.name
            prefs[Keys.LEVEL] = profile.level.name
            prefs[Keys.DAYS_PER_WEEK] = profile.daysPerWeek
            prefs[Keys.EQUIPMENT] = profile.availableEquipment.joinToString(separator = ",") { it.name }
            prefs[Keys.STREAK] = streakDays.coerceAtLeast(0)
            prefs[Keys.COMPLETED_WORKOUTS] = completedWorkouts.coerceAtLeast(0)

            if (lastCompletedDate.isNullOrBlank()) {
                prefs.remove(Keys.LAST_COMPLETED_DATE)
            } else {
                prefs[Keys.LAST_COMPLETED_DATE] = lastCompletedDate
            }

            if (planGeneratedAt.isNullOrBlank()) {
                prefs.remove(Keys.PLAN_GENERATED_AT)
            } else {
                prefs[Keys.PLAN_GENERATED_AT] = planGeneratedAt
            }

            if (workoutLogs.isEmpty()) {
                prefs.remove(Keys.WORKOUT_LOGS)
            } else {
                prefs[Keys.WORKOUT_LOGS] = encodeWorkoutLogs(workoutLogs)
            }
        }
    }

    suspend fun saveWorkoutLogs(logs: List<WorkoutLogEntry>) {
        context.profileStore.edit { prefs ->
            if (logs.isEmpty()) {
                prefs.remove(Keys.WORKOUT_LOGS)
            } else {
                prefs[Keys.WORKOUT_LOGS] = encodeWorkoutLogs(logs)
            }
        }
    }

    private fun parseEquipment(raw: String?): Set<EquipmentType> {
        if (raw.isNullOrBlank()) return emptySet()
        return raw.split(',')
            .mapNotNull { value ->
                runCatching { EquipmentType.valueOf(value.trim()) }.getOrNull()
            }
            .toSet()
    }

    private inline fun <reified T : Enum<T>> enumOrDefault(value: String?, fallback: T): T {
        if (value.isNullOrBlank()) return fallback
        return runCatching { enumValueOf<T>(value) }.getOrDefault(fallback)
    }

    private fun encodeWorkoutLogs(logs: List<WorkoutLogEntry>): String {
        return logs.joinToString(separator = ";") { entry ->
            listOf(
                entry.id,
                entry.date,
                entry.exerciseId,
                entry.completedSets.toString(),
                entry.completedReps.toString(),
                entry.loadKg.toString(),
                entry.rpe.toString()
            ).joinToString(separator = "|")
        }
    }

    private fun parseWorkoutLogs(raw: String?): List<WorkoutLogEntry> {
        if (raw.isNullOrBlank()) return emptyList()
        return raw.split(';')
            .mapNotNull { row ->
                val parts = row.split('|')
                if (parts.size != 7) return@mapNotNull null
                val sets = parts[3].toIntOrNull() ?: return@mapNotNull null
                val reps = parts[4].toIntOrNull() ?: return@mapNotNull null
                val loadKg = parts[5].toDoubleOrNull() ?: return@mapNotNull null
                val rpe = parts[6].toIntOrNull() ?: return@mapNotNull null

                WorkoutLogEntry(
                    id = parts[0],
                    date = parts[1],
                    exerciseId = parts[2],
                    completedSets = sets,
                    completedReps = reps,
                    loadKg = loadKg,
                    rpe = rpe
                )
            }
            .sortedByDescending { it.date }
    }
}
