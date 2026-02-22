package com.maicon.treinoemcasa.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maicon.treinoemcasa.domain.TrainingPlan
import com.maicon.treinoemcasa.domain.UserProfile

@Composable
fun HomeScreen(
    profile: UserProfile,
    plan: TrainingPlan?,
    streakDays: Int,
    completedWorkouts: Int,
    exerciseCount: Int,
    cloudStatus: String?,
    onGeneratePlan: () -> Unit,
    onOpenPlan: () -> Unit,
    contentPadding: PaddingValues
) {
    val bmi = profile.bmi()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Treino em Casa Pro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Plano de 28 dias baseado em perfil físico, objetivo e progressão semanal.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Seu perfil atual", style = MaterialTheme.typography.titleMedium)
                Text("Altura: ${profile.heightCm} cm | Peso: ${profile.weightKg} kg")
                Text("IMC: ${"%.1f".format(bmi)} (${bmiLabel(bmi)})")
                Text("Objetivo: ${profile.goal.label}")
                Text("Estilo de treino: ${profile.style.label}")
                Text("Nível: ${profile.level.label} | Dias/semana: ${profile.daysPerWeek}")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Consistência",
                value = "$streakDays dias",
                subtitle = "$completedWorkouts treinos"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Exercícios",
                value = "${exerciseCount}+",
                subtitle = "Calistenia científica"
            )
        }

        if (!cloudStatus.isNullOrBlank()) {
            Card {
                Text(
                    text = cloudStatus,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Base científica do app", style = MaterialTheme.typography.titleMedium)
                Text("1. Progressão de volume por semana (28 dias divididos em 4 blocos).")
                Text("2. Repetições e descanso ajustados por objetivo (força, hipertrofia, resistência, etc.).")
                Text("3. Recuperação ativa planejada em dias sem treino para reduzir fadiga acumulada.")
            }
        }

        Button(onClick = onGeneratePlan, modifier = Modifier.fillMaxWidth()) {
            Text("Gerar plano de 28 dias")
        }

        if (plan != null) {
            OutlinedButton(onClick = onOpenPlan, modifier = Modifier.fillMaxWidth()) {
                Text("Abrir plano atual")
            }
            Text(
                text = "Plano gerado em: ${plan.generatedAt}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    title: String,
    value: String,
    subtitle: String
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun bmiLabel(bmi: Double): String = when {
    bmi < 18.5 -> "baixo peso"
    bmi < 25.0 -> "normal"
    bmi < 30.0 -> "sobrepeso"
    else -> "obesidade"
}
