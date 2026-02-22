package com.maicon.treinoemcasa.features.exercises

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maicon.treinoemcasa.ExerciseFilters
import com.maicon.treinoemcasa.domain.EquipmentType
import com.maicon.treinoemcasa.domain.Exercise
import com.maicon.treinoemcasa.domain.ExperienceLevel
import com.maicon.treinoemcasa.domain.FocusArea
import coil.compose.AsyncImage

@Composable
fun ExerciseListScreen(
    exercises: List<Exercise>,
    filters: ExerciseFilters,
    onQueryChange: (String) -> Unit,
    onFocusFilterChange: (FocusArea?) -> Unit,
    onLevelFilterChange: (ExperienceLevel?) -> Unit,
    onEquipmentFilterChange: (EquipmentType?) -> Unit,
    onClearFilters: () -> Unit,
    onExerciseClick: (String) -> Unit,
    contentPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Biblioteca de exercícios",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text("Enciclopédia calistênica com progressões, técnica e base científica.")

        OutlinedTextField(
            value = filters.query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar exercício") },
            singleLine = true
        )

        FilterRow(
            title = "Foco",
            allLabel = "Todos",
            options = FocusArea.entries.filter { it != FocusArea.RECOVERY },
            selected = filters.focus,
            labelOf = { it.label },
            onSelect = onFocusFilterChange
        )

        FilterRow(
            title = "Nível",
            allLabel = "Todos",
            options = ExperienceLevel.entries,
            selected = filters.level,
            labelOf = { it.label },
            onSelect = onLevelFilterChange
        )

        FilterRow(
            title = "Equipamento",
            allLabel = "Todos",
            options = EquipmentType.entries,
            selected = filters.equipment,
            labelOf = { it.label },
            onSelect = onEquipmentFilterChange
        )

        Button(onClick = onClearFilters, modifier = Modifier.fillMaxWidth()) {
            Text("Limpar filtros")
        }

        Text("${exercises.size} exercício(s) encontrado(s)")

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(exercises, key = { it.id }) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onOpen = { onExerciseClick(exercise.id) }
                )
            }
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: Exercise,
    onOpen: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = exercise.homeImageUrl,
                contentDescription = exercise.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                error = painterResource(android.R.drawable.ic_menu_report_image)
            )
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(exercise.description)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(exercise.level.label) })
                AssistChip(onClick = {}, label = { Text("Evidência ${exercise.evidenceLevel.label}") })
                AssistChip(
                    onClick = {},
                    label = {
                        Text(exercise.focusAreas.joinToString(" • ") { it.label })
                    }
                )
            }

            Text(
                text = "Músculos primários: ${exercise.musclesPrimary.joinToString { it.label }}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Padrão de movimento: ${exercise.movementPattern.label}",
                style = MaterialTheme.typography.bodySmall
            )

            if (exercise.progressions.isNotEmpty()) {
                Text(
                    text = "Progressões disponíveis: ${exercise.progressions.size}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onOpen) {
                    Text("Ver execução")
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> FilterRow(
    title: String,
    allLabel: String,
    options: Iterable<T>,
    selected: T?,
    labelOf: (T) -> String,
    onSelect: (T?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selected == null,
                onClick = { onSelect(null) },
                label = { Text(allLabel) }
            )
            options.forEach { option ->
                FilterChip(
                    selected = option == selected,
                    onClick = { onSelect(option) },
                    label = { Text(labelOf(option)) }
                )
            }
        }
    }
}
