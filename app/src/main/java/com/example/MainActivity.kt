package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.i18n.AppStrings
import com.example.model.AppLanguage
import com.example.ui.components.MysticTopBar
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AiToolsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.theme.MysticAITheme
import com.example.viewmodel.AiToolsViewModel
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.QuizViewModel

object MysticRoutes {
    const val HOME = "home"
    const val NOTES = "notes"
    const val AI_TOOLS = "ai_tools"
    const val QUIZ = "quiz"
    const val ADMIN = "admin"
}

private data class NavDestination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MysticAiApp()
        }
    }
}

@Composable
fun MysticAiApp(
    appViewModel: AppViewModel = viewModel(),
    aiToolsViewModel: AiToolsViewModel = viewModel(),
    quizViewModel: QuizViewModel = viewModel()
) {
    val preferences by appViewModel.preferences.collectAsStateWithLifecycle()
    val allNotes by appViewModel.allNotes.collectAsStateWithLifecycle()
    val filteredNotes by appViewModel.filteredNotes.collectAsStateWithLifecycle()
    val filterState by appViewModel.filterState.collectAsStateWithLifecycle()
    val recentQuizzes by appViewModel.recentQuizzes.collectAsStateWithLifecycle()
    val adminPinError by appViewModel.adminPinError.collectAsStateWithLifecycle()
    val statusMessage by appViewModel.statusMessage.collectAsStateWithLifecycle()

    val strings = remember(preferences.language) { AppStrings.get(preferences.language) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: MysticRoutes.HOME
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(statusMessage) {
        val msg = statusMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            appViewModel.clearStatusMessage()
        }
    }

    val destinations = remember(strings) {
        listOf(
            NavDestination(
                route = MysticRoutes.HOME,
                label = strings.navHome,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                testTag = "nav_home"
            ),
            NavDestination(
                route = MysticRoutes.NOTES,
                label = strings.navNotes,
                selectedIcon = Icons.Filled.MenuBook,
                unselectedIcon = Icons.Outlined.MenuBook,
                testTag = "nav_notes"
            ),
            NavDestination(
                route = MysticRoutes.AI_TOOLS,
                label = strings.navAiTools,
                selectedIcon = Icons.Filled.AutoAwesome,
                unselectedIcon = Icons.Outlined.AutoAwesome,
                testTag = "nav_ai_tools"
            ),
            NavDestination(
                route = MysticRoutes.QUIZ,
                label = strings.navQuiz,
                selectedIcon = Icons.Filled.Quiz,
                unselectedIcon = Icons.Outlined.Quiz,
                testTag = "nav_quiz"
            ),
            NavDestination(
                route = MysticRoutes.ADMIN,
                label = strings.navAdmin,
                selectedIcon = Icons.Filled.AdminPanelSettings,
                unselectedIcon = Icons.Outlined.AdminPanelSettings,
                testTag = "nav_admin"
            )
        )
    }

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    MysticAITheme(
        darkTheme = preferences.isDarkTheme,
        isBengali = preferences.language == AppLanguage.BN
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useNavRail = maxWidth >= 600.dp

            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets.safeDrawing,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    MysticTopBar(
                        strings = strings,
                        language = preferences.language,
                        isAdminUnlocked = preferences.isAdminUnlocked,
                        onToggleLanguage = { appViewModel.toggleLanguage() },
                        onLogoClick = { navigateTo(MysticRoutes.HOME) }
                    )
                },
                bottomBar = {
                    if (!useNavRail) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 4.dp
                        ) {
                            destinations.forEach { dest ->
                                val selected = currentRoute == dest.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { navigateTo(dest.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                            contentDescription = dest.label
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = dest.label,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.testTag(dest.testTag)
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (useNavRail) {
                        NavigationRail(
                            containerColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            destinations.forEach { dest ->
                                val selected = currentRoute == dest.route
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { navigateTo(dest.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                            contentDescription = dest.label
                                        )
                                    },
                                    label = { Text(dest.label, maxLines = 1) },
                                    colors = NavigationRailItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.testTag(dest.testTag)
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                        NavHost(
                            navController = navController,
                            startDestination = MysticRoutes.HOME
                        ) {
                            composable(MysticRoutes.HOME) {
                                HomeScreen(
                                    strings = strings,
                                    language = preferences.language,
                                    preferredClass = preferences.preferredClass,
                                    notes = allNotes,
                                    recentQuizzes = recentQuizzes,
                                    onSelectPreferredClass = { cls ->
                                        appViewModel.setPreferredClass(cls)
                                        quizViewModel.setClassLevel(cls)
                                    },
                                    onSubmitHomeQuestion = { question ->
                                        aiToolsViewModel.openAssistantWithPrompt(question, null)
                                        aiToolsViewModel.sendAssistantMessage(
                                            classLevel = preferences.preferredClass,
                                            language = preferences.language
                                        )
                                        navigateTo(MysticRoutes.AI_TOOLS)
                                    },
                                    onOpenSubjectNotes = { subject ->
                                        appViewModel.applyQuickFilter(
                                            classLevel = null,
                                            subject = subject
                                        )
                                        navigateTo(MysticRoutes.NOTES)
                                    },
                                    onOpenNotesTab = {
                                        appViewModel.applyQuickFilter(classLevel = null, subject = null)
                                        navigateTo(MysticRoutes.NOTES)
                                    },
                                    onOpenClassLessons = {
                                        appViewModel.applyQuickFilter(
                                            classLevel = preferences.preferredClass,
                                            subject = null
                                        )
                                        navigateTo(MysticRoutes.NOTES)
                                    },
                                    onOpenAiTool = { tab ->
                                        aiToolsViewModel.selectTab(tab)
                                        navigateTo(MysticRoutes.AI_TOOLS)
                                    },
                                    onOpenQuizTab = {
                                        quizViewModel.setClassLevel(preferences.preferredClass)
                                        navigateTo(MysticRoutes.QUIZ)
                                    }
                                )
                            }

                            composable(MysticRoutes.NOTES) {
                                NotesScreen(
                                    strings = strings,
                                    language = preferences.language,
                                    notes = filteredNotes,
                                    filterState = filterState,
                                    onSearchChange = appViewModel::updateSearchQuery,
                                    onClassFilterChange = appViewModel::setClassFilter,
                                    onSubjectFilterChange = appViewModel::setSubjectFilter,
                                    onToggleBookmarkedOnly = appViewModel::toggleBookmarkedOnly,
                                    onToggleBookmark = appViewModel::toggleBookmark,
                                    onAskAiAboutNote = { note ->
                                        val prompt = if (preferences.language == AppLanguage.BN) {
                                            "দয়া করে \"${note.localizedTitle(AppLanguage.BN)}\" বিষয়টি সহজভাবে বুঝিয়ে দাও।"
                                        } else {
                                            "Explain \"${note.localizedTitle(AppLanguage.EN)}\" with clear examples."
                                        }
                                        aiToolsViewModel.openAssistantWithPrompt(prompt, note.subject)
                                        navigateTo(MysticRoutes.AI_TOOLS)
                                    },
                                    onPracticeQuizFromNote = { note ->
                                        quizViewModel.prefillAndGenerate(
                                            classLevel = note.classLevel,
                                            subject = note.subject,
                                            topic = note.localizedTitle(preferences.language),
                                            language = preferences.language
                                        )
                                        navigateTo(MysticRoutes.QUIZ)
                                    }
                                )
                            }

                            composable(MysticRoutes.AI_TOOLS) {
                                AiToolsScreen(
                                    strings = strings,
                                    language = preferences.language,
                                    preferredClass = preferences.preferredClass,
                                    viewModel = aiToolsViewModel
                                )
                            }

                            composable(MysticRoutes.QUIZ) {
                                QuizScreen(
                                    strings = strings,
                                    language = preferences.language,
                                    quizViewModel = quizViewModel,
                                    notesRepository = appViewModel.notesRepository,
                                    recentQuizzes = recentQuizzes
                                )
                            }

                            composable(MysticRoutes.ADMIN) {
                                AdminScreen(
                                    strings = strings,
                                    language = preferences.language,
                                    isAdminUnlocked = preferences.isAdminUnlocked,
                                    isDarkTheme = preferences.isDarkTheme,
                                    adminPinError = adminPinError,
                                    notes = allNotes,
                                    onToggleDarkTheme = appViewModel::setDarkTheme,
                                    onUnlockAdmin = { pin ->
                                        appViewModel.unlockAdmin(pin, strings.invalidPinError)
                                    },
                                    onLockAdmin = appViewModel::lockAdmin,
                                    onSaveNote = appViewModel::saveNote,
                                    onDeleteNote = appViewModel::deleteNote,
                                    onRestoreStarterNotes = appViewModel::restoreStarterNotes
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
