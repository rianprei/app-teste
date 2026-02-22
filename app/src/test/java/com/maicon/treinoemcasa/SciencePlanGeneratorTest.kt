package com.maicon.treinoemcasa

import com.maicon.treinoemcasa.data.ExerciseCatalog
import com.maicon.treinoemcasa.domain.BodyType
import com.maicon.treinoemcasa.domain.EquipmentType
import com.maicon.treinoemcasa.domain.ExperienceLevel
import com.maicon.treinoemcasa.domain.SciencePlanGenerator
import com.maicon.treinoemcasa.domain.TrainingGoal
import com.maicon.treinoemcasa.domain.TrainingStyle
import com.maicon.treinoemcasa.domain.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SciencePlanGeneratorTest {
    private val generator = SciencePlanGenerator(ExerciseCatalog.allExercises)

    @Test
    fun `generate returns exactly 28 days`() {
        val profile = UserProfile(
            heightCm = 178,
            weightKg = 74,
            bodyType = BodyType.MESOMORPH,
            goal = TrainingGoal.MUSCLE_GAIN,
            style = TrainingStyle.CALISTHENICS_ONLY,
            level = ExperienceLevel.BEGINNER,
            daysPerWeek = 4,
            availableEquipment = setOf(EquipmentType.NONE)
        )

        val plan = generator.generate(profile)
        assertEquals(28, plan.days.size)
    }

    @Test
    fun `generate uses training split respecting days per week`() {
        val profile = UserProfile(
            heightCm = 165,
            weightKg = 82,
            bodyType = BodyType.ENDOMORPH,
            goal = TrainingGoal.FAT_LOSS,
            style = TrainingStyle.HIIT_HOME,
            level = ExperienceLevel.INTERMEDIATE,
            daysPerWeek = 4,
            availableEquipment = setOf(EquipmentType.NONE)
        )

        val plan = generator.generate(profile)
        val workoutDays = plan.days.count { !it.isRestDay }
        val restDays = plan.days.count { it.isRestDay }

        assertEquals(16, workoutDays)
        assertEquals(12, restDays)
    }

    @Test
    fun `generate creates non-empty workout prescriptions on workout days`() {
        val profile = UserProfile(
            heightCm = 180,
            weightKg = 70,
            bodyType = BodyType.ECTOMORPH,
            goal = TrainingGoal.STRENGTH,
            style = TrainingStyle.STRENGTH_FOCUSED,
            level = ExperienceLevel.ADVANCED,
            daysPerWeek = 5,
            availableEquipment = setOf(
                EquipmentType.NONE,
                EquipmentType.DUMBBELL,
                EquipmentType.PULL_UP_BAR,
                EquipmentType.BENCH
            )
        )

        val plan = generator.generate(profile)
        val workoutDays = plan.days.filter { !it.isRestDay }

        assertTrue(workoutDays.isNotEmpty())
        assertTrue(workoutDays.all { it.workout.isNotEmpty() })
    }
}
