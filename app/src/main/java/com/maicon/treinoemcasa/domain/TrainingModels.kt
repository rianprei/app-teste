package com.maicon.treinoemcasa.domain

import java.time.LocalDate

enum class ExperienceLevel(val label: String) {
    BEGINNER("Iniciante"),
    INTERMEDIATE("Intermediário"),
    ADVANCED("Avançado")
}

enum class BodyType(val label: String) {
    ECTOMORPH("Ectomorfo"),
    MESOMORPH("Mesomorfo"),
    ENDOMORPH("Endomorfo")
}

enum class TrainingGoal(val label: String, val scienceBase: String) {
    MUSCLE_GAIN("Hipertrofia", "Faixas de 6-15 repetições com sobrecarga progressiva e recuperação adequada."),
    FAT_LOSS("Emagrecimento", "Combinação de treino resistido + blocos metabólicos melhora gasto energético e manutenção muscular."),
    STRENGTH("Força", "Baixas repetições, séries múltiplas e intervalos longos favorecem adaptações neurais."),
    ENDURANCE("Resistência", "Repetições altas e pausas curtas elevam capacidade de esforço sustentado."),
    RECONDITIONING("Recondicionamento", "Progressão gradual e técnica consistente reduzem risco de lesão em retorno ao treino.")
}

enum class TrainingStyle(val label: String) {
    CALISTHENICS_ONLY("Calistenia pura"),
    MIXED_WITH_EQUIPMENT("Misto com equipamento"),
    HIIT_HOME("HIIT em casa"),
    STRENGTH_FOCUSED("Força com carga")
}

enum class FocusArea(val label: String) {
    CHEST("Peito"),
    BACK("Costas"),
    LEGS("Pernas"),
    ARMS("Braços"),
    ABS("Abdômen"),
    FULL_BODY("Corpo inteiro"),
    MOBILITY("Mobilidade"),
    RECOVERY("Recuperação")
}

enum class MovementPattern(val label: String) {
    HORIZONTAL_PUSH("Empurrar horizontal"),
    VERTICAL_PUSH("Empurrar vertical"),
    HORIZONTAL_PULL("Puxar horizontal"),
    VERTICAL_PULL("Puxar vertical"),
    SQUAT("Agachar"),
    LUNGE("Passada unilateral"),
    HINGE("Hinge de quadril"),
    CORE_ANTI_EXTENSION("Core anti-extensão"),
    CORE_ANTI_ROTATION("Core anti-rotação"),
    CORE_FLEXION("Core flexão"),
    LOCOMOTION("Locomoção"),
    PLYOMETRIC("Pliométrico"),
    SKILL_STATIC("Skill estática"),
    CONDITIONING("Condicionamento"),
    MOBILITY("Mobilidade"),
    MIXED("Misto")
}

enum class EquipmentType(val label: String) {
    NONE("Sem equipamento"),
    DUMBBELL("Halter"),
    BARBELL("Barra olímpica"),
    KETTLEBELL("Kettlebell"),
    RESISTANCE_BAND("Faixa elástica"),
    PULL_UP_BAR("Barra fixa"),
    GYMNASTIC_RINGS("Argolas"),
    PARALLETTES("Paralelas baixas"),
    AB_WHEEL("Roda abdominal"),
    WEIGHT_VEST("Colete de peso"),
    BENCH("Banco"),
    JUMP_ROPE("Corda")
}

enum class EvidenceLevel(val label: String) {
    HIGH("Alta"),
    MODERATE("Moderada"),
    APPLIED("Aplicada")
}

enum class MuscleGroup(val label: String) {

    FULL_BODY("Full Body"),
    CHEST("Peito"),
    BACK("Costas"),
    LEGS("Pernas"),
    ARMS("Braços"),
    SHOULDERS("Ombros"),
    CORE("Core"),
    MOBILITY("Mobilidade"),

    BICEPS("Bíceps"),
    TRICEPS("Tríceps"),
    FOREARMS("Antebraço"),

    UPPER_BACK("Costas Superiores"),
    LOWER_BACK("Costas Inferiores"),
    LATS("Dorsal"),

    GLUTES("Glúteos"),
    QUADS("Quadríceps"),
    HAMSTRINGS("Posterior"),
    CALVES("Panturrilha"),

    OBLIQUES("Oblíquos"),
    HIP_FLEXORS("Flexores do Quadril")
}

data class UserProfile(
    val heightCm: Int = 170,
    val weightKg: Int = 70,
    val bodyType: BodyType = BodyType.MESOMORPH,
    val goal: TrainingGoal = TrainingGoal.MUSCLE_GAIN,
    val style: TrainingStyle = TrainingStyle.CALISTHENICS_ONLY,
    val level: ExperienceLevel = ExperienceLevel.BEGINNER,
    val daysPerWeek: Int = 4,
    val availableEquipment: Set<EquipmentType> = setOf(EquipmentType.NONE)
) {
    fun bmi(): Double {
        val meters = heightCm / 100.0
        return if (meters == 0.0) 0.0 else weightKg / (meters * meters)
    }
}

data class Exercise(
    val id: String,
    val name: String,
    val description: String,
    val level: ExperienceLevel,
    val movementPattern: MovementPattern = MovementPattern.MIXED,
    val focusAreas: Set<FocusArea>,
    val equipment: Set<EquipmentType>,
    val musclesPrimary: Set<MuscleGroup>,
    val musclesSecondary: Set<MuscleGroup>,
    val steps: List<String>,
    val homeImageHint: String,
    val homeImageUrl: String = "",
    val videoUrl: String,
    val videoEmbedUrl: String = "",
    val scienceTip: String,
    val evidenceLevel: EvidenceLevel = EvidenceLevel.MODERATE,
    val scienceReferences: List<String> = emptyList(),
    val regressions: List<String> = emptyList(),
    val progressions: List<String> = emptyList()
)

data class WorkoutPrescription(
    val exerciseId: String,
    val sets: Int,
    val reps: String,
    val restSeconds: Int,
    val loadHint: String
)

data class WorkoutLogEntry(
    val id: String,
    val date: String,
    val exerciseId: String,
    val completedSets: Int,
    val completedReps: Int,
    val loadKg: Double,
    val rpe: Int
)

data class PlanDay(
    val dayNumber: Int,
    val title: String,
    val focus: FocusArea,
    val intensity: String,
    val challenge: String,
    val recoveryTip: String,
    val isRestDay: Boolean,
    val workout: List<WorkoutPrescription>
)

data class TrainingPlan(
    val generatedAt: LocalDate,
    val userProfile: UserProfile,
    val days: List<PlanDay>
)
