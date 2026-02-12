package com.michaelfrancoodev.studentcrud.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * QuickActionFAB - Expandable FAB with multiple quick actions
 * Used in main screens for quick add operations
 */
@Composable
fun QuickActionFAB(
    modifier: Modifier = Modifier,
    items: List<FABItem>,
    onItemClick: (FABItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        label = "fab_rotation"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Quick action items
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items.forEach { item ->
                    FABItemRow(
                        item = item,
                        onClick = {
                            onItemClick(item)
                            expanded = false
                        }
                    )
                }
            }
        }

        // Main FAB
        FloatingActionButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = if (expanded) Icons.Filled.Close else Icons.Filled.Add,
                contentDescription = if (expanded) "Close" else "Add",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

/**
 * FABItemRow - Single row in expanded FAB menu
 */
@Composable
private fun FABItemRow(
    item: FABItem,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        // Label
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small,
            shadowElevation = 2.dp
        ) {
            Text(
                text = item.label,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Small FAB
        SmallFloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label
            )
        }
    }
}

/**
 * FABItem - Data class for FAB menu items
 */
data class FABItem(
    val id: String,
    val label: String,
    val icon: ImageVector
)

/**
 * SimpleFAB - Simple floating action button (no expansion)
 */
@Composable
fun SimpleFAB(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Add,
    contentDescription: String = "Add",
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

/**
 * Predefined FAB items for common screens
 */
object FABItems {
    val addCourse = FABItem(
        id = "add_course",
        label = "Add Course",
        icon = Icons.Filled.School
    )

    val addTask = FABItem(
        id = "add_task",
        label = "Add Task",
        icon = Icons.Filled.CheckCircle
    )
}
