import java.util.*

fun main() {
    val date = takeInput()
    val calendar = Calendar.getInstance().apply {
        time = date
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val timeInDay = (calendar.get(Calendar.HOUR)/24.0) +
            (calendar.get(Calendar.MINUTE)/(24.0 * 60.0)) +
            (calendar.get(Calendar.SECOND)/(24.0 * 60.0 * 60.0))
    val timeZone = calendar.get(Calendar.ZONE_OFFSET) / (1000.0 * 60.0 * 60.0)
    val julianDay = getJulianDay(year, month, day, timeInDay, timeZone)
    val julianCentury = getJulianCentury(julianDay)
    val gmls = getGeomMeanLongSunInDegree(julianCentury)
}

fun takeInput(): Date {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2010)
        set(Calendar.MONTH, 6)
        set(Calendar.DAY_OF_MONTH, 21)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 6)
        set(Calendar.SECOND, 0)
    }
    return calendar.time
}

fun getJulianDay(year: Int, month: Int, day: Int, timeInDay: Double, timeZone: Double): Double {
    val a = year / 100.0
    val b = a / 4.0
    val c = 2 - a + b
    val e = 365.25 * (year + 4716.0)
    val f = 30.6001 * (month + 1.0)
    val julianDay = c + day + e + f - 1524.5 + timeInDay + timeZone / 24.0
    println(julianDay)
    return julianDay
}

fun getJulianCentury(julianDay: Double): Double {
    val julianCentury = (julianDay - 2451545) / 36525
    println(julianCentury)
    return julianCentury
}

fun getGeomMeanLongSunInDegree(julianCentury: Double): Double {
    val gmls = (280.46646+julianCentury*(36000.76983+julianCentury*0.0003032)) % 360
    println(gmls)
    return gmls
}

