import java.lang.Math.*
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
    val timeZone = -6.0
    val latitude = 40.0
    val longitude = -105.0
    val julianDay = getJulianDay(year, month, day, timeInDay, timeZone)
    val julianCentury = getJulianCentury(julianDay)
    val gmls = getGeomMeanLongSunInDegree(julianCentury)
    val gmas = getGeomMeanAnomSunInDegree(julianCentury)
    val eccentEarthOrbit = getEccentEarthOrbit(julianCentury)
    val sunEqOfCenter = getSunEqOfCenter(julianCentury, gmas)
    val sunTrueLong = getSunTrueLongInDegree(gmls, sunEqOfCenter)
    val sunTrueAnom = getSunTrueAnomInDegree(gmas, sunEqOfCenter)
    val sunAppLong = getSunAppLongInDegree(julianCentury, sunTrueLong)
    val meanObliqEcliptic = getMeanObliqEclipticInDegree(julianCentury)
    val obliqCorr = getObliqCorrInDegree(julianCentury, meanObliqEcliptic)
    val sunRtAscen = getSunRtAscenInDegree(sunAppLong, obliqCorr)
    val sunDeclin = getSunDeclinInDegree(sunAppLong, obliqCorr)
    val varY = getVarY(obliqCorr)
    val equationOfTime = getEquationOfTimeInMimutes(gmls, gmas, eccentEarthOrbit, varY)
    val hASunrise = getHASunriseInDegree(latitude, sunDeclin)
    val solarNoonTime = getSolarNoonTimeInDay(longitude, timeZone, equationOfTime)
    val sunriseTime = getSunriseTimeInDay(hASunrise, solarNoonTime)
    val sunsetTime = getSunsetTimeInDay(hASunrise, solarNoonTime)
    val trueSolarTime = getTrueSolarTimeInMinutes(longitude, timeZone, timeInDay, equationOfTime)
    val hourAngle = getHourAngle(trueSolarTime)
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

fun getGeomMeanAnomSunInDegree(julianCentury: Double): Double {
    val gmas = 357.52911+julianCentury*(35999.05029-0.0001537*julianCentury)
    println(gmas)
    return gmas
}

fun getEccentEarthOrbit(julianCentury: Double): Double {
    val eeo = 0.016708634 - julianCentury * (0.000042037 + 0.0000001267 * julianCentury)
    println(eeo)
    return eeo
}

fun getSunEqOfCenter(julianCentury: Double, geomMeanAnomSunInDegree: Double): Double {
    val sunEqOfCenter = sin(toRadians(geomMeanAnomSunInDegree)) *
            (1.914602 - julianCentury * (0.004817 + 0.000014 * julianCentury)) +
            sin(toRadians(2 * geomMeanAnomSunInDegree)) * (0.019993 - 0.000101 * julianCentury) +
            sin(toRadians(3 * geomMeanAnomSunInDegree)) * 0.000289
    println(sunEqOfCenter)
    return sunEqOfCenter
}

fun getSunTrueLongInDegree(geomMeanLongSunInDegree: Double, sunEqOfCenter: Double): Double {
    val sunTrueLongInDegree = geomMeanLongSunInDegree + sunEqOfCenter
    println(sunTrueLongInDegree)
    return sunTrueLongInDegree
}

fun getSunTrueAnomInDegree(geomMeanAnomSunInDegree: Double, sunEqOfCenter: Double): Double {
    val sunTrueAnomInDegree = geomMeanAnomSunInDegree + sunEqOfCenter
    println(sunTrueAnomInDegree)
    return sunTrueAnomInDegree
}

fun getSunAppLongInDegree(julianCentury: Double, sunTrueLongInDegree: Double): Double {
    val sunAppLongInDegree = sunTrueLongInDegree - 0.00569 - 0.00478 * sin(toRadians(125.04 - 1934.136 * julianCentury))
    print(sunAppLongInDegree)
    return sunAppLongInDegree
}

