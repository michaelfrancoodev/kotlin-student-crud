package com.michaelfrancoodev.studentcrud.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.michaelfrancoodev.studentcrud.data.local.dao.ClassScheduleDao
import com.michaelfrancoodev.studentcrud.data.local.dao.CourseDao
import com.michaelfrancoodev.studentcrud.data.local.dao.StudentDao
import com.michaelfrancoodev.studentcrud.data.local.dao.TaskDao
import com.michaelfrancoodev.studentcrud.data.local.entity.ClassSchedule
import com.michaelfrancoodev.studentcrud.data.local.entity.Course
import com.michaelfrancoodev.studentcrud.data.local.entity.Student
import com.michaelfrancoodev.studentcrud.data.local.entity.Task

/**
 * AppDatabase - Room database configuration
 *
 * Contains all entities and DAOs for Soma Smart
 */
@Database(
    entities = [
        Student::class,
        Course::class,
        ClassSchedule::class,
        Task::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun studentDao(): StudentDao
    abstract fun courseDao(): CourseDao
    abstract fun classScheduleDao(): ClassScheduleDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DATABASE_NAME = "soma_smart_database"

        /**
         * Get database instance (singleton)
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // For development - recreate on schema change
                    .build()

                INSTANCE = instance
                instance
            }
        }

        /**
         * Clear database instance (for testing)
         */
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
