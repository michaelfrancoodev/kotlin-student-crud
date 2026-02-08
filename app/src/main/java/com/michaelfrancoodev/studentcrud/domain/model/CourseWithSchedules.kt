package com.michaelfrancoodev.studentcrud.domain.model

import com.michaelfrancoodev.studentcrud.data.local.entity.ClassSchedule
import com.michaelfrancoodev.studentcrud.data.local.entity.Course

/**
 * CourseWithSchedules - Domain model combining Course with its schedules
 * Used for detailed course view with all schedule blocks
 */
data class CourseWithSchedules(
    val course: Course,
    val schedules: List<ClassSchedule>
) {

    // Get total class sessions per week
    val weeklySessionCount: Int
        get() = schedules.size

    // Get all unique days this course meets
    val classDays: List<String>
        get() {
            val allDays = schedules.flatMap { it.daysList }
            return allDays.distinct().sorted()
        }

    // Get formatted class days (e.g., "Mon, Wed, Fri")
    val formattedDays: String
        get() = classDays.joinToString(", ") { it.take(3) }

    // Check if course has classes today
    fun hasClassToday(dayName: String): Boolean {
        return schedules.any { it.isOnDay(dayName) }
    }

    // Get schedules for a specific day
    fun getSchedulesForDay(dayName: String): List<ClassSchedule> {
        return schedules.filter { it.isOnDay(dayName) }
    }

    // Check if course has schedules
    val hasSchedules: Boolean
        get() = schedules.isNotEmpty()
}
