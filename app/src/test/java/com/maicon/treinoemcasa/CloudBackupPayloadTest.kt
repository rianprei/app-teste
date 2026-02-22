package com.maicon.treinoemcasa

import com.maicon.treinoemcasa.data.CloudBackupPayload
import com.maicon.treinoemcasa.domain.BodyType
import com.maicon.treinoemcasa.domain.EquipmentType
import com.maicon.treinoemcasa.domain.TrainingGoal
import com.maicon.treinoemcasa.domain.TrainingStyle
import com.maicon.treinoemcasa.domain.UserProfile
import com.maicon.treinoemcasa.domain.WorkoutLogEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CloudBackupPayloadTest {
    @Test
    fun `cloud payload should roundtrip workout logs`() {
        val payload = CloudBackupPayload(
            profile = UserProfile(
                heightCm = 175,
                weightKg = 78,
                bodyType = BodyType.MESOMORPH,
                goal = TrainingGoal.MUSCLE_GAIN,
                style = TrainingStyle.CALISTHENICS_ONLY,
                availableEquipment = setOf(EquipmentType.NONE, EquipmentType.PULL_UP_BAR)
            ),
            streakDays = 6,
            completedWorkouts = 17,
            lastCompletedDate = "2026-02-22",
            planGeneratedAt = "2026-02-21",
            workoutLogs = listOf(
                WorkoutLogEntry(
                    id = "log_1",
                    date = "2026-02-22",
                    exerciseId = "push_up",
                    completedSets = 4,
                    completedReps = 12,
                    loadKg = 0.0,
                    rpe = 8
                )
            )
        )

        val restored = CloudBackupPayload.fromMap(payload.toMap())
        assertNotNull(restored)
        assertEquals(1, restored!!.workoutLogs.size)
        assertEquals("push_up", restored.workoutLogs.first().exerciseId)
        assertEquals(17, restored.completedWorkouts)
    }
}
