package com.maicon.treinoemcasa.features.exercises

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maicon.treinoemcasa.domain.MuscleGroup
import com.maicon.treinoemcasa.domain.MuscleGroup.*

@Composable
fun BodyActivationDiagram(
    primary: Set<MuscleGroup>,
    secondary: Set<MuscleGroup>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Mapa de ativação muscular",
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BodyFigure(
                modifier = Modifier.weight(1f),
                title = "Frontal",
                front = true,
                primary = primary,
                secondary = secondary
            )
            BodyFigure(
                modifier = Modifier.weight(1f),
                title = "Posterior",
                front = false,
                primary = primary,
                secondary = secondary
            )
        }
        Text(
            text = "Cor forte = músculo primário | Cor média = secundário",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun BodyFigure(
    modifier: Modifier,
    title: String,
    front: Boolean,
    primary: Set<MuscleGroup>,
    secondary: Set<MuscleGroup>
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val inactive = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.28f)

    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)) {
                val w = size.width
                val h = size.height
                val centerX = w / 2f

                fun tint(intensity: Float): Color = when {
                    intensity >= 1f -> primaryColor
                    intensity >= 0.6f -> secondaryColor
                    else -> inactive
                }

                fun intensity(groups: Set<MuscleGroup>): Float {
                    return when {
                        groups.any { it in primary } -> 1f
                        groups.any { it in secondary } -> 0.65f
                        else -> 0.15f
                    }
                }

                val headSize = h * 0.10f
                drawRoundRect(
                    color = inactive,
                    topLeft = Offset(centerX - headSize / 2f, h * 0.02f),
                    size = Size(headSize, headSize),
                    cornerRadius = CornerRadius(headSize / 2f)
                )

                if (front) {
                    drawRoundRect(
                        color = tint(intensity(setOf(SHOULDERS))),
                        topLeft = Offset(centerX - w * 0.22f, h * 0.16f),
                        size = Size(w * 0.44f, h * 0.08f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(CHEST))),
                        topLeft = Offset(centerX - w * 0.17f, h * 0.25f),
                        size = Size(w * 0.34f, h * 0.13f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(CORE, OBLIQUES, HIP_FLEXORS))),
                        topLeft = Offset(centerX - w * 0.14f, h * 0.39f),
                        size = Size(w * 0.28f, h * 0.16f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(BICEPS, TRICEPS, FOREARMS, SHOULDERS))),
                        topLeft = Offset(centerX - w * 0.34f, h * 0.22f),
                        size = Size(w * 0.12f, h * 0.30f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(BICEPS, TRICEPS, FOREARMS, SHOULDERS))),
                        topLeft = Offset(centerX + w * 0.22f, h * 0.22f),
                        size = Size(w * 0.12f, h * 0.30f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(QUADS, GLUTES))),
                        topLeft = Offset(centerX - w * 0.17f, h * 0.57f),
                        size = Size(w * 0.14f, h * 0.22f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(QUADS, GLUTES))),
                        topLeft = Offset(centerX + w * 0.03f, h * 0.57f),
                        size = Size(w * 0.14f, h * 0.22f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(CALVES))),
                        topLeft = Offset(centerX - w * 0.15f, h * 0.80f),
                        size = Size(w * 0.10f, h * 0.16f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(CALVES))),
                        topLeft = Offset(centerX + w * 0.05f, h * 0.80f),
                        size = Size(w * 0.10f, h * 0.16f),
                        cornerRadius = CornerRadius(14f)
                    )
                } else {
                    drawRoundRect(
                        color = tint(intensity(setOf(UPPER_BACK, SHOULDERS, LATS))),
                        topLeft = Offset(centerX - w * 0.20f, h * 0.18f),
                        size = Size(w * 0.40f, h * 0.16f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(LATS, LOWER_BACK))),
                        topLeft = Offset(centerX - w * 0.16f, h * 0.35f),
                        size = Size(w * 0.32f, h * 0.17f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(TRICEPS, BICEPS, FOREARMS, SHOULDERS))),
                        topLeft = Offset(centerX - w * 0.34f, h * 0.22f),
                        size = Size(w * 0.12f, h * 0.30f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(TRICEPS, BICEPS, FOREARMS, SHOULDERS))),
                        topLeft = Offset(centerX + w * 0.22f, h * 0.22f),
                        size = Size(w * 0.12f, h * 0.30f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(GLUTES))),
                        topLeft = Offset(centerX - w * 0.15f, h * 0.54f),
                        size = Size(w * 0.30f, h * 0.10f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(HAMSTRINGS))),
                        topLeft = Offset(centerX - w * 0.17f, h * 0.66f),
                        size = Size(w * 0.14f, h * 0.18f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(HAMSTRINGS))),
                        topLeft = Offset(centerX + w * 0.03f, h * 0.66f),
                        size = Size(w * 0.14f, h * 0.18f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(CALVES))),
                        topLeft = Offset(centerX - w * 0.15f, h * 0.84f),
                        size = Size(w * 0.10f, h * 0.13f),
                        cornerRadius = CornerRadius(14f)
                    )
                    drawRoundRect(
                        color = tint(intensity(setOf(CALVES))),
                        topLeft = Offset(centerX + w * 0.05f, h * 0.84f),
                        size = Size(w * 0.10f, h * 0.13f),
                        cornerRadius = CornerRadius(14f)
                    )
                }
            }
        }
    }
}
