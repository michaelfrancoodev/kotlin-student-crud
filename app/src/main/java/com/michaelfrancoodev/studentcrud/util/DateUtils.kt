package com.michaelfrancoodev.studentcrud.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * DateUtils - Utility functions for date and time operations
 */
object DateUtils {

    // ========================================
    // DATE FORMATTING
    // ========================================

    /**
     * Format timestamp to full date string
     * Example: "Monday, January 27, 2026"
     */
    fun formatFullDate(timestamp: Long): String {
        val sdf = SimpleDateFormat(Constants.DATE_FORMAT_FULL, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Format timestamp to short date string
     * Example: "Jan 27, 2026"
     */
    fun formatShortDate(timestamp: Long): String {
        val sdf = SimpleDateFormat(Constants.DATE_FORMAT_SHORT, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Format timestamp to day and month only
     * Example: "Jan 27"
     */
    fun formatDayMonth(timestamp: Long): String {
        val sdf = SimpleDateFormat(Constants.DATE_FORMAT_DAY_MONTH, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Get day name from timestamp
     * Example: "Monday"
     */
    fun getDayName(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.SUNDAY -> Constants.SUNDAY
            Calendar.MONDAY -> Constants.MONDAY
            Calendar.TUESDAY -> Constants.TUESDAY
            Calendar.WEDNESDAY -> Constants.WEDNESDAY
            Calendar.THURSDAY -> Constants.THURSDAY
            Calendar.FRIDAY -> Constants.FRIDAY
            Calendar.SATURDAY -> Constants.SATURDAY
            else -> ""
        }
    }

    /**
     * Get current day name
     * Example: "Monday"
     */
    fun getCurrentDayName(): String {
        return getDayName(System.currentTimeMillis())
    }

    /**
     * Get tomorrow's day name
     */
    fun getTomorrowDayName(): String {
        return getDayName(System.currentTimeMillis() + Constants.DAY_IN_MILLIS)
    }

    // ========================================
    // RELATIVE TIME
    // ========================================

    /**
     * Get relative time string
     * Examples: "Today", "Tomorrow", "Yesterday", "2 days ago", "In 3 days"
     */
    fun getRelativeTimeString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = timestamp - now

        return when {
            isToday(timestamp) -> "Today"
            isTomorrow(timestamp) -> "Tomorrow"
            isYesterday(timestamp) -> "Yesterday"
            diff < 0 -> {
                val days = (-diff / Constants.DAY_IN_MILLIS).toInt()
                when {
                    days == 1 -> "1 day ago"
                    days < 7 -> "$days days ago"
                    days < 30 -> "${days / 7} week${if (days / 7 > 1) "s" else ""} ago"
                    else -> formatShortDate(timestamp)
                }
            }
            else -> {
                val days = (diff / Constants.DAY_IN_MILLIS).toInt()
                when {
                    days == 1 -> "In 1 day"
                    days < 7 -> "In $days days"
                    days < 30 -> "In ${days / 7} week${if (days / 7 > 1) "s" else ""}"
                    else -> formatShortDate(timestamp)
                }
            }
        }
    }

    /**
     * Get time remaining until timestamp
     * Examples: "2 hours left", "30 minutes left", "Overdue"
     */
    fun getTimeRemaining(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = timestamp - now

        return when {
            diff < 0 -> "Overdue"
            diff < Constants.HOUR_IN_MILLIS -> {
                val minutes = (diff / 60000).toInt()
                "$minutes minute${if (minutes != 1) "s" else ""} left"
            }
            diff < Constants.DAY_IN_MILLIS -> {
                val hours = (diff / Constants.HOUR_IN_MILLIS).toInt()
                "$hours hour${if (hours != 1) "s" else ""} left"
            }
            else -> {
                val days = (diff / Constants.DAY_IN_MILLIS).toInt()
                "$days day${if (days != 1) "s" else ""} left"
            }
        }
    }

    // ========================================
    // DATE COMPARISONS
    // ========================================

    /**
     * Check if timestamp is today
     */
    fun isToday(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        val todayYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = timestamp
        val targetDay = calendar.get(Calendar.DAY_OF_YEAR)
        val targetYear = calendar.get(Calendar.YEAR)

        return today == targetDay && todayYear == targetYear
    }

    /**
     * Check if timestamp is tomorrow
     */
    fun isTomorrow(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.get(Calendar.DAY_OF_YEAR)
        val tomorrowYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = timestamp
        val targetDay = calendar.get(Calendar.DAY_OF_YEAR)
        val targetYear = calendar.get(Calendar.YEAR)

        return tomorrow == targetDay && tomorrowYear == targetYear
    }

    /**
     * Check if timestamp is yesterday
     */
    fun isYesterday(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = calendar.get(Calendar.DAY_OF_YEAR)
        val yesterdayYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = timestamp
        val targetDay = calendar.get(Calendar.DAY_OF_YEAR)
        val targetYear = calendar.get(Calendar.YEAR)

        return yesterday == targetDay && yesterdayYear == targetYear
    }

    /**
     * Check if timestamp is in this week
     */
    fun isThisWeek(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = timestamp
        val targetWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val targetYear = calendar.get(Calendar.YEAR)

        return currentWeek == targetWeek && currentYear == targetYear
    }

    // ========================================
    // DATE CALCULATIONS
    // ========================================

    /**
     * Get start of day timestamp (00:00:00)
     */
    fun getStartOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * Get end of day timestamp (23:59:59)
     */
    fun getEndOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    /**
     * Get start of week timestamp (Monday 00:00:00)
     */
    fun getStartOfWeek(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return getStartOfDay(calendar.timeInMillis)
    }

    /**
     * Get end of week timestamp (Sunday 23:59:59)
     */
    fun getEndOfWeek(timestamp: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        return getEndOfDay(calendar.timeInMillis)
    }

    /**
     * Add days to timestamp
     */
    fun addDays(timestamp: Long, days: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.timeInMillis
    }

    /**
     * Get days between two timestamps
     */
    fun getDaysBetween(start: Long, end: Long): Int {
        return ((end - start) / Constants.DAY_IN_MILLIS).toInt()
    }
}
