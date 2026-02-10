package com.michaelfrancoodev.studentcrud.util

/**
 * Constants - App-wide constant values for Soma Smart
 */
object Constants {

    // ========================================
    // APP INFO
    // ========================================
    const val APP_NAME = "Soma Smart"
    const val APP_VERSION = "1.0.0"
    const val APP_PACKAGE = "com.michaelfrancoodev.studentcrud"

    // ========================================
    // DATABASE
    // ========================================
    const val DATABASE_NAME = "soma_smart_database"
    const val DATABASE_VERSION = 1

    // ========================================
    // PREFERENCES
    // ========================================
    const val PREFS_NAME = "soma_smart_prefs"
    const val PREF_THEME_MODE = "theme_mode" // "light", "dark", "system"
    const val PREF_FIRST_RUN = "first_run"
    const val PREF_LAST_STREAK_DATE = "last_streak_date"
    const val PREF_CURRENT_SEMESTER = "current_semester"

    // ========================================
    // SEMESTERS
    // ========================================
    const val SEMESTER_1 = "Semester 1"
    const val SEMESTER_2 = "Semester 2"
    val SEMESTERS = listOf(SEMESTER_1, SEMESTER_2)

    // ========================================
    // CREDITS (Tanzanian System)
    // ========================================
    val CREDIT_OPTIONS = listOf(2, 3, 6, 9, 10, 11)

    // ========================================
    // DAYS OF WEEK
    // ========================================
    const val MONDAY = "Monday"
    const val TUESDAY = "Tuesday"
    const val WEDNESDAY = "Wednesday"
    const val THURSDAY = "Thursday"
    const val FRIDAY = "Friday"
    const val SATURDAY = "Saturday"
    const val SUNDAY = "Sunday"

    val DAYS_OF_WEEK = listOf(
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    )

    val WEEKDAYS = listOf(
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY
    )

    // ========================================
    // TASK PRIORITIES
    // ========================================
    const val PRIORITY_LOW = "Low"
    const val PRIORITY_MEDIUM = "Medium"
    const val PRIORITY_HIGH = "High"

    val TASK_PRIORITIES = listOf(
        PRIORITY_LOW,
        PRIORITY_MEDIUM,
        PRIORITY_HIGH
    )

    // ========================================
    // COURSE COLORS (Hex codes)
    // ========================================
    const val COLOR_BLUE = "#2196F3"
    const val COLOR_GREEN = "#4CAF50"
    const val COLOR_ORANGE = "#FF9800"
    const val COLOR_RED = "#F44336"
    const val COLOR_PURPLE = "#9C27B0"
    const val COLOR_TEAL = "#009688"

    val COURSE_COLORS = listOf(
        COLOR_BLUE,
        COLOR_GREEN,
        COLOR_ORANGE,
        COLOR_RED,
        COLOR_PURPLE,
        COLOR_TEAL
    )

    // ========================================
    // TIME
    // ========================================
    const val HOUR_IN_MILLIS = 3600000L // 1 hour in milliseconds
    const val DAY_IN_MILLIS = 86400000L // 1 day in milliseconds
    const val WEEK_IN_MILLIS = 604800000L // 1 week in milliseconds

    // ========================================
    // STUDY GOALS
    // ========================================
    const val DEFAULT_DAILY_GOAL = 4 // hours
    const val DEFAULT_WEEKLY_GOAL = 20 // hours
    const val MIN_STUDY_GOAL = 1
    const val MAX_STUDY_GOAL = 12

    // ========================================
    // UI
    // ========================================
    const val ANIMATION_DURATION = 300 // milliseconds
    const val SPLASH_DELAY = 2000L // milliseconds
    const val DEBOUNCE_DELAY = 300L // milliseconds for search

    // Pagination
    const val ITEMS_PER_PAGE = 20
    const val MAX_TASKS_IN_HOME = 5
    const val MAX_CLASSES_IN_HOME = 3

    // ========================================
    // NOTIFICATIONS
    // ========================================
    const val NOTIFICATION_CHANNEL_ID = "soma_smart_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Soma Smart Notifications"
    const val NOTIFICATION_CHANNEL_DESCRIPTION = "Class reminders and task deadlines"

    // Notification IDs
    const val NOTIFICATION_ID_CLASS_REMINDER = 1001
    const val NOTIFICATION_ID_TASK_DEADLINE = 1002
    const val NOTIFICATION_ID_STREAK_REMINDER = 1003

    // ========================================
    // VALIDATION
    // ========================================
    const val MIN_COURSE_NAME_LENGTH = 3
    const val MAX_COURSE_NAME_LENGTH = 100
    const val MIN_TASK_TITLE_LENGTH = 3
    const val MAX_TASK_TITLE_LENGTH = 200
    const val MAX_DESCRIPTION_LENGTH = 1000

    // ========================================
    // DATE FORMATS
    // ========================================
    const val DATE_FORMAT_FULL = "EEEE, MMMM dd, yyyy" // "Monday, January 27, 2026"
    const val DATE_FORMAT_SHORT = "MMM dd, yyyy" // "Jan 27, 2026"
    const val DATE_FORMAT_DAY_MONTH = "MMM dd" // "Jan 27"
    const val TIME_FORMAT_12H = "h:mm a" // "8:00 AM"
    const val TIME_FORMAT_24H = "HH:mm" // "08:00"

    // ========================================
    // ERROR MESSAGES
    // ========================================
    const val ERROR_EMPTY_FIELD = "This field cannot be empty"
    const val ERROR_INVALID_EMAIL = "Please enter a valid email"
    const val ERROR_INVALID_CREDITS = "Credits must be a positive number"
    const val ERROR_INVALID_TIME = "End time must be after start time"
    const val ERROR_NO_DAYS_SELECTED = "Please select at least one day"
    const val ERROR_DUPLICATE_COURSE_CODE = "Course code already exists"
    const val ERROR_NETWORK = "Network error. Please check your connection"
    const val ERROR_UNKNOWN = "An unexpected error occurred"
}
