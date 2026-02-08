package com.michaelfrancoodev.studentcrud.domain.model

import com.michaelfrancoodev.studentcrud.data.local.entity.ClassSchedule
import com.michaelfrancoodev.studentcrud.data.local.entity.Task

/**
 * DashboardStats - Domain model for Home screen statistics
 * Aggregates data from multiple sources for dashboard display
 */
data class DashboardStats(
    // Today's overview
    val activeCourseCount: Int = 0,
    val pendingTaskCount: Int = 0,
    val todayStudyHours: Int = 0,

    // Semester info
    val totalCredits: Int = 0,
    val totalClassHours: Int = 0,

    // Study tracking
    val currentStreak: Int = 0,
    val weeklyStudyGoal: Int = 20,
    val weeklyStudyProgress: Int = 0,

    // Today's schedule
    val todayClasses: List<ClassScheduleWithCourse> = emptyList(),
    val tomorrowClasses: List<ClassScheduleWithCourse> = emptyList(),

    // Urgent tasks
    val overdueTaskCount: Int = 0,
    val dueTodayTaskCount: Int = 0,
    val pendingTasks: List<TaskWithCourse> = emptyList()
) {

    // Calculate weekly study progress percentage
    val weeklyProgressPercentage: Int
        get() = if (weeklyStudyGoal > 0) {
            ((weeklyStudyProgress.toFloat() / weeklyStudyGoal) * 100).toInt()
        } else 0

    // Check if there are urgent tasks
    val hasUrgentTasks: Boolean
        get() = overdueTaskCount > 0 || dueTodayTaskCount > 0

    // Check if user is on track with study goal
    val isOnTrack: Boolean
        get() = weeklyProgressPercentage >= 70
}

/**
 * ClassSchedule with Course information
 * Used to display schedule with course details
 */
data class ClassScheduleWithCourse(
    val schedule: ClassSchedule,
    val courseName: String,
    val courseCode: String,
    val courseColor: String,
    val instructor: String,
    val credits: Int
)

/**
 * Task with Course information
 * Used to display tasks with course context
 */
data class TaskWithCourse(
    val task: Task,
    val courseName: String,
    val courseCode: String,
    val courseColor: String,
    val credits: Int
)
