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
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * ErrorState - Reusable error state component
 * Shows when an error occurs
 */
@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.ErrorOutline,
    title: String = "Something went wrong",
    message: String = "An error occurred. Please try again.",
    primaryActionText: String? = "Retry",
    onPrimaryAction: (() -> Unit)? = null,
    secondaryActionText: String? = null,
    onSecondaryAction: (() -> Unit)? = null
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
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
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

            if (primaryActionText != null && onPrimaryAction != null) {
                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = onPrimaryAction) {
                    Text(text = primaryActionText)
                }
            }

            if (secondaryActionText != null && onSecondaryAction != null) {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(onClick = onSecondaryAction) {
                    Text(text = secondaryActionText)
                }
            }
        }
    }
}

/**
 * NetworkErrorState - Error state for network issues
 */
@Composable
fun NetworkErrorState(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    ErrorState(
        modifier = modifier,
        icon = Icons.Outlined.WifiOff,
        title = "No internet connection",
        message = "Please check your network connection and try again",
        primaryActionText = "Retry",
        onPrimaryAction = onRetry
    )
}

/**
 * GenericErrorState - Generic error with retry option
 */
@Composable
fun GenericErrorState(
    modifier: Modifier = Modifier,
    message: String = "An unexpected error occurred",
    onRetry: () -> Unit
) {
    ErrorState(
        modifier = modifier,
        icon = Icons.Outlined.ErrorOutline,
        title = "Error",
        message = message,
        primaryActionText = "Retry",
        onPrimaryAction = onRetry
    )
}

/**
 * LoadErrorState - Error when loading data fails
 */
@Composable
fun LoadErrorState(
    modifier: Modifier = Modifier,
    message: String = "Failed to load data",
    onRetry: () -> Unit,
    onCancel: (() -> Unit)? = null
) {
    ErrorState(
        modifier = modifier,
        icon = Icons.Outlined.Warning,
        title = "Loading Failed",
        message = message,
        primaryActionText = "Try Again",
        onPrimaryAction = onRetry,
        secondaryActionText = if (onCancel != null) "Cancel" else null,
        onSecondaryAction = onCancel
    )
}

/**
 * NotFoundErrorState - Error when item is not found
 */
@Composable
fun NotFoundErrorState(
    modifier: Modifier = Modifier,
    itemName: String = "Item",
    onGoBack: () -> Unit
) {
    ErrorState(
        modifier = modifier,
        icon = Icons.Outlined.ErrorOutline,
        title = "$itemName not found",
        message = "The $itemName you're looking for doesn't exist or has been deleted",
        primaryActionText = "Go Back",
        onPrimaryAction = onGoBack
    )
}
