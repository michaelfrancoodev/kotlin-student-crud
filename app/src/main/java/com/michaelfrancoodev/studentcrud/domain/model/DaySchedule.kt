package com.michaelfrancoodev.studentcrud.domain.model

import com.michaelfrancoodev.studentcrud.data.local.entity.ClassSchedule
import com.michaelfrancoodev.studentcrud.data.local.entity.Course

/**
 * DaySchedule - Domain model for daily timetable view
 * Represents all classes and events for a specific day
 */
data class DaySchedule(
    val dayName: String, // e.g., "Monday"
    val date: String, // e.g., "January 27, 2026"
    val classesWithCourses: List<ClassWithCourse>
) {

    // Get total class count for this day
    val classCount: Int
        get() = classesWithCourses.size

    // Check if this day has classes
    val hasClasses: Boolean
        get() = classesWithCourses.isNotEmpty()

    // Get total class hours for this day
    val totalClassHours: Int
        get() = classesWithCourses.sumOf { it.course.credits }

    // Get earliest class time
    val firstClassTime: String?
        get() = classesWithCourses.minByOrNull { it.schedule.startTime }?.schedule?.startTime

    // Get latest class time
    val lastClassTime: String?
        get() = classesWithCourses.maxByOrNull { it.schedule.endTime }?.schedule?.endTime
}

/**
 * ClassWithCourse - Single class session with full course details
 * Used in timetable display
 */
data class ClassWithCourse(
    val schedule: ClassSchedule,
    val course: Course
) {

    // Get formatted time range
    val timeRange: String
        get() = "${schedule.startTime} - ${schedule.endTime}"

    // Get location or default text
    val displayLocation: String
        get() = schedule.location.ifBlank { "Location TBA" }
}
