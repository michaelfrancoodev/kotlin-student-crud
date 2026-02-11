package com.michaelfrancoodev.studentcrud.util

import android.content.Context
import android.content.SharedPreferences

/**
 * PreferencesManager - Manages app preferences and settings
 * Uses SharedPreferences for simple key-value storage
 */
class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constants.PREFS_NAME,
        Context.MODE_PRIVATE
    )

    // ========================================
    // THEME
    // ========================================

    /**
     * Get theme mode
     * Returns: "light", "dark", or "system"
     */
    fun getThemeMode(): String {
        return prefs.getString(Constants.PREF_THEME_MODE, "system") ?: "system"
    }

    /**
     * Set theme mode
     * @param mode "light", "dark", or "system"
     */
    fun setThemeMode(mode: String) {
        prefs.edit().putString(Constants.PREF_THEME_MODE, mode).apply()
    }

    /**
     * Check if dark mode is enabled
     */
    fun isDarkMode(): Boolean {
        return getThemeMode() == "dark"
    }

    // ========================================
    // FIRST RUN
    // ========================================

    /**
     * Check if this is the first time the app is launched
     */
    fun isFirstRun(): Boolean {
        return prefs.getBoolean(Constants.PREF_FIRST_RUN, true)
    }

    /**
     * Mark first run as complete
     */
    fun setFirstRunComplete() {
        prefs.edit().putBoolean(Constants.PREF_FIRST_RUN, false).apply()
    }

    // ========================================
    // STREAK TRACKING
    // ========================================

    /**
     * Get last streak date (timestamp)
     */
    fun getLastStreakDate(): Long {
        return prefs.getLong(Constants.PREF_LAST_STREAK_DATE, 0L)
    }

    /**
     * Update last streak date to today
     */
    fun updateStreakDate() {
        val today = DateUtils.getStartOfDay()
        prefs.edit().putLong(Constants.PREF_LAST_STREAK_DATE, today).apply()
    }

    /**
     * Check if streak should be maintained or reset
     * Returns true if streak is still valid (logged in yesterday or today)
     */
    fun isStreakValid(): Boolean {
        val lastDate = getLastStreakDate()
        if (lastDate == 0L) return false

        val today = DateUtils.getStartOfDay()
        val yesterday = today - Constants.DAY_IN_MILLIS

        return lastDate == today || lastDate == yesterday
    }

    // ========================================
    // SEMESTER
    // ========================================

    /**
     * Get current semester setting
     * Returns: "Semester 1" or "Semester 2"
     */
    fun getCurrentSemester(): String {
        return prefs.getString(Constants.PREF_CURRENT_SEMESTER, Constants.SEMESTER_1)
            ?: Constants.SEMESTER_1
    }

    /**
     * Set current semester
     */
    fun setCurrentSemester(semester: String) {
        prefs.edit().putString(Constants.PREF_CURRENT_SEMESTER, semester).apply()
    }

    // ========================================
    // NOTIFICATIONS
    // ========================================

    /**
     * Check if class reminders are enabled
     */
    fun areClassRemindersEnabled(): Boolean {
        return prefs.getBoolean("class_reminders_enabled", true)
    }

    /**
     * Set class reminders enabled state
     */
    fun setClassRemindersEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("class_reminders_enabled", enabled).apply()
    }

    /**
     * Get class reminder time (minutes before class)
     */
    fun getClassReminderTime(): Int {
        return prefs.getInt("class_reminder_time", 15)
    }

    /**
     * Set class reminder time
     */
    fun setClassReminderTime(minutes: Int) {
        prefs.edit().putInt("class_reminder_time", minutes).apply()
    }

    /**
     * Check if task deadline reminders are enabled
     */
    fun areTaskRemindersEnabled(): Boolean {
        return prefs.getBoolean("task_reminders_enabled", true)
    }

    /**
     * Set task reminders enabled state
     */
    fun setTaskRemindersEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("task_reminders_enabled", enabled).apply()
    }

    /**
     * Check if streak reminders are enabled
     */
    fun areStreakRemindersEnabled(): Boolean {
        return prefs.getBoolean("streak_reminders_enabled", true)
    }

    /**
     * Set streak reminders enabled state
     */
    fun setStreakRemindersEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("streak_reminders_enabled", enabled).apply()
    }

    // ========================================
    // STUDY GOALS
    // ========================================

    /**
     * Get daily study goal (hours)
     */
    fun getDailyStudyGoal(): Int {
        return prefs.getInt("daily_study_goal", Constants.DEFAULT_DAILY_GOAL)
    }

    /**
     * Set daily study goal
     */
    fun setDailyStudyGoal(hours: Int) {
        prefs.edit().putInt("daily_study_goal", hours).apply()
    }

    /**
     * Get weekly study goal (hours)
     */
    fun getWeeklyStudyGoal(): Int {
        return prefs.getInt("weekly_study_goal", Constants.DEFAULT_WEEKLY_GOAL)
    }

    /**
     * Set weekly study goal
     */
    fun setWeeklyStudyGoal(hours: Int) {
        prefs.edit().putInt("weekly_study_goal", hours).apply()
    }

    // ========================================
    // ONBOARDING
    // ========================================

    /**
     * Check if onboarding is complete
     */
    fun isOnboardingComplete(): Boolean {
        return prefs.getBoolean("onboarding_complete", false)
    }

    /**
     * Mark onboarding as complete
     */
    fun setOnboardingComplete() {
        prefs.edit().putBoolean("onboarding_complete", true).apply()
    }

    // ========================================
    // UTILITY
    // ========================================

    /**
     * Clear all preferences (for testing/logout)
     */
    fun clearAll() {
        prefs.edit().clear().apply()
    }

    /**
     * Save a custom string value
     */
    fun saveString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    /**
     * Get a custom string value
     */
    fun getString(key: String, defaultValue: String = ""): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    /**
     * Save a custom int value
     */
    fun saveInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    /**
     * Get a custom int value
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return prefs.getInt(key, defaultValue)
    }

    /**
     * Save a custom boolean value
     */
    fun saveBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    /**
     * Get a custom boolean value
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    /**
     * Save a custom long value
     */
    fun saveLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    /**
     * Get a custom long value
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return prefs.getLong(key, defaultValue)
    }
}