fun getMeanObliqEclipticInDegree(julianCentury: Double): Double {
    val moe = 23 + (26 + ((21.448 - julianCentury * (46.815 + julianCentury * (0.00059 - julianCentury * 0.001813)))) / 60) / 60
    print(moe)
    return moe
}

fun getObliqCorrInDegree(julianCentury: Double, meanObliqEclipticInDegree: Double): Double {
    val obliqCorr = meanObliqEclipticInDegree + 0.00256 * cos(toRadians(125.04 - 1934.136 * julianCentury))
    println(obliqCorr)
    return obliqCorr
}

fun getSunRtAscenInDegree(sunAppLong: Double, obliqCorr: Double): Double {
    val sunRtAscen = toDegrees(atan2(cos(toRadians(sunAppLong)), cos(toRadians(obliqCorr)) * sin(toRadians(sunAppLong))))
    println(sunRtAscen)
    return sunRtAscen
}

fun getSunDeclinInDegree(sunAppLong: Double, obliqCorr: Double): Double {
    val sunDeclin = toDegrees(asin(sin(toRadians(obliqCorr))*sin(toRadians(sunAppLong))))
    println(sunDeclin)
    return sunDeclin
}

fun getVarY(obliqCorr: Double): Double {
    val varY = tan(toRadians(obliqCorr / 2)) * tan(toRadians(obliqCorr / 2))
    println(varY)
    return varY
}

fun getEquationOfTimeInMimutes(geomMeanLongSunInDegree: Double, geomMeanAnomSunInDegree: Double,
                               eccentEarthOrbit: Double, varY: Double): Double {
    val equationOfTime = 4 * toDegrees(varY * sin(2 * toRadians(geomMeanLongSunInDegree)) -
            2 * eccentEarthOrbit * sin(toRadians(geomMeanAnomSunInDegree)) + 4 * eccentEarthOrbit *
            varY * sin(toRadians(geomMeanAnomSunInDegree)) * cos(2 * toRadians(geomMeanLongSunInDegree)) -
            0.5 * varY * varY * sin(4 * toRadians(geomMeanLongSunInDegree)) - 1.25 *
            eccentEarthOrbit * eccentEarthOrbit*sin(2* toRadians(geomMeanAnomSunInDegree)))
    println(equationOfTime)
    return equationOfTime
}

fun getHASunriseInDegree(latitude: Double, sunDeclinInDegree: Double): Double {
    val hASunrise = toDegrees(acos(cos(toRadians(90.833)) / (cos(toRadians(latitude)) *
            cos(toRadians(sunDeclinInDegree)))-tan(toRadians(latitude))*tan(toRadians(sunDeclinInDegree))))
    println(hASunrise)
    return hASunrise
}

fun getSolarNoonTimeInDay(longitude: Double, timeZone: Double, equationOfTime: Double): Double {
    val solarNoonTimeInDay = (720.0 - 4.0 * longitude - equationOfTime + timeZone * 60.0) / 1440.0
    println(solarNoonTimeInDay)
    return solarNoonTimeInDay
}

fun getSunriseTimeInDay(hASunrise: Double, solarNoonInDay: Double): Double {
    val sunriseTimeInDay = solarNoonInDay - hASunrise * 4 / 1440
    println(sunriseTimeInDay)
    return sunriseTimeInDay
}

fun getSunsetTimeInDay(hASunrise: Double, solarNoonInDay: Double): Double {
    val sunsetTime = solarNoonInDay + hASunrise * 4.0 / 1440.0
    println(sunsetTime)
    return sunsetTime
}

fun getTrueSolarTimeInMinutes(longitude: Double, timeZone: Double, timeInDay: Double, equationOfTime: Double): Double {
    val trueSolarTime = abs(1440 + ((timeInDay * 1440.0) + equationOfTime + (4 * longitude) - (60.0 * timeZone))).rem(1440)
    println(trueSolarTime)
    return trueSolarTime
}

fun getHourAngle(truSolarTime: Double): Double {
    val hourAngle = if(truSolarTime/4 < 0) truSolarTime / 4 + 180 else truSolarTime / 4 - 180
    println(hourAngle)
    return hourAngle
}
