package no.nav.sokos.logging

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KMarkerFactory

private val teamLogsMarker = KMarkerFactory.getMarker("TEAM_LOGS")

fun KLogger.secureInfo(throwable: Throwable? = null, message: () -> Any?) = info(throwable, teamLogsMarker, message)

fun KLogger.secureWarn(throwable: Throwable? = null, message: () -> Any?) = warn(throwable, teamLogsMarker, message)

fun KLogger.secureError(throwable: Throwable? = null, message: () -> Any?) = error(throwable, teamLogsMarker, message)
