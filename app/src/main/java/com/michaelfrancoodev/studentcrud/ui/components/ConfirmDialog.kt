package com.michaelfrancoodev.studentcrud.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * ConfirmDialog - Reusable confirmation dialog
 * Used for destructive actions or important confirmations
 */
@Composable
fun ConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    icon: ImageVector? = null,
    isDestructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = if (icon != null) {
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isDestructive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        } else null,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(
                    text = confirmText,
                    color = if (isDestructive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissText)
            }
        }
    )
}

/**
 * DeleteConfirmDialog - Confirmation dialog for delete actions
 */
@Composable
fun DeleteConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    itemName: String = "item",
    message: String = "This action cannot be undone."
) {
    ConfirmDialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = "Delete $itemName?",
        message = message,
        confirmText = "Delete",
        dismissText = "Cancel",
        icon = Icons.Outlined.Delete,
        isDestructive = true
    )
}

/**
 * WarningDialog - Warning dialog for important information
 */
@Composable
fun WarningDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Continue",
    dismissText: String = "Cancel"
) {
    ConfirmDialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = title,
        message = message,
        confirmText = confirmText,
        dismissText = dismissText,
        icon = Icons.Outlined.Warning,
        isDestructive = false
    )
}

/**
 * InfoDialog - Simple information dialog with single action
 */
@Composable
fun InfoDialog(
    onDismiss: () -> Unit,
    title: String,
    message: String,
    buttonText: String = "OK"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = buttonText)
            }
        }
    )
}

/**
 * ErrorDialog - Error dialog with single action
 */
@Composable
fun ErrorDialog(
    onDismiss: () -> Unit,
    title: String = "Error",
    message: String,
    buttonText: String = "OK"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = buttonText)
            }
        }
    )
}
