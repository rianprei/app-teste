package com.maicon.treinoemcasa.data

import com.maicon.treinoemcasa.domain.BodyType
import com.maicon.treinoemcasa.domain.EquipmentType
import com.maicon.treinoemcasa.domain.ExperienceLevel
import com.maicon.treinoemcasa.domain.TrainingGoal
import com.maicon.treinoemcasa.domain.TrainingStyle
import com.maicon.treinoemcasa.domain.UserProfile
import com.maicon.treinoemcasa.domain.WorkoutLogEntry

data class CloudBackupPayload(
    val profile: UserProfile,
    val streakDays: Int,
    val completedWorkouts: Int,
    val lastCompletedDate: String?,
    val planGeneratedAt: String?,
    val workoutLogs: List<WorkoutLogEntry>
) {
    fun toMap(): Map<String, Any> {
        val profileMap = mapOf(
            "heightCm" to profile.heightCm,
            "weightKg" to profile.weightKg,
            "bodyType" to profile.bodyType.name,
            "goal" to profile.goal.name,
            "style" to profile.style.name,
            "level" to profile.level.name,
            "daysPerWeek" to profile.daysPerWeek,
            "equipment" to profile.availableEquipment.map { it.name }
        )

        val root = mutableMapOf<String, Any>(
            "profile" to profileMap,
            "streakDays" to streakDays,
            "completedWorkouts" to completedWorkouts
        )

        if (!lastCompletedDate.isNullOrBlank()) {
            root["lastCompletedDate"] = lastCompletedDate
        }
        if (!planGeneratedAt.isNullOrBlank()) {
            root["planGeneratedAt"] = planGeneratedAt
        }
        if (workoutLogs.isNotEmpty()) {
            root["workoutLogs"] = workoutLogs.map { entry ->
                mapOf(
                    "id" to entry.id,
                    "date" to entry.date,
                    "exerciseId" to entry.exerciseId,
                    "completedSets" to entry.completedSets,
                    "completedReps" to entry.completedReps,
                    "loadKg" to entry.loadKg,
                    "rpe" to entry.rpe
                )
            }
        }

        return root
    }

    companion object {
        fun fromMap(data: Map<String, Any>): CloudBackupPayload? {
            val profileMap = data["profile"] as? Map<*, *> ?: return null

            val profile = UserProfile(
                heightCm = (profileMap["heightCm"] as? Number)?.toInt() ?: 170,
                weightKg = (profileMap["weightKg"] as? Number)?.toInt() ?: 70,
                bodyType = enumOrDefault(profileMap["bodyType"] as? String, BodyType.MESOMORPH),
                goal = enumOrDefault(profileMap["goal"] as? String, TrainingGoal.MUSCLE_GAIN),
                style = enumOrDefault(profileMap["style"] as? String, TrainingStyle.CALISTHENICS_ONLY),
                level = enumOrDefault(profileMap["level"] as? String, ExperienceLevel.BEGINNER),
                daysPerWeek = ((profileMap["daysPerWeek"] as? Number)?.toInt() ?: 4).coerceIn(3, 7),
                availableEquipment = parseEquipment(profileMap["equipment"])
                    .ifEmpty { setOf(EquipmentType.NONE) }
            )

            return CloudBackupPayload(
                profile = profile,
                streakDays = ((data["streakDays"] as? Number)?.toInt() ?: 0).coerceAtLeast(0),
                completedWorkouts = ((data["completedWorkouts"] as? Number)?.toInt() ?: 0).coerceAtLeast(0),
                lastCompletedDate = data["lastCompletedDate"] as? String,
                planGeneratedAt = data["planGeneratedAt"] as? String,
                workoutLogs = parseWorkoutLogs(data["workoutLogs"])
            )
        }

        private fun parseEquipment(raw: Any?): Set<EquipmentType> {
            val values = raw as? List<*> ?: return emptySet()
            return values
                .mapNotNull { value ->
                    val name = value as? String ?: return@mapNotNull null
                    runCatching { EquipmentType.valueOf(name) }.getOrNull()
                }
                .toSet()
        }

        private inline fun <reified T : Enum<T>> enumOrDefault(value: String?, fallback: T): T {
            if (value.isNullOrBlank()) return fallback
            return runCatching { enumValueOf<T>(value) }.getOrDefault(fallback)
        }

        private fun parseWorkoutLogs(raw: Any?): List<WorkoutLogEntry> {
            val rows = raw as? List<*> ?: return emptyList()
            return rows.mapNotNull { row ->
                val map = row as? Map<*, *> ?: return@mapNotNull null
                val id = map["id"] as? String ?: return@mapNotNull null
                val date = map["date"] as? String ?: return@mapNotNull null
                val exerciseId = map["exerciseId"] as? String ?: return@mapNotNull null
                val completedSets = (map["completedSets"] as? Number)?.toInt() ?: return@mapNotNull null
                val completedReps = (map["completedReps"] as? Number)?.toInt() ?: return@mapNotNull null
                val loadKg = (map["loadKg"] as? Number)?.toDouble() ?: return@mapNotNull null
                val rpe = (map["rpe"] as? Number)?.toInt() ?: return@mapNotNull null

                WorkoutLogEntry(
                    id = id,
                    date = date,
                    exerciseId = exerciseId,
                    completedSets = completedSets,
                    completedReps = completedReps,
                    loadKg = loadKg,
                    rpe = rpe
                )
            }.sortedByDescending { it.date }
        }
    }
}

data class AuthSession(
    val cloudAvailable: Boolean,
    val uid: String?,
    val email: String?,
    val isAnonymous: Boolean
)
