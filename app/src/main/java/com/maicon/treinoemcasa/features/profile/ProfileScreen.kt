package com.maicon.treinoemcasa.features.profile

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maicon.treinoemcasa.AuthUiState
import com.maicon.treinoemcasa.domain.BodyType
import com.maicon.treinoemcasa.domain.EquipmentType
import com.maicon.treinoemcasa.domain.ExperienceLevel
import com.maicon.treinoemcasa.domain.TrainingGoal
import com.maicon.treinoemcasa.domain.TrainingStyle
import com.maicon.treinoemcasa.domain.UserProfile

@Composable
fun ProfileScreen(
    profile: UserProfile,
    auth: AuthUiState,
    onSave: (UserProfile) -> Unit,
    onSignIn: (String, String) -> Unit,
    onSignUp: (String, String) -> Unit,
    onSignInGuest: () -> Unit,
    onSignOut: () -> Unit,
    onBackup: () -> Unit,
    onRestore: () -> Unit,
    contentPadding: PaddingValues
) {
    var height by remember(profile.heightCm) { mutableStateOf(profile.heightCm.toString()) }
    var weight by remember(profile.weightKg) { mutableStateOf(profile.weightKg.toString()) }
    var selectedBodyType by remember(profile.bodyType) { mutableStateOf(profile.bodyType) }
    var selectedGoal by remember(profile.goal) { mutableStateOf(profile.goal) }
    var selectedStyle by remember(profile.style) { mutableStateOf(profile.style) }
    var selectedLevel by remember(profile.level) { mutableStateOf(profile.level) }
    var daysPerWeek by remember(profile.daysPerWeek) { mutableFloatStateOf(profile.daysPerWeek.toFloat()) }
    var equipment by remember(profile.availableEquipment) {
        mutableStateOf(profile.availableEquipment.ifEmpty { setOf(EquipmentType.NONE) })
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Perfil físico e objetivo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text("Esses dados alimentam o gerador científico do plano de 28 dias.")

        AccountCard(
            auth = auth,
            email = email,
            password = password,
            onEmailChange = { email = it.trim() },
            onPasswordChange = { password = it },
            onSignIn = { onSignIn(email, password) },
            onSignUp = { onSignUp(email, password) },
            onSignInGuest = onSignInGuest,
            onSignOut = onSignOut,
            onBackup = onBackup,
            onRestore = onRestore
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it.filter(Char::isDigit).take(3) },
            label = { Text("Altura (cm)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it.filter(Char::isDigit).take(3) },
            label = { Text("Peso (kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        EnumFilterRow(
            title = "Tipo corporal",
            values = BodyType.entries,
            selected = selectedBodyType,
            labelOf = { it.label },
            onSelect = { selectedBodyType = it }
        )

        EnumFilterRow(
            title = "Objetivo principal",
            values = TrainingGoal.entries,
            selected = selectedGoal,
            labelOf = { it.label },
            onSelect = { selectedGoal = it }
        )

        EnumFilterRow(
            title = "Estilo de treino",
            values = TrainingStyle.entries,
            selected = selectedStyle,
            labelOf = { it.label },
            onSelect = { selectedStyle = it }
        )

        EnumFilterRow(
            title = "Nível atual",
            values = ExperienceLevel.entries,
            selected = selectedLevel,
            labelOf = { it.label },
            onSelect = { selectedLevel = it }
        )

        Card {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Dias de treino por semana: ${daysPerWeek.toInt()}")
                Slider(
                    value = daysPerWeek,
                    onValueChange = { daysPerWeek = it },
                    valueRange = 3f..7f,
                    steps = 3
                )
            }
        }

        Text("Equipamentos disponíveis")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EquipmentType.entries.forEach { option ->
                FilterChip(
                    selected = option in equipment,
                    onClick = {
                        equipment = toggleEquipment(equipment, option)
                    },
                    label = { Text(option.label) }
                )
            }
        }

        Button(
            onClick = {
                val heightCm = height.toIntOrNull()?.coerceIn(120, 230) ?: profile.heightCm
                val weightKg = weight.toIntOrNull()?.coerceIn(35, 250) ?: profile.weightKg
                onSave(
                    UserProfile(
                        heightCm = heightCm,
                        weightKg = weightKg,
                        bodyType = selectedBodyType,
                        goal = selectedGoal,
                        style = selectedStyle,
                        level = selectedLevel,
                        daysPerWeek = daysPerWeek.toInt(),
                        availableEquipment = equipment
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar perfil")
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun AccountCard(
    auth: AuthUiState,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    onSignInGuest: () -> Unit,
    onSignOut: () -> Unit,
    onBackup: () -> Unit,
    onRestore: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Conta e backup em nuvem", style = MaterialTheme.typography.titleMedium)

            if (!auth.cloudAvailable) {
                Text(
                    "Firebase não configurado no projeto. Defina as chaves no Gradle para habilitar login e backup.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (auth.isAuthenticated) {
                Text(
                    text = if (auth.isAnonymous) {
                        "Sessão anônima ativa"
                    } else {
                        "Logado como: ${auth.email ?: auth.uid.orEmpty()}"
                    }
                )

                if (!auth.lastSyncAt.isNullOrBlank()) {
                    Text("Última sincronização: ${auth.lastSyncAt}", style = MaterialTheme.typography.bodySmall)
                }

                Button(
                    onClick = onBackup,
                    enabled = !auth.isBusy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fazer backup agora")
                }

                OutlinedButton(
                    onClick = onRestore,
                    enabled = !auth.isBusy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restaurar da nuvem")
                }

                OutlinedButton(
                    onClick = onSignOut,
                    enabled = !auth.isBusy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sair")
                }
            } else {
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Senha") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = onSignIn,
                    enabled = !auth.isBusy && email.isNotBlank() && password.length >= 6,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar")
                }

                OutlinedButton(
                    onClick = onSignUp,
                    enabled = !auth.isBusy && email.isNotBlank() && password.length >= 6,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Criar conta")
                }

                OutlinedButton(
                    onClick = onSignInGuest,
                    enabled = !auth.isBusy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar como convidado")
                }
            }

            if (!auth.statusMessage.isNullOrBlank()) {
                Text(auth.statusMessage, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun <T> EnumFilterRow(
    title: String,
    values: Iterable<T>,
    selected: T,
    labelOf: (T) -> String,
    onSelect: (T) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            values.forEach { value ->
                FilterChip(
                    selected = value == selected,
                    onClick = { onSelect(value) },
                    label = { Text(labelOf(value)) }
                )
            }
        }
    }
}

private fun toggleEquipment(
    current: Set<EquipmentType>,
    option: EquipmentType
): Set<EquipmentType> {
    return when (option) {
        EquipmentType.NONE -> setOf(EquipmentType.NONE)
        else -> {
            val withoutNone = current - EquipmentType.NONE
            val next = if (option in withoutNone) {
                withoutNone - option
            } else {
                withoutNone + option
            }
            if (next.isEmpty()) setOf(EquipmentType.NONE) else next
        }
    }
}
