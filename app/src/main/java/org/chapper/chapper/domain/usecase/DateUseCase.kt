package org.chapper.chapper.domain.usecase

import java.util.*

object DateUseCase {
    fun isDateInCurrentWeek(date: Date): Boolean {
        val currentCalendar = Calendar.getInstance()
        val week = currentCalendar.get(Calendar.WEEK_OF_YEAR)
        val year = currentCalendar.get(Calendar.YEAR)
        val targetCalendar = Calendar.getInstance()
        targetCalendar.time = date
        val targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR)
        val targetYear = targetCalendar.get(Calendar.YEAR)
        return week == targetWeek && year == targetYear
    }
}