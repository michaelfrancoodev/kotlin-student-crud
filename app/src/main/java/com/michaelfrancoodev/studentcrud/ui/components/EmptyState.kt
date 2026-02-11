package com.michaelfrancoodev.studentcrud.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * EmptyState - Reusable empty state component
 * Shows when there's no data to display
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Folder,
    title: String = "No data yet",
    message: String = "Start by adding your first item",
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            if (actionText != null && onActionClick != null) {
                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = onActionClick) {
                    Text(text = actionText)
                }
            }
        }
    }
}

/**
 * EmptyCoursesState - Empty state for courses screen
 */
@Composable
fun EmptyCoursesState(
    modifier: Modifier = Modifier,
    onAddCourse: () -> Unit
) {
    EmptyState(
        modifier = modifier,
        icon = Icons.Outlined.School,
        title = "No courses yet",
        message = "Add your first course to get started with your academic journey",
        actionText = "Add Course",
        onActionClick = onAddCourse
    )
}

/**
 * EmptyTasksState - Empty state for tasks screen
 */
@Composable
fun EmptyTasksState(
    modifier: Modifier = Modifier,
    onAddTask: () -> Unit
) {
    EmptyState(
        modifier = modifier,
        icon = Icons.Outlined.CheckCircle,
        title = "No tasks yet",
        message = "Add assignments and deadlines to track your progress",
        actionText = "Add Task",
        onActionClick = onAddTask
    )
}

/**
 * EmptyTimetableState - Empty state for timetable screen
 */
@Composable
fun EmptyTimetableState(
    modifier: Modifier = Modifier,
    onAddCourse: () -> Unit
) {
    EmptyState(
        modifier = modifier,
        icon = Icons.Outlined.CalendarMonth,
        title = "No classes scheduled",
        message = "Add courses with schedules to see your timetable",
        actionText = "Add Course",
        onActionClick = onAddCourse
    )
}

/**
 * EmptySearchState - Empty state for search results
 */
@Composable
fun EmptySearchState(
    modifier: Modifier = Modifier,
    searchQuery: String = ""
) {
    EmptyState(
        modifier = modifier,
        icon = Icons.Outlined.Folder,
        title = "No results found",
        message = if (searchQuery.isNotEmpty()) {
            "No results for \"$searchQuery\""
        } else {
            "Try a different search term"
        }
    )
}
