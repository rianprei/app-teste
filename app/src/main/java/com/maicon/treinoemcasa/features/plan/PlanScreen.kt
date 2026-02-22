package com.maicon.treinoemcasa.features.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maicon.treinoemcasa.domain.PlanDay
import com.maicon.treinoemcasa.domain.TrainingPlan

@Composable
fun PlanScreen(
    plan: TrainingPlan?,
    exerciseNameById: (String) -> String,
    onGeneratePlan: () -> Unit,
    contentPadding: PaddingValues
) {
    if (plan == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Plano de 28 dias",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("Ainda não há plano gerado para o perfil atual.")
            Button(onClick = onGeneratePlan, modifier = Modifier.fillMaxWidth()) {
                Text("Gerar plano agora")
            }
        }
        return
    }

    val workoutDays = plan.days.count { !it.isRestDay }
    val restDays = plan.days.count { it.isRestDay }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Plano de 28 dias",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Gerado em ${plan.generatedAt}", style = MaterialTheme.typography.labelLarge)
                    Text("Objetivo: ${plan.userProfile.goal.label}")
                    Text("Estilo: ${plan.userProfile.style.label}")
                    Text("Nível: ${plan.userProfile.level.label}")
                    Text("Treino x descanso: $workoutDays dias de treino | $restDays dias de recuperação")
                }
            }
        }

        items(plan.days, key = { it.dayNumber }) { day ->
            PlanDayCard(day = day, exerciseNameById = exerciseNameById)
        }
    }
}

@Composable
private fun PlanDayCard(
    day: PlanDay,
    exerciseNameById: (String) -> String
) {
    Card {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = day.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text("Foco: ${day.focus.label} | Intensidade: ${day.intensity}")
            Text("Desafio do dia: ${day.challenge}")
            Text("Recuperação: ${day.recoveryTip}")

            if (!day.isRestDay) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                day.workout.forEach { block ->
                    Text(
                        text = "• ${exerciseNameById(block.exerciseId)} - ${block.sets}x ${block.reps} | Descanso ${block.restSeconds}s | ${block.loadHint}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Text("Dia de recuperação ativa.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
