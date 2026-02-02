package com.michaelfrancoodev.studentcrud.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.michaelfrancoodev.studentcrud.data.local.entity.Task
import kotlinx.coroutines.flow.Flow

/**
 * TaskDao - Data Access Object for task/assignment management
 */
@Dao
interface TaskDao {

    // Get all tasks (reactive)
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>

    // Get all tasks (one-time)
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    suspend fun getAllTasksOnce(): List<Task>

    // Get task by id (reactive)
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): Flow<Task?>

    // Get task by id (one-time)
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskByIdOnce(taskId: Int): Task?

    // Get tasks by course
    @Query("SELECT * FROM tasks WHERE courseId = :courseId ORDER BY dueDate ASC")
    fun getTasksByCourse(courseId: Int): Flow<List<Task>>

    // Get tasks by course (one-time)
    @Query("SELECT * FROM tasks WHERE courseId = :courseId ORDER BY dueDate ASC")
    suspend fun getTasksByCourseOnce(courseId: Int): List<Task>

    // Get pending tasks (not completed)
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getPendingTasks(): Flow<List<Task>>

    // Get completed tasks
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedTasks(): Flow<List<Task>>

    // Get overdue tasks (not completed and past due date)
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND dueDate < :currentTime ORDER BY dueDate ASC")
    fun getOverdueTasks(currentTime: Long = System.currentTimeMillis()): Flow<List<Task>>

    // Get overdue tasks (one-time)
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND dueDate < :currentTime ORDER BY dueDate ASC")
    suspend fun getOverdueTasksOnce(currentTime: Long = System.currentTimeMillis()): List<Task>

    // Get tasks due today
    @Query("""
        SELECT * FROM tasks 
        WHERE isCompleted = 0 
        AND dueDate >= :startOfDay 
        AND dueDate < :endOfDay 
        ORDER BY dueDate ASC
    """)
    fun getTasksDueToday(startOfDay: Long, endOfDay: Long): Flow<List<Task>>

    // Get tasks due this week
    @Query("""
        SELECT * FROM tasks 
        WHERE isCompleted = 0 
        AND dueDate >= :startOfWeek 
        AND dueDate < :endOfWeek 
        ORDER BY dueDate ASC
    """)
    fun getTasksDueThisWeek(startOfWeek: Long, endOfWeek: Long): Flow<List<Task>>

    // Get tasks by priority
    @Query("SELECT * FROM tasks WHERE priority = :priority AND isCompleted = 0 ORDER BY dueDate ASC")
    fun getTasksByPriority(priority: String): Flow<List<Task>>

    // Insert task
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    // Insert multiple tasks
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>)

    // Update task
    @Update
    suspend fun updateTask(task: Task)

    // Delete task
    @Delete
    suspend fun deleteTask(task: Task)

    // Delete task by id
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)

    // Delete all tasks for a course
    @Query("DELETE FROM tasks WHERE courseId = :courseId")
    suspend fun deleteTasksByCourse(courseId: Int)

    // Mark task as completed
    @Query("""
        UPDATE tasks 
        SET isCompleted = 1, 
            completedAt = :completedTime,
            updatedAt = :completedTime
        WHERE id = :taskId
    """)
    suspend fun markTaskComplete(taskId: Int, completedTime: Long = System.currentTimeMillis())

    // Mark task as incomplete
    @Query("""
        UPDATE tasks 
        SET isCompleted = 0, 
            completedAt = NULL,
            updatedAt = :updatedTime
        WHERE id = :taskId
    """)
    suspend fun markTaskIncomplete(taskId: Int, updatedTime: Long = System.currentTimeMillis())

    // Get task count by course
    @Query("SELECT COUNT(*) FROM tasks WHERE courseId = :courseId AND isCompleted = 0")
    suspend fun getPendingTaskCountByCourse(courseId: Int): Int

    // Get total pending tasks count
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    suspend fun getPendingTaskCount(): Int

    // Get completed tasks count
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    suspend fun getCompletedTaskCount(): Int

    // Get overdue tasks count
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0 AND dueDate < :currentTime")
    suspend fun getOverdueTaskCount(currentTime: Long = System.currentTimeMillis()): Int

    // Search tasks by title
    @Query("""
        SELECT * FROM tasks 
        WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'
        ORDER BY dueDate ASC
    """)
    fun searchTasks(query: String): Flow<List<Task>>

    // Delete all completed tasks
    @Query("DELETE FROM tasks WHERE isCompleted = 1")
    suspend fun deleteCompletedTasks()

    // Delete all tasks (for testing/reset)
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}
