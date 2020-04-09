package com.ivoberger.timbersentry

import android.util.Log
import io.sentry.core.Breadcrumb
import io.sentry.core.Sentry
import io.sentry.core.SentryEvent
import io.sentry.core.SentryLevel
import io.sentry.core.protocol.Message
import timber.log.Timber

class SentryTree(
    private val minCapturePriority: Int = Log.ERROR,
    private val minBreadcrumbPriority: Int = Log.INFO,
    private val clearContext: Boolean = true,
    private val extrasDelimiter: Char = '|'
) : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= minBreadcrumbPriority
    }

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        val level: SentryLevel = when (priority) {
            Log.ASSERT -> SentryLevel.FATAL
            Log.ERROR -> SentryLevel.ERROR
            Log.WARN -> SentryLevel.WARNING
            Log.INFO -> SentryLevel.INFO
            Log.DEBUG -> SentryLevel.DEBUG
            Log.VERBOSE -> SentryLevel.DEBUG
            else -> return
        }

        if (priority >= minCapturePriority) {
            val event = buildEvent(level, tag, message, throwable)
            Sentry.captureEvent(event)
            if (clearContext) {
                Sentry.clearBreadcrumbs()
            }

        } else {
            val breadcrumb = Breadcrumb()
            breadcrumb.level = level
            breadcrumb.message = message
            Sentry.addBreadcrumb(breadcrumb)
        }
    }

    private fun buildEvent(level: SentryLevel, tag: String?, message: String, throwable: Throwable?): SentryEvent {
        val (messageContent, extras) = splitMessageAndExtras(message)

        val event = SentryEvent()
        event.level = level
        event.message = Message()
        event.message.formatted = messageContent
        tag?.let {
            event.setTag("LogTag", it)
        }
        extras.forEach { (label, extra) ->
            event.setExtra(label, extra)
        }
        throwable?.let {
            event.setThrowable(throwable)
        }

        return event
    }

    private fun splitMessageAndExtras(message: String): Pair<String, Map<String, String>> {
        val extras = mutableMapOf<String, String>()
        extras["__timberOriginalMessage"] = message

        val messageParts = message.split(extrasDelimiter)
        if (messageParts.size.rem(2) == 0) {
            return message to extras // fallback
        }

        for (index in 1 until messageParts.size step 2) {
            extras[messageParts[index]] = messageParts[index+1]
        }

        return messageParts[0] to extras
    }

}
