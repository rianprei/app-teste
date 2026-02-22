package com.maicon.treinoemcasa.features.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.maicon.treinoemcasa.domain.Exercise

@Composable
fun ExerciseDetailScreen(
    exercise: Exercise?,
    onBack: () -> Unit,
    contentPadding: PaddingValues,
    resolveExerciseName: (String) -> String = { it }
) {
    if (exercise == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Exercício não encontrado.")
            Button(onClick = onBack) { Text("Voltar") }
        }
        return
    }

    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedButton(onClick = onBack) {
                Text("Voltar")
            }
        }

        item {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(exercise.description)
        }

        item {
            AsyncImage(
                model = exercise.homeImageUrl,
                contentDescription = "Imagem de ${exercise.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                error = painterResource(android.R.drawable.ic_menu_report_image)
            )
            Text(
                text = exercise.homeImageHint,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        item {
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Classificação", style = MaterialTheme.typography.titleSmall)
                    AssistChip(onClick = {}, label = { Text(exercise.level.label) })
                    AssistChip(onClick = {}, label = { Text("Evidência ${exercise.evidenceLevel.label}") })
                    Text("Padrão: ${exercise.movementPattern.label}")
                    Text("Foco: ${exercise.focusAreas.joinToString { it.label }}")
                    Text("Equipamento: ${exercise.equipment.joinToString { it.label }}")
                }
            }
        }

        item {
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Como fazer", style = MaterialTheme.typography.titleSmall)
                    exercise.steps.forEachIndexed { index, step ->
                        Text(text = "${index + 1}. $step")
                    }
                }
            }
        }

        item {
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Músculos ativados", style = MaterialTheme.typography.titleSmall)
                    Text("Primários: ${exercise.musclesPrimary.joinToString { it.label }}")
                    Text("Secundários: ${exercise.musclesSecondary.joinToString { it.label }}")
                    BodyActivationDiagram(
                        primary = exercise.musclesPrimary,
                        secondary = exercise.musclesSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (exercise.regressions.isNotEmpty() || exercise.progressions.isNotEmpty()) {
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Progressões e regressões", style = MaterialTheme.typography.titleSmall)

                        if (exercise.regressions.isNotEmpty()) {
                            Text(
                                text = "Regressões: ${exercise.regressions.joinToString { resolveExerciseName(it) }}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        if (exercise.progressions.isNotEmpty()) {
                            Text(
                                text = "Progressões: ${exercise.progressions.joinToString { resolveExerciseName(it) }}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        item {
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Vídeo embutido", style = MaterialTheme.typography.titleSmall)
                    EmbeddedVideoPlayer(
                        embedUrl = exercise.videoEmbedUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    OutlinedButton(
                        onClick = { uriHandler.openUri(exercise.videoUrl) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Abrir vídeo no navegador")
                    }
                }
            }
        }

        item {
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Base científica", style = MaterialTheme.typography.titleSmall)
                    Text(exercise.scienceTip)
                    Text("Referências:", style = MaterialTheme.typography.labelLarge)
                    exercise.scienceReferences.forEach { ref ->
                        Text("• $ref", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
