package com.michaelfrancoodev.studentcrud.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelfrancoodev.studentcrud.domain.model.TaskWithCourse
import com.michaelfrancoodev.studentcrud.ui.theme.Error
import com.michaelfrancoodev.studentcrud.ui.theme.PriorityHigh
import com.michaelfrancoodev.studentcrud.ui.theme.PriorityLow
import com.michaelfrancoodev.studentcrud.ui.theme.PriorityMedium
import com.michaelfrancoodev.studentcrud.ui.theme.Warning
import com.michaelfrancoodev.studentcrud.util.DateUtils

/**
 * PendingTasksCard - Shows pending tasks with urgency indicators
 */
@Composable
fun PendingTasksCard(
    tasks: List<TaskWithCourse>,
    overdueCount: Int,
    dueTodayCount: Int,
    onTaskClick: (TaskWithCourse) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with urgency info
            Column {
                Text(
                    text = "Pending Tasks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (overdueCount > 0) {
                        UrgencyBadge(
                            text = "$overdueCount overdue",
                            color = Error
                        )
                    }

                    if (dueTodayCount > 0) {
                        UrgencyBadge(
                            text = "$dueTodayCount due today",
                            color = Warning
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tasks List
            if (tasks.isEmpty()) {
                Text(
                    text = "No pending tasks",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                tasks.forEach { task ->
                    TaskItem(
                        task = task,
                        onClick = { onTaskClick(task) }
                    )

                    if (task != tasks.last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

/**
 * TaskItem - Single task item
 */
@Composable
private fun TaskItem(
    task: TaskWithCourse,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOverdue = task.task.isOverdue

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isOverdue) {
                Error.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Priority indicator
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(getPriorityColor(task.task.priority))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Task info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.task.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (isOverdue) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Overdue",
                            modifier = Modifier.size(16.dp),
                            tint = Error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.courseCode,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if (isOverdue) Error else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = DateUtils.getRelativeTimeString(task.task.dueDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isOverdue) Error else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isOverdue) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            // Priority badge
            Text(
                text = task.task.priority,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(
                        getPriorityColor(task.task.priority),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * UrgencyBadge - Small badge for urgency indicators
 */
@Composable
private fun UrgencyBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

/**
 * Get priority color
 */
private fun getPriorityColor(priority: String): Color {
    return when (priority.lowercase()) {
        "high" -> PriorityHigh
        "medium" -> PriorityMedium
        "low" -> PriorityLow
        else -> PriorityMedium
    }
}
