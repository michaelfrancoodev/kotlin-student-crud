package com.michaelfrancoodev.studentcrud.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.michaelfrancoodev.studentcrud.data.local.entity.ClassSchedule
import kotlinx.coroutines.flow.Flow

/**
 * ClassScheduleDao - Data Access Object for course schedules
 * Handles multiple schedules per course (different days/times/locations)
 */
@Dao
interface ClassScheduleDao {

    // Get all schedules for a course (reactive)
    @Query("SELECT * FROM class_schedules WHERE courseId = :courseId ORDER BY days ASC")
    fun getSchedulesByCourse(courseId: Int): Flow<List<ClassSchedule>>

    // Get all schedules for a course (one-time)
    @Query("SELECT * FROM class_schedules WHERE courseId = :courseId ORDER BY days ASC")
    suspend fun getSchedulesByCourseOnce(courseId: Int): List<ClassSchedule>

    // Get schedule by id (reactive)
    @Query("SELECT * FROM class_schedules WHERE id = :scheduleId")
    fun getScheduleById(scheduleId: Int): Flow<ClassSchedule?>

    // Get schedule by id (one-time)
    @Query("SELECT * FROM class_schedules WHERE id = :scheduleId")
    suspend fun getScheduleByIdOnce(scheduleId: Int): ClassSchedule?

    // Get all schedules (reactive) - for timetable view
    @Query("SELECT * FROM class_schedules ORDER BY courseId ASC")
    fun getAllSchedules(): Flow<List<ClassSchedule>>

    // Get all schedules (one-time)
    @Query("SELECT * FROM class_schedules ORDER BY courseId ASC")
    suspend fun getAllSchedulesOnce(): List<ClassSchedule>

    // Get schedules by day (e.g., all Monday classes)
    @Query("SELECT * FROM class_schedules WHERE days LIKE '%' || :dayName || '%' ORDER BY startTime ASC")
    fun getSchedulesByDay(dayName: String): Flow<List<ClassSchedule>>

    // Get schedules by day (one-time)
    @Query("SELECT * FROM class_schedules WHERE days LIKE '%' || :dayName || '%' ORDER BY startTime ASC")
    suspend fun getSchedulesByDayOnce(dayName: String): List<ClassSchedule>

    // Insert schedule
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ClassSchedule): Long

    // Insert multiple schedules
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ClassSchedule>)

    // Update schedule
    @Update
    suspend fun updateSchedule(schedule: ClassSchedule)

    // Delete schedule
    @Delete
    suspend fun deleteSchedule(schedule: ClassSchedule)

    // Delete schedule by id
    @Query("DELETE FROM class_schedules WHERE id = :scheduleId")
    suspend fun deleteScheduleById(scheduleId: Int)

    // Delete all schedules for a course
    @Query("DELETE FROM class_schedules WHERE courseId = :courseId")
    suspend fun deleteSchedulesByCourse(courseId: Int)

    // Get schedule count for a course
    @Query("SELECT COUNT(*) FROM class_schedules WHERE courseId = :courseId")
    suspend fun getScheduleCount(courseId: Int): Int

    // Get total class hours per week for a course
    @Query("SELECT COUNT(*) FROM class_schedules WHERE courseId = :courseId")
    suspend fun getWeeklyClassCount(courseId: Int): Int

    // Check if course has schedules
    @Query("SELECT COUNT(*) > 0 FROM class_schedules WHERE courseId = :courseId")
    suspend fun courseHasSchedules(courseId: Int): Boolean

    // Get schedules by location
    @Query("SELECT * FROM class_schedules WHERE location LIKE '%' || :locationQuery || '%' ORDER BY startTime ASC")
    fun getSchedulesByLocation(locationQuery: String): Flow<List<ClassSchedule>>

    // Delete all schedules (for testing/reset)
    @Query("DELETE FROM class_schedules")
    suspend fun deleteAllSchedules()
}
