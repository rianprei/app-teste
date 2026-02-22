package com.maicon.treinoemcasa

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maicon.treinoemcasa.features.challenges.ChallengesScreen
import com.maicon.treinoemcasa.features.exercises.ExerciseDetailScreen
import com.maicon.treinoemcasa.features.exercises.ExerciseListScreen
import com.maicon.treinoemcasa.features.home.HomeScreen
import com.maicon.treinoemcasa.features.plan.PlanScreen
import com.maicon.treinoemcasa.features.profile.ProfileScreen

private sealed class AppRoute(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Home : AppRoute("home", "Início", Icons.Default.Home)
    data object Profile : AppRoute("profile", "Perfil", Icons.Default.Person)
    data object Exercises : AppRoute("exercises", "Exercícios", Icons.Default.FitnessCenter)
    data object Plan : AppRoute("plan", "Plano 28D", Icons.Default.List)
    data object Challenges : AppRoute("challenges", "Desafios", Icons.Default.CheckCircle)
}

private val rootRoutes = listOf(
    AppRoute.Home,
    AppRoute.Profile,
    AppRoute.Exercises,
    AppRoute.Plan,
    AppRoute.Challenges
)

@Composable
fun TreinoApp(viewModel: AppViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route.orEmpty()
    val showBottomBar = !currentRoute.startsWith("exercise/")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    rootRoutes.forEach { route ->
                        val selected = currentRoute == route.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(route.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(route.icon, contentDescription = route.label) },
                            label = { Text(route.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Home.route,
            modifier = Modifier
        ) {
            composable(AppRoute.Home.route) {
                HomeScreen(
                    profile = uiState.profile,
                    plan = uiState.plan,
                    streakDays = uiState.streakDays,
                    completedWorkouts = uiState.completedWorkouts,
                    exerciseCount = uiState.exercises.size,
                    cloudStatus = uiState.auth.statusMessage,
                    onGeneratePlan = viewModel::generatePlan,
                    onOpenPlan = { navController.navigate(AppRoute.Plan.route) },
                    contentPadding = innerPadding
                )
            }
            composable(AppRoute.Profile.route) {
                ProfileScreen(
                    profile = uiState.profile,
                    auth = uiState.auth,
                    onSave = viewModel::saveProfile,
                    onSignIn = viewModel::signIn,
                    onSignUp = viewModel::signUp,
                    onSignInGuest = viewModel::signInGuest,
                    onSignOut = viewModel::signOut,
                    onBackup = viewModel::backupToCloud,
                    onRestore = viewModel::restoreFromCloud,
                    contentPadding = innerPadding
                )
            }
            composable(AppRoute.Exercises.route) {
                ExerciseListScreen(
                    exercises = uiState.filteredExercises,
                    filters = uiState.filters,
                    onQueryChange = viewModel::setQuery,
                    onFocusFilterChange = viewModel::setFocusFilter,
                    onLevelFilterChange = viewModel::setLevelFilter,
                    onEquipmentFilterChange = viewModel::setEquipmentFilter,
                    onClearFilters = viewModel::clearFilters,
                    onExerciseClick = { exerciseId ->
                        navController.navigate("exercise/$exerciseId")
                    },
                    contentPadding = innerPadding
                )
            }
            composable(
                route = "exercise/{exerciseId}",
                arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
            ) { backStack ->
                val exerciseId = backStack.arguments?.getString("exerciseId").orEmpty()
                ExerciseDetailScreen(
                    exercise = viewModel.findExerciseById(exerciseId),
                    onBack = { navController.popBackStack() },
                    contentPadding = innerPadding,
                    resolveExerciseName = { id -> viewModel.findExerciseById(id)?.name ?: id }
                )
            }
            composable(AppRoute.Plan.route) {
                PlanScreen(
                    plan = uiState.plan,
                    exerciseNameById = { id -> viewModel.findExerciseById(id)?.name ?: id },
                    onGeneratePlan = viewModel::generatePlan,
                    contentPadding = innerPadding
                )
            }
            composable(AppRoute.Challenges.route) {
                ChallengesScreen(
                    plan = uiState.plan,
                    streakDays = uiState.streakDays,
                    completedWorkouts = uiState.completedWorkouts,
                    lastCompletedDate = uiState.lastCompletedDate,
                    allExercises = uiState.exercises,
                    workoutLogs = uiState.workoutLogs,
                    onMarkDone = viewModel::markTodayWorkoutDone,
                    onReset = viewModel::resetProgress,
                    onAddWorkoutLog = viewModel::addWorkoutLog,
                    resolveExerciseName = { id -> viewModel.findExerciseById(id)?.name ?: id },
                    contentPadding = innerPadding
                )
            }
        }
    }
}
