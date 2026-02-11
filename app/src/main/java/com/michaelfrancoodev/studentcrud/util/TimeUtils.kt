package com.michaelfrancoodev.studentcrud.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * TimeUtils - Utility functions for time operations and formatting
 */
object TimeUtils {

    // ========================================
    // TIME FORMATTING
    // ========================================

    /**
     * Format time string to 12-hour format
     * Input: "08:00" or "14:30"
     * Output: "8:00 AM" or "2:30 PM"
     */
    fun format12Hour(time: String): String {
        return try {
            val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat(Constants.TIME_FORMAT_12H, Locale.getDefault())
            val date = inputFormat.parse(time)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            time
        }
    }

    /**
     * Format time string to 24-hour format
     * Input: "8:00 AM" or "2:30 PM"
     * Output: "08:00" or "14:30"
     */
    fun format24Hour(time: String): String {
        return try {
            val inputFormat = SimpleDateFormat(Constants.TIME_FORMAT_12H, Locale.getDefault())
            val outputFormat = SimpleDateFormat(Constants.TIME_FORMAT_24H, Locale.getDefault())
            val date = inputFormat.parse(time)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            time
        }
    }

    /**
     * Get current time in 12-hour format
     * Example: "9:30 AM"
     */
    fun getCurrentTime12Hour(): String {
        val sdf = SimpleDateFormat(Constants.TIME_FORMAT_12H, Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Get current time in 24-hour format
     * Example: "09:30"
     */
    fun getCurrentTime24Hour(): String {
        val sdf = SimpleDateFormat(Constants.TIME_FORMAT_24H, Locale.getDefault())
        return sdf.format(Date())
    }

    // ========================================
    // TIME COMPARISONS
    // ========================================

    /**
     * Compare two time strings (24-hour format)
     * Returns: -1 if time1 < time2, 0 if equal, 1 if time1 > time2
     */
    fun compareTime(time1: String, time2: String): Int {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date1 = sdf.parse(time1)
            val date2 = sdf.parse(time2)
            date1?.compareTo(date2 ?: Date()) ?: 0
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Check if current time is between start and end time
     * Times in 24-hour format (e.g., "08:00", "10:00")
     */
    fun isCurrentTimeBetween(startTime: String, endTime: String): Boolean {
        val currentTime = getCurrentTime24Hour()
        return currentTime >= startTime && currentTime <= endTime
    }

    /**
     * Check if a time has passed
     * Time in 24-hour format
     */
    fun hasTimePassed(time: String): Boolean {
        val currentTime = getCurrentTime24Hour()
        return currentTime > time
    }

    // ========================================
    // TIME CALCULATIONS
    // ========================================

    /**
     * Calculate duration between two times in minutes
     * Times in 24-hour format (e.g., "08:00", "10:00")
     * Returns: Duration in minutes
     */
    fun getDurationInMinutes(startTime: String, endTime: String): Int {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val start = sdf.parse(startTime)
            val end = sdf.parse(endTime)
            val diffMillis = (end?.time ?: 0) - (start?.time ?: 0)
            (diffMillis / 60000).toInt()
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Calculate duration between two times in hours
     * Times in 24-hour format
     * Returns: Duration in hours (decimal)
     */
    fun getDurationInHours(startTime: String, endTime: String): Float {
        val minutes = getDurationInMinutes(startTime, endTime)
        return minutes / 60f
    }

    /**
     * Format duration in minutes to readable string
     * Examples: "30 mins", "1h 30m", "2 hours"
     */
    fun formatDuration(minutes: Int): String {
        return when {
            minutes < 60 -> "$minutes mins"
            minutes % 60 == 0 -> {
                val hours = minutes / 60
                "$hours hour${if (hours > 1) "s" else ""}"
            }
            else -> {
                val hours = minutes / 60
                val mins = minutes % 60
                "${hours}h ${mins}m"
            }
        }
    }

    /**
     * Add minutes to a time string
     * Input: "08:00", 30
     * Output: "08:30"
     */
    fun addMinutes(time: String, minutes: Int): String {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.time = sdf.parse(time) ?: Date()
            calendar.add(Calendar.MINUTE, minutes)
            sdf.format(calendar.time)
        } catch (e: Exception) {
            time
        }
    }

    // ========================================
    // TIME VALIDATION
    // ========================================

    /**
     * Validate time string format (HH:mm)
     * Returns true if valid
     */
    fun isValidTime(time: String): Boolean {
        return try {
            val pattern = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
            pattern.matches(time)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Check if end time is after start time
     */
    fun isEndTimeValid(startTime: String, endTime: String): Boolean {
        return compareTime(startTime, endTime) < 0
    }

    // ========================================
    // TIME RANGES
    // ========================================

    /**
     * Get time range string
     * Input: "08:00", "10:00"
     * Output: "8:00 AM - 10:00 AM"
     */
    fun getTimeRange(startTime: String, endTime: String): String {
        val start = format12Hour(startTime)
        val end = format12Hour(endTime)
        return "$start - $end"
    }

    /**
     * Get time range with duration
     * Input: "08:00", "10:00"
     * Output: "8:00 AM - 10:00 AM (2 hours)"
     */
    fun getTimeRangeWithDuration(startTime: String, endTime: String): String {
        val range = getTimeRange(startTime, endTime)
        val duration = formatDuration(getDurationInMinutes(startTime, endTime))
        return "$range ($duration)"
    }

    // ========================================
    // TIME HELPERS
    // ========================================

    /**
     * Get greeting based on current time
     * Returns: "Good Morning", "Good Afternoon", or "Good Evening"
     */
    fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }

    /**
     * Check if it's morning (before 12:00)
     */
    fun isMorning(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour < 12
    }

    /**
     * Check if it's afternoon (12:00 - 17:00)
     */
    fun isAfternoon(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour in 12..16
    }

    /**
     * Check if it's evening (after 17:00)
     */
    fun isEvening(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour >= 17
    }

    /**
     * Generate time options for time picker (15-minute intervals)
     * Returns list of times like ["07:00", "07:15", "07:30", ...]
     */
    fun generateTimeOptions(
        startHour: Int = 7,
        endHour: Int = 22,
        intervalMinutes: Int = 15
    ): List<String> {
        val times = mutableListOf<String>()
        var hour = startHour
        var minute = 0

        while (hour < endHour || (hour == endHour && minute == 0)) {
            times.add(String.format("%02d:%02d", hour, minute))
            minute += intervalMinutes
            if (minute >= 60) {
                minute = 0
                hour++
            }
        }

        return times
    }
}
