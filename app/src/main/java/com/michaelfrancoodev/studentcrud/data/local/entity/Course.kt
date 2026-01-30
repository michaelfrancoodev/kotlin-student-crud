package com.michaelfrancoodev.studentcrud.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Course Entity - Represents a university course
 * Each course has credits, semester, and color coding
 */
@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Basic Info
    val courseName: String,
    val courseCode: String, // e.g., IT 6315, CS301, MATH101
    val instructor: String = "",

    // Academic Details
    val credits: Int, // e.g., 2, 3, 6, 9, 10, 11
    val semester: String, // "Semester 1" or "Semester 2"

    // UI Customization
    val color: String, // Hex color code (e.g., "#2196F3")

    // Status
    val isActive: Boolean = true,

    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {

    // Calculate recommended study hours (rule: 2x credits per week)
    val recommendedStudyHours: Int
        get() = credits * 2

    // Check if course is in current semester
    fun isCurrentSemester(currentSemester: String): Boolean {
        return semester == currentSemester
    }
}
