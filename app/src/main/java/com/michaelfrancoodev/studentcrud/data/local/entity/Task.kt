package com.michaelfrancoodev.studentcrud.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Task Entity - Represents an assignment, quiz, project, etc. for a course.
 */
@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("courseId")]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val courseId: Int,

    // Basic info
    val title: String,
    val description: String = "",

    // Deadlines
    val dueDate: Long,          // timestamp (millis)
    val dueTime: String = "",  // e.g. "11:59 PM" (optional, for display)

    // Priority: "Low", "Medium", "High"
    val priority: String = "Medium",

    // Status
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,

    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {

    // True if task is overdue and not completed
    val isOverdue: Boolean
        get() = !isCompleted && dueDate < System.currentTimeMillis()

    // Short label for UI chips/cards
    val priorityLabel: String
        get() = when (priority.lowercase()) {
            "high" -> "High"
            "low" -> "Low"
            else -> "Medium"
        }
}
