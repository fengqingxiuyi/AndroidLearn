package com.example.utils.ext

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import com.example.utils.other.locale
import java.text.SimpleDateFormat
import java.util.*

fun Date.millisecondsSince(date: Date) = (time - date.time)
fun Date.secondsSince(date: Date): Double = millisecondsSince(date) / 1000.0
fun Date.minutesSince(date: Date): Double = secondsSince(date) / 60
fun Date.hoursSince(date: Date): Double = minutesSince(date) / 60
fun Date.daysSince(date: Date): Double = hoursSince(date) / 24
fun Date.weeksSince(date: Date): Double = daysSince(date) / 7
fun Date.monthsSince(date: Date): Double = weeksSince(date) / 4
fun Date.yearsSince(date: Date): Double = monthsSince(date) / 12


/**
 * get Current Date.
 */
val currentDate get() = Date(System.currentTimeMillis())

/**
 * Gives [Calendar] object from Date
 */
inline val Date.calendar: Calendar
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar
    }


/**
 * Gets  Year directly from [Calendar] Object
 */
inline val Calendar.year: Int
    get() = get(Calendar.YEAR)

/**
 * Gets value of DayOfMonth from [Calendar] Object
 */
inline val Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)

/**
 * Gets value of Month from [Calendar] Object
 */
inline val Calendar.month: Int
    get() = get(Calendar.MONTH)

/**
 * Gets value of Hour from [Calendar] Object
 */
inline val Calendar.hour: Int
    get() = get(Calendar.HOUR)

/**
 * Gets value of HourOfDay from [Calendar] Object
 */
inline val Calendar.hourOfDay: Int
    get() = get(Calendar.HOUR_OF_DAY)

/**
 * Gets value of Minute from [Calendar] Object
 */
inline val Calendar.minute: Int
    get() = get(Calendar.MINUTE)

/**
 * Gets value of Second from [Calendar] Object
 */
inline val Calendar.second: Int
    get() = get(Calendar.SECOND)

/**
 * Gets value of DayOfMonth from [Date] Object
 */
inline val Date.yearFromCalendar: Int
    get() = calendar.year

/**
 * Gets value of DayOfMonth from [Date] Object
 */
inline val Date.dayOfMonth: Int
    get() = calendar.dayOfMonth

/**
 * Gets value of Month from [Date] Object
 */
inline val Date.monthFromCalendar: Int
    get() = calendar.month

/**
 * Gets value of Hour from [Date] Object
 */
inline val Date.hour: Int
    get() = calendar.hour

/**
 * Gets value of HourOfDay from [Date] Object
 */
inline val Date.hourOfDay: Int
    get() = calendar.hourOfDay

/**
 * Gets value of Minute from [Date] Object
 */
inline val Date.minute: Int
    get() = calendar.minute


/**
 * Gets value of Second from [Date] Object
 */
inline val Date.second: Int
    get() = calendar.second


/**
 * Gets value of Milliseconds of current time
 */
inline val now: Long
    get() = Calendar.getInstance().timeInMillis

/**
 * Gets current time in given format
 */
fun getCurrentTimeInFormat(stringFormat: String): String {
    val currentTime = Date()
    return SimpleDateFormat(stringFormat, Locale.getDefault()).format(currentTime)
}

/**
 * Formats date according to device's default date format
 */
fun Context.formatDateAccordingToDevice(date: Date): String {
    val format = DateFormat.getDateFormat(this)
    return format.format(date)
}

/**
 * Formats time according to device's default time format
 */
fun Context.formatTimeAccordingToDevice(date: Date): String {
    val format = DateFormat.getTimeFormat(this)
    return format.format(date)
}

fun Date.getCurrentTimeString(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(this.time)
}

fun Date.getCurrentTimeHour(): Int {
    return this.getCurrentTimeString().split(":")[0].toInt()
}

fun Date.getCurrentTimeMinutes(): Int {
    return this.getCurrentTimeString().split(":")[1].toInt()
}

fun Date.getMonthNumber(): Int {
    val dateFormat = SimpleDateFormat("MM", Locale.getDefault())
    val mStr = dateFormat.format(this.time)
    return Integer.parseInt(mStr)
}

fun Date.getCurrentDayString(): String {
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
    return dateFormat.format(this.time)
}

fun Date.getCurrentWeekdayString(): String {
    val weekdayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return weekdayFormat.format(this.time)
}

