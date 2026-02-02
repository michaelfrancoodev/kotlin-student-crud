package com.michaelfrancoodev.studentcrud.data.repository

import com.michaelfrancoodev.studentcrud.data.local.dao.TaskDao
import com.michaelfrancoodev.studentcrud.data.local.entity.Task
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

/**
 * TaskRepository - Repository layer for Task operations
 * Handles business logic and data operations for tasks/assignments
 */
class TaskRepository(private val taskDao: TaskDao) {

    // Get all tasks (reactive)
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    // Get all tasks (one-time)
    suspend fun getAllTasksOnce(): List<Task> = taskDao.getAllTasksOnce()

    // Get task by id (reactive)
    fun getTaskById(taskId: Int): Flow<Task?> = taskDao.getTaskById(taskId)

    // Get task by id (one-time)
    suspend fun getTaskByIdOnce(taskId: Int): Task? = taskDao.getTaskByIdOnce(taskId)

    // Get tasks by course
    fun getTasksByCourse(courseId: Int): Flow<List<Task>> = taskDao.getTasksByCourse(courseId)

    // Get tasks by course (one-time)
    suspend fun getTasksByCourseOnce(courseId: Int): List<Task> =
        taskDao.getTasksByCourseOnce(courseId)

    // Get pending tasks
    fun getPendingTasks(): Flow<List<Task>> = taskDao.getPendingTasks()

    // Get completed tasks
    fun getCompletedTasks(): Flow<List<Task>> = taskDao.getCompletedTasks()

    // Get overdue tasks
    fun getOverdueTasks(): Flow<List<Task>> = taskDao.getOverdueTasks()

    // Get overdue tasks (one-time)
    suspend fun getOverdueTasksOnce(): List<Task> = taskDao.getOverdueTasksOnce()

    // Get tasks due today
    fun getTasksDueToday(): Flow<List<Task>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis

        return taskDao.getTasksDueToday(startOfDay, endOfDay)
    }

    // Get tasks due this week
    fun getTasksDueThisWeek(): Flow<List<Task>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfWeek = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 7)
        val endOfWeek = calendar.timeInMillis

        return taskDao.getTasksDueThisWeek(startOfWeek, endOfWeek)
    }

    // Get tasks by priority
    fun getTasksByPriority(priority: String): Flow<List<Task>> =
        taskDao.getTasksByPriority(priority)

    // Insert task
    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    // Insert multiple tasks
    suspend fun insertTasks(tasks: List<Task>) = taskDao.insertTasks(tasks)

    // Update task
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    // Delete task
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    // Delete task by id
    suspend fun deleteTaskById(taskId: Int) = taskDao.deleteTaskById(taskId)

    // Delete all tasks for a course
    suspend fun deleteTasksByCourse(courseId: Int) = taskDao.deleteTasksByCourse(courseId)

    // Mark task as completed
    suspend fun markTaskComplete(taskId: Int) = taskDao.markTaskComplete(taskId)

    // Mark task as incomplete
    suspend fun markTaskIncomplete(taskId: Int) = taskDao.markTaskIncomplete(taskId)

    // Toggle task completion
    suspend fun toggleTaskCompletion(taskId: Int) {
        val task = getTaskByIdOnce(taskId)
        task?.let {
            if (it.isCompleted) {
                markTaskIncomplete(taskId)
            } else {
                markTaskComplete(taskId)
            }
        }
    }

    // Get task count by course
    suspend fun getPendingTaskCountByCourse(courseId: Int): Int =
        taskDao.getPendingTaskCountByCourse(courseId)

    // Get total pending tasks count
    suspend fun getPendingTaskCount(): Int = taskDao.getPendingTaskCount()

    // Get completed tasks count
    suspend fun getCompletedTaskCount(): Int = taskDao.getCompletedTaskCount()

    // Get overdue tasks count
    suspend fun getOverdueTaskCount(): Int = taskDao.getOverdueTaskCount()

    // Search tasks
    fun searchTasks(query: String): Flow<List<Task>> = taskDao.searchTasks(query)

    // Delete all completed tasks
    suspend fun deleteCompletedTasks() = taskDao.deleteCompletedTasks()

    // Validate and save task
    suspend fun validateAndSaveTask(task: Task): Result<Long> {
        return try {
            // Validate title
            if (task.title.isBlank()) {
                return Result.failure(Exception("Task title cannot be empty"))
            }

            // Validate due date (must be in future for new tasks)
            if (task.id == 0 && task.dueDate < System.currentTimeMillis()) {
                return Result.failure(Exception("Due date must be in the future"))
            }

            // Validate priority
            val validPriorities = listOf("Low", "Medium", "High")
            if (task.priority !in validPriorities) {
                return Result.failure(Exception("Invalid priority. Must be Low, Medium, or High"))
            }

            val id = insertTask(task)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get task statistics
    suspend fun getTaskStatistics(): TaskStatistics {
        return TaskStatistics(
            total = getAllTasksOnce().size,
            pending = getPendingTaskCount(),
            completed = getCompletedTaskCount(),
            overdue = getOverdueTaskCount()
        )
    }

    // Get high priority pending tasks
    suspend fun getHighPriorityPendingTasks(): List<Task> {
        val allPending = taskDao.getAllTasksOnce().filter { !it.isCompleted }
        return allPending.filter { it.priority.equals("High", ignoreCase = true) }
            .sortedBy { it.dueDate }
    }

    // Check if task is due soon (within 24 hours)
    fun isTaskDueSoon(task: Task): Boolean {
        val oneDayInMillis = 24 * 60 * 60 * 1000
        val now = System.currentTimeMillis()
        return !task.isCompleted && task.dueDate - now in 0..oneDayInMillis
    }

    // Delete all tasks (for testing/reset)
    suspend fun deleteAllTasks() = taskDao.deleteAllTasks()
}

/**
 * Data class for task statistics
 */
data class TaskStatistics(
    val total: Int,
    val pending: Int,
    val completed: Int,
    val overdue: Int
)
