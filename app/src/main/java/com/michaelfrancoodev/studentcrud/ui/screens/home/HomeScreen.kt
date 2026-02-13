package com.michaelfrancoodev.studentcrud.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.michaelfrancoodev.studentcrud.ui.components.ErrorState
import com.michaelfrancoodev.studentcrud.ui.components.LoadingState
import com.michaelfrancoodev.studentcrud.ui.screens.home.components.PendingTasksCard
import com.michaelfrancoodev.studentcrud.ui.screens.home.components.StatsCard
import com.michaelfrancoodev.studentcrud.ui.screens.home.components.TodayClassesCard
import com.michaelfrancoodev.studentcrud.ui.screens.home.components.TomorrowClassesCard
import com.michaelfrancoodev.studentcrud.ui.screens.home.components.WelcomeCard

/**
 * HomeScreen - Main dashboard screen
 * Shows welcome message, stats, today's classes, tomorrow's classes, and pending tasks
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCourse: (Int) -> Unit,
    onNavigateToTask: (Int) -> Unit,
    onNavigateToTimetable: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    // Handle pull to refresh
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.refresh()
        }
    }

    // Reset refresh state when loading completes
    LaunchedEffect(uiState) {
        if (uiState !is HomeUiState.Loading) {
            pullToRefreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Soma Smart") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    LoadingState(message = "Loading dashboard...")
                }

                is HomeUiState.Success -> {
                    HomeContent(
                        state = state,
                        onNavigateToCourse = onNavigateToCourse,
                        onNavigateToTask = onNavigateToTask,
                        onNavigateToTimetable = onNavigateToTimetable
                    )
                }

                is HomeUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.refresh() }
                    )
                }
            }

            // Pull to refresh indicator
            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

/**
 * HomeContent - Main content for home screen
 */
@Composable
private fun HomeContent(
    state: HomeUiState.Success,
    onNavigateToCourse: (Int) -> Unit,
    onNavigateToTask: (Int) -> Unit,
    onNavigateToTimetable: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card
        item {
            WelcomeCard(
                student = state.student,
                currentStreak = state.stats.currentStreak
            )
        }

        // Stats Card
        item {
            StatsCard(
                activeCourses = state.stats.activeCourseCount,
                pendingTasks = state.stats.pendingTaskCount,
                totalCredits = state.stats.totalCredits,
                weeklyProgress = state.stats.weeklyProgressPercentage
            )
        }

        // Today's Classes Card
        if (state.stats.todayClasses.isNotEmpty()) {
            item {
                TodayClassesCard(
                    classes = state.stats.todayClasses,
                    onClassClick = { classItem ->
                        // Navigate to course detail
                        val courseId = classItem.schedule.courseId
                        onNavigateToCourse(courseId)
                    },
                    onViewAllClick = onNavigateToTimetable
                )
            }
        }

        // Tomorrow's Classes Card
        if (state.stats.tomorrowClasses.isNotEmpty()) {
            item {
                TomorrowClassesCard(
                    classes = state.stats.tomorrowClasses,
                    onClassClick = { classItem ->
                        // Navigate to course detail
                        val courseId = classItem.schedule.courseId
                        onNavigateToCourse(courseId)
                    }
                )
            }
        }

        // Pending Tasks Card
        if (state.stats.pendingTasks.isNotEmpty()) {
            item {
                PendingTasksCard(
                    tasks = state.stats.pendingTasks,
                    overdueCount = state.stats.overdueTaskCount,
                    dueTodayCount = state.stats.dueTodayTaskCount,
                    onTaskClick = { task ->
                        onNavigateToTask(task.task.id)
                    }
                )
            }
        }
    }
}