fun Date.getCurrentDateString(pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, locale)
    return dateFormat.format(this.time)
}

// Converts current date to proper provided format
fun Date.convertTo(format: String): String? {
    var dateStr: String? = null
    val df = SimpleDateFormat(format)
    try {
        dateStr = df.format(this)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    return dateStr
}

// Converts current date to Calendar
fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun Date.isFuture(): Boolean {
    return !Date().before(this)
}

fun Date.isPast(): Boolean {
    return Date().before(this)
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isTomorrow(): Boolean {
    return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}

val Date.endOfDay: Date
    get() = with(hour = 23, minute = 59, second = 59, millisecond = 999)

fun Date.isWithinWeek():Boolean {
    val endDayDate = Calendar.getInstance().time.endOfDay
    val cDate = Calendar.getInstance()
    cDate.timeInMillis = time
    val startTime = Calendar.getInstance().apply {
        add(Calendar.DATE, -7)
    }.timeInMillis
    val endTime = endDayDate.time
    return cDate.timeInMillis in (startTime..endTime)
}

fun Date.today(): Date {
    return Date()
}

fun Date.yesterday(): Date {
    val cal = this.toCalendar()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    return cal.time
}

fun Date.tomorrow(): Date {
    val cal = this.toCalendar()
    cal.add(Calendar.DAY_OF_YEAR, 1)
    return cal.time
}

fun Date.hour(): Int {
    return this.toCalendar().get(Calendar.HOUR)
}

fun Date.minute(): Int {
    return this.toCalendar().get(Calendar.MINUTE)
}

fun Date.second(): Int {
    return this.toCalendar().get(Calendar.SECOND)
}

fun Date.month(): Int {
    return this.toCalendar().get(Calendar.MONTH) + 1
}

fun Date.monthName(locale: Locale = Locale.getDefault()): String? {
    return this.toCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, locale)
}

fun Date.year(): Int {
    return this.toCalendar().get(Calendar.YEAR)
}

fun Date.day(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_MONTH)
}

fun Date.dayOfWeek(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_WEEK)
}

fun Date.dayOfWeekName(locale: Locale = Locale.getDefault()): String? {
    return this.toCalendar().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale)
}

fun Date.dayOfYear(): Int {
    return this.toCalendar().get(Calendar.DAY_OF_YEAR)
}


fun Date.resetHourMinSecForDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}


fun Date.isDateToday(): Boolean {
    return this.resetHourMinSecForDate() == Date().resetHourMinSecForDate()
}

fun Date.isDateSameDay(compareDate: Date): Boolean {
    return this.resetHourMinSecForDate() == compareDate.resetHourMinSecForDate()
}

fun Date.format(pattern: String, locale: Locale): String {
    return SimpleDateFormat(pattern, locale).format(this)
}

fun Date.isDateThisYear(): Boolean {
    val calendar = Calendar.getInstance()

    val currentYear = calendar.get(Calendar.YEAR)
    calendar.time = this

    return currentYear == calendar.get(Calendar.YEAR)
}

fun Date.getAge(): Int {
    val birthday = Calendar.getInstance()
    birthday.time = this
    val today = Calendar.getInstance()

    var age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR)) {
        age -= 1
    }
    return age
}

fun Date.with(
    year: Int = -1,
    month: Int = -1,
    day: Int = -1,
    hour: Int = -1,
    minute: Int = -1,
    second: Int = -1,
    millisecond: Int = -1
): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    if (year > -1) calendar.set(Calendar.YEAR, year)
    if (month > 0) calendar.set(Calendar.MONTH, month - 1)
    if (day > 0) calendar.set(Calendar.DATE, day)
    if (hour > -1) calendar.set(Calendar.HOUR_OF_DAY, hour)
    if (minute > -1) calendar.set(Calendar.MINUTE, minute)
    if (second > -1) calendar.set(Calendar.SECOND, second)
    if (millisecond > -1) calendar.set(Calendar.MILLISECOND, millisecond)
    return calendar.time
}

fun Date.with(weekday: Int = -1): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    if (weekday > -1) calendar.set(Calendar.WEEK_OF_MONTH, weekday)
    return calendar.time
}

val Date.beginningOfDay: Date
    get() = with(hour = 0, minute = 0, second = 0, millisecond = 0)


