package com.michaelfrancoodev.studentcrud.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.michaelfrancoodev.studentcrud.data.local.entity.Course
import kotlinx.coroutines.flow.Flow

/**
 * CourseDao - Data Access Object for Course management
 */
@Dao
interface CourseDao {

    // Get all courses (reactive)
    @Query("SELECT * FROM courses ORDER BY courseName ASC")
    fun getAllCourses(): Flow<List<Course>>

    // Get all courses (one-time)
    @Query("SELECT * FROM courses ORDER BY courseName ASC")
    suspend fun getAllCoursesOnce(): List<Course>

    // Get course by id (reactive)
    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getCourseById(courseId: Int): Flow<Course?>

    // Get course by id (one-time)
    @Query("SELECT * FROM courses WHERE id = :courseId")
    suspend fun getCourseByIdOnce(courseId: Int): Course?

    // Get active courses only
    @Query("SELECT * FROM courses WHERE isActive = 1 ORDER BY courseName ASC")
    fun getActiveCourses(): Flow<List<Course>>

    // Get courses by semester
    @Query("SELECT * FROM courses WHERE semester = :semester AND isActive = 1 ORDER BY courseName ASC")
    fun getCoursesBySemester(semester: String): Flow<List<Course>>

    // Get courses by semester (one-time)
    @Query("SELECT * FROM courses WHERE semester = :semester AND isActive = 1 ORDER BY courseName ASC")
    suspend fun getCoursesBySemesterOnce(semester: String): List<Course>

    // Insert course
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course): Long

    // Insert multiple courses
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<Course>)

    // Update course
    @Update
    suspend fun updateCourse(course: Course)

    // Delete course
    @Delete
    suspend fun deleteCourse(course: Course)

    // Delete course by id
    @Query("DELETE FROM courses WHERE id = :courseId")
    suspend fun deleteCourseById(courseId: Int)

    // Archive course (soft delete)
    @Query("UPDATE courses SET isActive = 0 WHERE id = :courseId")
    suspend fun archiveCourse(courseId: Int)

    // Unarchive course
    @Query("UPDATE courses SET isActive = 1 WHERE id = :courseId")
    suspend fun unarchiveCourse(courseId: Int)

    // Get total credits for semester
    @Query("SELECT COALESCE(SUM(credits), 0) FROM courses WHERE semester = :semester AND isActive = 1")
    suspend fun getTotalCreditsBySemester(semester: String): Int

    // Get total credits for all active courses
    @Query("SELECT COALESCE(SUM(credits), 0) FROM courses WHERE isActive = 1")
    suspend fun getTotalActiveCredits(): Int

    // Get course count by semester
    @Query("SELECT COUNT(*) FROM courses WHERE semester = :semester AND isActive = 1")
    suspend fun getCourseCountBySemester(semester: String): Int

    // Get total active course count
    @Query("SELECT COUNT(*) FROM courses WHERE isActive = 1")
    suspend fun getActiveCourseCount(): Int

    // Search courses by name or code
    @Query("""
        SELECT * FROM courses 
        WHERE (courseName LIKE '%' || :query || '%' OR courseCode LIKE '%' || :query || '%')
        AND isActive = 1
        ORDER BY courseName ASC
    """)
    fun searchCourses(query: String): Flow<List<Course>>

    // Get courses by instructor
    @Query("SELECT * FROM courses WHERE instructor = :instructor AND isActive = 1 ORDER BY courseName ASC")
    fun getCoursesByInstructor(instructor: String): Flow<List<Course>>

    // Check if course code exists
    @Query("SELECT COUNT(*) FROM courses WHERE courseCode = :code AND isActive = 1")
    suspend fun courseCodeExists(code: String): Int

    // Delete all courses (for testing/reset)
    @Query("DELETE FROM courses")
    suspend fun deleteAllCourses()
}
