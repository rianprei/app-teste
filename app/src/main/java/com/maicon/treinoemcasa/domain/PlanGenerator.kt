package com.maicon.treinoemcasa.domain

import kotlin.random.Random

class SciencePlanGenerator(
    private val exerciseCatalog: List<Exercise>
) {
    fun generate(profile: UserProfile): TrainingPlan {
        val weeklyTrainingDays = trainingDaySlots(profile.daysPerWeek)
        val focusRotation = focusRotationFor(profile.style)
        val days = mutableListOf<PlanDay>()

        var focusPointer = 0

        for (dayNumber in 1..28) {
            val dayOfWeek = ((dayNumber - 1) % 7) + 1
            val week = ((dayNumber - 1) / 7) + 1
            val isWorkoutDay = dayOfWeek in weeklyTrainingDays

            if (!isWorkoutDay) {
                days += PlanDay(
                    dayNumber = dayNumber,
                    title = "Dia $dayNumber - Recuperação ativa",
                    focus = FocusArea.RECOVERY,
                    intensity = "Baixa",
                    challenge = restChallengeFor(dayNumber),
                    recoveryTip = recoveryTip(profile, week),
                    isRestDay = true,
                    workout = emptyList()
                )
                continue
            }

            val focus = focusRotation[focusPointer % focusRotation.size]
            focusPointer += 1

            val selectedExercises = selectExercises(
                dayNumber = dayNumber,
                week = week,
                focus = focus,
                profile = profile
            )

            val workout = selectedExercises.mapIndexed { index, exercise ->
                prescriptionFor(exercise, profile, week, index)
            }

            days += PlanDay(
                dayNumber = dayNumber,
                title = "Dia $dayNumber - ${focus.label}",
                focus = focus,
                intensity = intensityLabel(profile, week),
                challenge = workoutChallengeFor(dayNumber, week, focus),
                recoveryTip = recoveryTip(profile, week),
                isRestDay = false,
                workout = workout
            )
        }

        return TrainingPlan(
            generatedAt = java.time.LocalDate.now(),
            userProfile = profile,
            days = days
        )
    }

    private fun trainingDaySlots(daysPerWeek: Int): Set<Int> = when (daysPerWeek.coerceIn(3, 7)) {
        3 -> setOf(1, 3, 5)
        4 -> setOf(1, 2, 4, 6)
        5 -> setOf(1, 2, 3, 5, 6)
        6 -> setOf(1, 2, 3, 4, 5, 6)
        else -> setOf(1, 2, 3, 4, 5, 6, 7)
    }

    private fun focusRotationFor(style: TrainingStyle): List<FocusArea> = when (style) {
        TrainingStyle.CALISTHENICS_ONLY -> listOf(
            FocusArea.CHEST,
            FocusArea.BACK,
            FocusArea.LEGS,
            FocusArea.ABS,
            FocusArea.ARMS,
            FocusArea.FULL_BODY
        )
        TrainingStyle.MIXED_WITH_EQUIPMENT -> listOf(
            FocusArea.CHEST,
            FocusArea.BACK,
            FocusArea.LEGS,
            FocusArea.ARMS,
            FocusArea.FULL_BODY,
            FocusArea.ABS
        )
        TrainingStyle.HIIT_HOME -> listOf(
            FocusArea.FULL_BODY,
            FocusArea.ABS,
            FocusArea.LEGS,
            FocusArea.FULL_BODY,
            FocusArea.BACK,
            FocusArea.ARMS
        )
        TrainingStyle.STRENGTH_FOCUSED -> listOf(
            FocusArea.LEGS,
            FocusArea.CHEST,
            FocusArea.BACK,
            FocusArea.ARMS,
            FocusArea.FULL_BODY,
            FocusArea.ABS
        )
    }

    private fun selectExercises(
        dayNumber: Int,
        week: Int,
        focus: FocusArea,
        profile: UserProfile
    ): List<Exercise> {
        val rng = Random(dayNumber * 79 + week * 17)
        val allowedMaxLevel = when (profile.level) {
            ExperienceLevel.BEGINNER -> if (week >= 3) ExperienceLevel.INTERMEDIATE else ExperienceLevel.BEGINNER
            ExperienceLevel.INTERMEDIATE -> if (week >= 4) ExperienceLevel.ADVANCED else ExperienceLevel.INTERMEDIATE
            ExperienceLevel.ADVANCED -> ExperienceLevel.ADVANCED
        }

        val candidates = exerciseCatalog
            .asSequence()
            .filter { focus in it.focusAreas || FocusArea.FULL_BODY in it.focusAreas }
            .filter { it.level.ordinal <= allowedMaxLevel.ordinal }
            .filter { isEquipmentCompatible(it, profile.availableEquipment) }
            .filter { styleCompatible(it, profile.style) }
            .toList()

        val fallback = exerciseCatalog
            .asSequence()
            .filter { focus in it.focusAreas || FocusArea.FULL_BODY in it.focusAreas }
            .filter { it.level.ordinal <= profile.level.ordinal + 1 }
            .toList()

        val pool = if (candidates.size >= 4) candidates else fallback
        val count = baseExerciseCount(profile.level) + if (week >= 3) 1 else 0

        return pool.shuffled(rng).take(count.coerceAtMost(pool.size))
    }

    private fun baseExerciseCount(level: ExperienceLevel): Int = when (level) {
        ExperienceLevel.BEGINNER -> 5
        ExperienceLevel.INTERMEDIATE -> 6
        ExperienceLevel.ADVANCED -> 7
    }

    private fun isEquipmentCompatible(exercise: Exercise, availableEquipment: Set<EquipmentType>): Boolean {
        if (EquipmentType.NONE in exercise.equipment) return true
        return exercise.equipment.any { it in availableEquipment }
    }

    private fun styleCompatible(exercise: Exercise, style: TrainingStyle): Boolean = when (style) {
        TrainingStyle.CALISTHENICS_ONLY -> {
            val calisthenicsEquipment = setOf(
                EquipmentType.NONE,
                EquipmentType.PULL_UP_BAR,
                EquipmentType.RESISTANCE_BAND,
                EquipmentType.BENCH,
                EquipmentType.JUMP_ROPE,
                EquipmentType.GYMNASTIC_RINGS,
                EquipmentType.PARALLETTES,
                EquipmentType.WEIGHT_VEST,
                EquipmentType.AB_WHEEL
            )
            exercise.equipment.any { it in calisthenicsEquipment }
        }
        TrainingStyle.MIXED_WITH_EQUIPMENT -> true
        TrainingStyle.HIIT_HOME -> {
            FocusArea.FULL_BODY in exercise.focusAreas ||
                FocusArea.ABS in exercise.focusAreas ||
                FocusArea.LEGS in exercise.focusAreas
        }
        TrainingStyle.STRENGTH_FOCUSED -> {
            exercise.level != ExperienceLevel.BEGINNER ||
                (EquipmentType.NONE !in exercise.equipment || exercise.movementPattern == MovementPattern.SKILL_STATIC)
        }
    }

    private fun prescriptionFor(
        exercise: Exercise,
        profile: UserProfile,
        week: Int,
        index: Int
    ): WorkoutPrescription {
        val progressionBonus = (week - 1) / 2
        val setsBase = when (profile.goal) {
            TrainingGoal.MUSCLE_GAIN -> 3
            TrainingGoal.FAT_LOSS -> 3
            TrainingGoal.STRENGTH -> 4
            TrainingGoal.ENDURANCE -> 3
            TrainingGoal.RECONDITIONING -> 2
        }

        val sets = (setsBase + progressionBonus + if (index % 3 == 0 && week >= 3) 1 else 0).coerceAtMost(6)

        val reps = when (profile.goal) {
            TrainingGoal.MUSCLE_GAIN -> if (exercise.level == ExperienceLevel.ADVANCED) "6-10" else "8-12"
            TrainingGoal.FAT_LOSS -> "30-45s"
            TrainingGoal.STRENGTH -> "4-8"
            TrainingGoal.ENDURANCE -> "15-25"
            TrainingGoal.RECONDITIONING -> "10-15"
        }

        val rest = when (profile.goal) {
            TrainingGoal.STRENGTH -> 120
            TrainingGoal.MUSCLE_GAIN -> 90
            TrainingGoal.ENDURANCE -> 45
            TrainingGoal.FAT_LOSS -> 35
            TrainingGoal.RECONDITIONING -> 60
        } + when (profile.level) {
            ExperienceLevel.BEGINNER -> 15
            ExperienceLevel.INTERMEDIATE -> 0
            ExperienceLevel.ADVANCED -> -10
        }

        val loadHint = if (EquipmentType.NONE in exercise.equipment) {
            "Peso corporal"
        } else {
            when (profile.goal) {
                TrainingGoal.STRENGTH -> "Carga alta com execução limpa"
                TrainingGoal.MUSCLE_GAIN -> "Carga moderada a alta"
                TrainingGoal.FAT_LOSS -> "Carga leve/moderada com ritmo alto"
                TrainingGoal.ENDURANCE -> "Carga leve com técnica estável"
                TrainingGoal.RECONDITIONING -> "Carga leve e progressiva"
            }
        }

        return WorkoutPrescription(
            exerciseId = exercise.id,
            sets = sets,
            reps = reps,
            restSeconds = rest.coerceAtLeast(20),
            loadHint = loadHint
        )
    }

    private fun intensityLabel(profile: UserProfile, week: Int): String {
        val base = when (profile.level) {
            ExperienceLevel.BEGINNER -> 4
            ExperienceLevel.INTERMEDIATE -> 6
            ExperienceLevel.ADVANCED -> 7
        }

        val styleBonus = when (profile.style) {
            TrainingStyle.HIIT_HOME -> 1
            TrainingStyle.STRENGTH_FOCUSED -> 1
            else -> 0
        }

        val weekBonus = if (week == 4) 1 else 0
        val score = (base + styleBonus + weekBonus).coerceIn(1, 10)
        return "$score/10"
    }

    private fun workoutChallengeFor(dayNumber: Int, week: Int, focus: FocusArea): String {
        val challengePool = listOf(
            "Concluir todo o treino sem pular séries",
            "Adicionar 1 repetição em cada série final",
            "Manter descanso estrito e cronometrado",
            "Executar todas as repetições com tempo controlado (2-1-2)",
            "Registrar percepção de esforço e técnica pós-treino"
        )
        val base = challengePool[(dayNumber + week) % challengePool.size]
        return "${focus.label}: $base"
    }

    private fun restChallengeFor(dayNumber: Int): String {
        val options = listOf(
            "Caminhada leve de 20 minutos",
            "Sessão curta de mobilidade (10-15 min)",
            "Sono mínimo de 7h30 hoje",
            "Hidratação reforçada ao longo do dia",
            "Alongamento de quadril, coluna torácica e tornozelos"
        )
        return options[dayNumber % options.size]
    }

    private fun recoveryTip(profile: UserProfile, week: Int): String {
        val bmi = profile.bmi()
        val bmiNote = when {
            bmi < 18.5 -> "Atenção à ingestão calórica e proteína para recuperação."
            bmi in 18.5..24.9 -> "Mantenha consistência de sono e hidratação para progredir."
            bmi in 25.0..29.9 -> "Priorize passos diários e refeições de alta saciedade."
            else -> "Progressão gradual e técnica impecável para proteger articulações."
        }

        val weeklyLoad = week * 10 + profile.daysPerWeek * 6 + profile.level.ordinal * 8
        return "$bmiNote Carga semanal planejada: ~${weeklyLoad}% do máximo atual."
    }
}
