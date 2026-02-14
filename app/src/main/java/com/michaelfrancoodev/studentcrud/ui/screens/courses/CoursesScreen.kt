package com.michaelfrancoodev.studentcrud.ui.screens.courses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.michaelfrancoodev.studentcrud.ui.components.EmptyCoursesState
import com.michaelfrancoodev.studentcrud.ui.components.ErrorState
import com.michaelfrancoodev.studentcrud.ui.components.LoadingState
import com.michaelfrancoodev.studentcrud.ui.components.SimpleFAB
import com.michaelfrancoodev.studentcrud.ui.screens.courses.components.AddCourseDialog
import com.michaelfrancoodev.studentcrud.ui.screens.courses.components.CourseCard
import com.michaelfrancoodev.studentcrud.ui.screens.courses.components.CourseOverviewCard

/**
 * CoursesScreen - Main courses list screen
 * Shows all courses for current semester with overview stats
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
    viewModel: CourseViewModel,
    onNavigateToCourseDetail: (Int) -> Unit,
    onNavigateToAddCourse: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Handle pull to refresh
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.refresh()
        }
    }

    // Reset refresh state when loading completes
    LaunchedEffect(uiState) {
        if (uiState !is CourseUiState.Loading) {
            pullToRefreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Courses") },
                actions = {
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        floatingActionButton = {
            SimpleFAB(
                icon = Icons.Filled.Add,
                contentDescription = "Add Course",
                onClick = { showAddDialog = true }
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
                is CourseUiState.Loading -> {
                    LoadingState(message = "Loading courses...")
                }

                is CourseUiState.Success -> {
                    if (state.courses.isEmpty()) {
                        EmptyCoursesState(
                            onAddCourse = { showAddDialog = true }
                        )
                    } else {
                        CoursesContent(
                            state = state,
                            onCourseClick = onNavigateToCourseDetail,
                            onDeleteCourse = { courseId ->
                                viewModel.deleteCourse(courseId)
                            }
                        )
                    }
                }

                is CourseUiState.Error -> {
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

    // Add Course Dialog
    if (showAddDialog) {
        AddCourseDialog(
            onDismiss = { showAddDialog = false },
            onSave = { course, schedules ->
                viewModel.saveCourse(course, schedules)
                showAddDialog = false
            }
        )
    }
}

/**
 * CoursesContent - Main content for courses list
 */
@Composable
private fun CoursesContent(
    state: CourseUiState.Success,
    onCourseClick: (Int) -> Unit,
    onDeleteCourse: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overview Card
        item {
            CourseOverviewCard(
                totalCourses = state.totalCourses,
                totalCredits = state.totalCredits,
                currentSemester = state.currentSemester
            )
        }

        // Courses List
        items(
            items = state.courses,
            key = { course -> course.id }
        ) { course ->
            CourseCard(
                course = course,
                onClick = { onCourseClick(course.id) },
                onDelete = { onDeleteCourse(course.id) }
            )
        }
    }
}
