package com.michaelfrancoodev.studentcrud.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * ClassSchedule Entity - Represents one schedule block for a course
 * A course can have multiple schedules (different days/times/locations)
 *
 * Example: Cyber Security might have:
 * - Schedule 1: Mon, Wed at 8:00-10:00 AM in Room 301
 * - Schedule 2: Tue at 2:00-5:00 PM in Lab 204
 * - Schedule 3: Fri at 10:00 AM-12:00 PM in Conference Room
 */
@Entity(
    tableName = "class_schedules",
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
data class ClassSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val courseId: Int,

    // Days for this schedule (comma-separated)
    val days: String, // e.g., "Monday,Wednesday" or "Tuesday" or "Friday"

    // Time
    val startTime: String, // e.g., "8:00 AM"
    val endTime: String, // e.g., "10:00 AM"

    // Location
    val location: String = "", // e.g., "Room 301, CS Building"

    val createdAt: Long = System.currentTimeMillis()
) {

    // Parse days string into list
    val daysList: List<String>
        get() = days.split(",").map { it.trim() }

    // Check if this schedule is on a specific day
    fun isOnDay(dayName: String): Boolean {
        return daysList.any { it.equals(dayName, ignoreCase = true) }
    }

    // Get formatted time range
    val timeRange: String
        get() = "$startTime - $endTime"
}
