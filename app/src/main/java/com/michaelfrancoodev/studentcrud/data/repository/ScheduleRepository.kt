package com.michaelfrancoodev.studentcrud.data.repository

import com.michaelfrancoodev.studentcrud.data.local.dao.ClassScheduleDao
import com.michaelfrancoodev.studentcrud.data.local.entity.ClassSchedule
import kotlinx.coroutines.flow.Flow

/**
 * ScheduleRepository - Repository layer for ClassSchedule operations
 * Handles business logic and data operations for course schedules
 */
class ScheduleRepository(private val scheduleDao: ClassScheduleDao) {

    // Get all schedules for a course (reactive)
    fun getSchedulesByCourse(courseId: Int): Flow<List<ClassSchedule>> =
        scheduleDao.getSchedulesByCourse(courseId)

    // Get all schedules for a course (one-time)
    suspend fun getSchedulesByCourseOnce(courseId: Int): List<ClassSchedule> =
        scheduleDao.getSchedulesByCourseOnce(courseId)

    // Get schedule by id (reactive)
    fun getScheduleById(scheduleId: Int): Flow<ClassSchedule?> =
        scheduleDao.getScheduleById(scheduleId)

    // Get schedule by id (one-time)
    suspend fun getScheduleByIdOnce(scheduleId: Int): ClassSchedule? =
        scheduleDao.getScheduleByIdOnce(scheduleId)

    // Get all schedules (reactive)
    fun getAllSchedules(): Flow<List<ClassSchedule>> = scheduleDao.getAllSchedules()

    // Get all schedules (one-time)
    suspend fun getAllSchedulesOnce(): List<ClassSchedule> = scheduleDao.getAllSchedulesOnce()

    // Get schedules by day (reactive)
    fun getSchedulesByDay(dayName: String): Flow<List<ClassSchedule>> =
        scheduleDao.getSchedulesByDay(dayName)

    // Get schedules by day (one-time)
    suspend fun getSchedulesByDayOnce(dayName: String): List<ClassSchedule> =
        scheduleDao.getSchedulesByDayOnce(dayName)

    // Insert schedule
    suspend fun insertSchedule(schedule: ClassSchedule): Long =
        scheduleDao.insertSchedule(schedule)

    // Insert multiple schedules
    suspend fun insertSchedules(schedules: List<ClassSchedule>) =
        scheduleDao.insertSchedules(schedules)

    // Update schedule
    suspend fun updateSchedule(schedule: ClassSchedule) =
        scheduleDao.updateSchedule(schedule)

    // Delete schedule
    suspend fun deleteSchedule(schedule: ClassSchedule) =
        scheduleDao.deleteSchedule(schedule)

    // Delete schedule by id
    suspend fun deleteScheduleById(scheduleId: Int) =
        scheduleDao.deleteScheduleById(scheduleId)

    // Delete all schedules for a course
    suspend fun deleteSchedulesByCourse(courseId: Int) =
        scheduleDao.deleteSchedulesByCourse(courseId)

    // Get schedule count for a course
    suspend fun getScheduleCount(courseId: Int): Int =
        scheduleDao.getScheduleCount(courseId)

    // Check if course has schedules
    suspend fun courseHasSchedules(courseId: Int): Boolean =
        scheduleDao.courseHasSchedules(courseId)

    // Get schedules by location
    fun getSchedulesByLocation(locationQuery: String): Flow<List<ClassSchedule>> =
        scheduleDao.getSchedulesByLocation(locationQuery)

    // Get schedules for multiple days
    suspend fun getSchedulesForDays(days: List<String>): List<ClassSchedule> {
        val allSchedules = getAllSchedulesOnce()
        return allSchedules.filter { schedule ->
            days.any { day -> schedule.isOnDay(day) }
        }
    }

    // Get today's schedules (helper method)
    suspend fun getTodaysSchedules(dayName: String): List<ClassSchedule> =
        getSchedulesByDayOnce(dayName)

    // Get tomorrow's schedules (helper method)
    suspend fun getTomorrowSchedules(dayName: String): List<ClassSchedule> =
        getSchedulesByDayOnce(dayName)

    // Validate and save schedule
    suspend fun validateAndSaveSchedule(schedule: ClassSchedule): Result<Long> {
        return try {
            // Validate days
            if (schedule.days.isBlank()) {
                return Result.failure(Exception("At least one day must be selected"))
            }

            // Validate times
            if (schedule.startTime.isBlank() || schedule.endTime.isBlank()) {
                return Result.failure(Exception("Start time and end time are required"))
            }

            // Parse times and validate (basic check)
            if (schedule.startTime >= schedule.endTime) {
                return Result.failure(Exception("End time must be after start time"))
            }

            val id = insertSchedule(schedule)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Save multiple schedules for a course
    suspend fun saveCourseSchedules(courseId: Int, schedules: List<ClassSchedule>): Result<Unit> {
        return try {
            // Delete existing schedules for this course
            deleteSchedulesByCourse(courseId)

            // Insert new schedules
            val schedulesWithCourseId = schedules.map { it.copy(courseId = courseId) }
            insertSchedules(schedulesWithCourseId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get weekly schedule summary (how many classes per day)
    suspend fun getWeeklyScheduleSummary(): Map<String, Int> {
        val allSchedules = getAllSchedulesOnce()
        val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        return daysOfWeek.associateWith { day ->
            allSchedules.count { schedule -> schedule.isOnDay(day) }
        }
    }

    // Delete all schedules (for testing/reset)
    suspend fun deleteAllSchedules() = scheduleDao.deleteAllSchedules()
}
