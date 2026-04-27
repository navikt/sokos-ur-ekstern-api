package no.nav.sokos.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KMarkerFactory

private val teamLogsMarker = KMarkerFactory.getMarker("TEAM_LOGS")

fun KLogger.secureInfo(throwable: Throwable? = null, message: () -> Any?) = info(teamLogsMarker, throwable, message)

fun KLogger.secureWarn(throwable: Throwable? = null, message: () -> Any?) = warn(teamLogsMarker, throwable, message)

fun KLogger.secureError(throwable: Throwable? = null, message: () -> Any?) = error(teamLogsMarker, throwable, message)

fun KLogger.secureDebug(throwable: Throwable? = null, message: () -> Any?) = debug(teamLogsMarker, throwable, message)
