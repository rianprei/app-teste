package com.maicon.treinoemcasa.features.challenges

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maicon.treinoemcasa.domain.Exercise
import com.maicon.treinoemcasa.domain.TrainingPlan
import com.maicon.treinoemcasa.domain.WorkoutLogEntry
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

private data class ConsistencyTier(
    val title: String,
    val targetDays: Int,
    val description: String
)

@Composable
fun ChallengesScreen(
    plan: TrainingPlan?,
    streakDays: Int,
    completedWorkouts: Int,
    lastCompletedDate: String?,
    allExercises: List<Exercise>,
    workoutLogs: List<WorkoutLogEntry>,
    onMarkDone: () -> Unit,
    onReset: () -> Unit,
    onAddWorkoutLog: (String, Int, Int, Double, Int) -> Unit,
    resolveExerciseName: (String) -> String,
    contentPadding: PaddingValues
) {
    val tiers = listOf(
        ConsistencyTier("Bronze", 7, "Concluir 7 dias de treino consistentes."),
        ConsistencyTier("Prata", 14, "Manter duas semanas sem quebrar ritmo."),
        ConsistencyTier("Ouro", 21, "Alta aderência com foco em técnica."),
        ConsistencyTier("Elite 28D", 28, "Completar o ciclo inteiro do plano.")
    )

    val planChallenges = plan?.days
        ?.filter { !it.isRestDay }
        ?.map { "Dia ${it.dayNumber}: ${it.challenge}" }
        .orEmpty()

    val weeklyEvolution = remember(workoutLogs) { buildWeeklyEvolution(workoutLogs) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Desafios e histórico",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            ConsistencyCard(
                streakDays = streakDays,
                completedWorkouts = completedWorkouts,
                lastCompletedDate = lastCompletedDate,
                onMarkDone = onMarkDone,
                onReset = onReset
            )
        }

        item {
            SessionLogForm(
                allExercises = allExercises,
                onSave = onAddWorkoutLog
            )
        }

        item {
            WeeklyEvolutionChart(data = weeklyEvolution)
        }

        items(tiers) { tier ->
            val progress = (streakDays.toFloat() / tier.targetDays.toFloat()).coerceIn(0f, 1f)
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("${tier.title} - ${tier.targetDays} dias", fontWeight = FontWeight.SemiBold)
                    Text(tier.description)
                    LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
                    Text("${(progress * 100).toInt()}% concluído")
                }
            }
        }

        item {
            Text("Histórico de sessões", style = MaterialTheme.typography.titleMedium)
        }

        if (workoutLogs.isEmpty()) {
            item {
                Text("Ainda não há sessões registradas. Use o formulário acima.")
            }
        } else {
            items(workoutLogs.take(40), key = { it.id }) { log ->
                Card {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(resolveExerciseName(log.exerciseId), fontWeight = FontWeight.SemiBold)
                        Text("Data: ${log.date}")
                        Text("Séries: ${log.completedSets} | Reps: ${log.completedReps} | Carga: ${"%.1f".format(log.loadKg)} kg | RPE: ${log.rpe}")
                        val volume = (log.completedSets * log.completedReps * if (log.loadKg > 0.0) log.loadKg else 1.0)
                        Text("Volume estimado: ${"%.1f".format(volume)}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        item {
            Text("Desafios planejados do ciclo", style = MaterialTheme.typography.titleMedium)
        }

        if (planChallenges.isEmpty()) {
            item {
                Text("Gere um plano de 28 dias para habilitar desafios diários.")
            }
        } else {
            items(planChallenges.take(14)) { item ->
                Card {
                    Text(
                        text = item,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ConsistencyCard(
    streakDays: Int,
    completedWorkouts: Int,
    lastCompletedDate: String?,
    onMarkDone: () -> Unit,
    onReset: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Streak atual: $streakDays dia(s)", style = MaterialTheme.typography.titleMedium)
            Text("Treinos concluídos: $completedWorkouts")
            if (!lastCompletedDate.isNullOrBlank()) {
                Text("Último treino registrado: $lastCompletedDate")
            }
            Button(onClick = onMarkDone, modifier = Modifier.fillMaxWidth()) {
                Text("Marcar treino de hoje como concluído")
            }
            OutlinedButton(onClick = onReset, modifier = Modifier.fillMaxWidth()) {
                Text("Resetar progresso")
            }
        }
    }
}

@Composable
private fun SessionLogForm(
    allExercises: List<Exercise>,
    onSave: (String, Int, Int, Double, Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val sortedExercises = remember(allExercises) { allExercises.sortedBy { it.name } }
    val suggestions = remember(query, sortedExercises) {
        val q = query.trim()
        if (q.isBlank()) sortedExercises.take(10)
        else sortedExercises.filter { it.name.contains(q, ignoreCase = true) }.take(10)
    }

    var selectedExerciseId by remember { mutableStateOf(sortedExercises.firstOrNull()?.id.orEmpty()) }
    var selectedExerciseName by remember { mutableStateOf(sortedExercises.firstOrNull()?.name.orEmpty()) }
    var sets by remember { mutableStateOf("3") }
    var reps by remember { mutableStateOf("10") }
    var loadKg by remember { mutableStateOf("0") }
    var rpe by remember { mutableStateOf("7") }

    Card {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Registrar sessão detalhada", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar exercício") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (suggestions.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    suggestions.forEach { exercise ->
                        AssistChip(
                            onClick = {
                                selectedExerciseId = exercise.id
                                selectedExerciseName = exercise.name
                                query = exercise.name
                            },
                            label = { Text(exercise.name) }
                        )
                    }
                }
            }

            Text("Selecionado: $selectedExerciseName")

            OutlinedTextField(
                value = sets,
                onValueChange = { sets = it.filter(Char::isDigit).take(2) },
                label = { Text("Séries concluídas") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = reps,
                onValueChange = { reps = it.filter(Char::isDigit).take(3) },
                label = { Text("Repetições por série") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = loadKg,
                onValueChange = { loadKg = it.filter { c -> c.isDigit() || c == '.' }.take(6) },
                label = { Text("Carga usada (kg)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = rpe,
                onValueChange = { rpe = it.filter(Char::isDigit).take(2) },
                label = { Text("RPE (1-10)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    val parsedSets = sets.toIntOrNull() ?: 0
                    val parsedReps = reps.toIntOrNull() ?: 0
                    val parsedLoad = loadKg.toDoubleOrNull() ?: 0.0
                    val parsedRpe = rpe.toIntOrNull() ?: 7
                    onSave(selectedExerciseId, parsedSets, parsedReps, parsedLoad, parsedRpe)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedExerciseId.isNotBlank()
            ) {
                Text("Salvar sessão")
            }
        }
    }
}

private fun buildWeeklyEvolution(logs: List<WorkoutLogEntry>): List<WeeklyEvolutionPoint> {
    if (logs.isEmpty()) return emptyList()

    val weekFields = WeekFields.of(Locale.getDefault())

    data class WeekKey(val year: Int, val week: Int)

    val grouped = logs.groupBy { entry ->
        val date = runCatching { LocalDate.parse(entry.date) }.getOrElse { LocalDate.now() }
        WeekKey(
            year = date.get(weekFields.weekBasedYear()),
            week = date.get(weekFields.weekOfWeekBasedYear())
        )
    }

    return grouped.entries
        .sortedWith(compareBy({ it.key.year }, { it.key.week }))
        .takeLast(8)
        .map { (key, entries) ->
            val totalReps = entries.sumOf { it.completedReps * it.completedSets }
            val totalVolume = entries.sumOf { entry ->
                val effectiveLoad = if (entry.loadKg > 0.0) entry.loadKg else 1.0
                effectiveLoad * entry.completedReps * entry.completedSets
            }
            WeeklyEvolutionPoint(
                label = "S${key.week}",
                totalVolume = totalVolume,
                totalReps = totalReps,
                sessions = entries.size
            )
        }
}
