package com.michaelfrancoodev.studentcrud.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Shape definitions for Soma Smart
val Shapes = Shapes(
    // Extra Small - Used for badges, chips
    extraSmall = RoundedCornerShape(4.dp),

    // Small - Used for buttons, small cards
    small = RoundedCornerShape(8.dp),

    // Medium - Used for cards, dialogs (DEFAULT)
    medium = RoundedCornerShape(12.dp),

    // Large - Used for bottom sheets, large cards
    large = RoundedCornerShape(16.dp),

    // Extra Large - Used for modals, full-screen dialogs
    extraLarge = RoundedCornerShape(24.dp)
)

// Additional custom shapes for specific components
object CustomShapes {
    val BottomSheet = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    val TopRounded = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    val BottomRounded = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )

    val LeftRounded = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 0.dp
    )

    val RightRounded = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 16.dp,
        bottomStart = 0.dp,
        bottomEnd = 16.dp
    )

    val FullRounded = RoundedCornerShape(50) // Circular/pill shape
}
