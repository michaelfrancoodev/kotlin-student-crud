package com.michaelfrancoodev.studentcrud.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Student Profile - Represents your personal profile
 * Single user app - only ONE record exists (id = 1)
 */
@Entity(tableName = "student_profile")
data class Student(
    @PrimaryKey
    val id: Int = 1,

    // Personal Info
    val fullName: String,
    val profilePhotoUri: String? = null,

    // Academic Context
    val university: String = "",
    val major: String = "",
    val currentSemester: String = "Semester 1", // "Semester 1" or "Semester 2"

    // Contact (Optional)
    val email: String = "",
    val phoneNumber: String = "",

    // Goals & Personalization
    val dailyStudyGoalHours: Int = 4,
    val motto: String = "Keep learning, keep growing!",

    // Tracking
    val currentStreak: Int = 0,
    val totalStudyHours: Int = 0,

    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {

    // Get first name for greetings
    val firstName: String
        get() = fullName.split(" ").firstOrNull() ?: fullName

    // Get initials for avatar (e.g., "MF")
    val initials: String
        get() {
            val names = fullName.split(" ")
            return when {
                names.size >= 2 -> "${names[0].first()}${names[1].first()}"
                names.size == 1 && names[0].length >= 2 -> names[0].take(2)
                names.size == 1 && names[0].isNotEmpty() -> names[0].first().toString() + names[0].first().toString()
                else -> "ME"
            }.uppercase()
        }

    // Check if profile setup is complete
    val isProfileComplete: Boolean
        get() = fullName.isNotBlank() &&
                university.isNotBlank() &&
                major.isNotBlank()

    // Check if streak is active
    val hasActiveStreak: Boolean
        get() = currentStreak > 0
}
