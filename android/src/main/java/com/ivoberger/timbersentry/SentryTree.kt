package com.ivoberger.timbersentry

import android.content.Context
import android.util.Log
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import io.sentry.event.Breadcrumb
import io.sentry.event.BreadcrumbBuilder
import io.sentry.event.Event
import io.sentry.event.EventBuilder
import io.sentry.event.interfaces.ExceptionInterface
import timber.log.Timber

class SentryTree(
    sentryDsn: String? = null,
    context: Context? = null,
    private val minCapturePriority: Int = Log.ERROR,
    private val minBreadcrumbPriority: Int = Log.INFO,
    private val clearContext: Boolean = true,
    private val addAllExtrasToContext: Boolean = false,
    private val extrasDelimiter: Char = '|'
) : Timber.Tree() {

    init {
        when {
            context != null && sentryDsn != null ->
                Sentry.init(sentryDsn, AndroidSentryClientFactory(context.applicationContext))
            context != null && sentryDsn == null ->
                Sentry.init(AndroidSentryClientFactory(context.applicationContext))
            context == null && sentryDsn != null ->
                Sentry.init(sentryDsn)
            else -> Sentry.init()
        }
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean =
        priority >= minBreadcrumbPriority

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val breadcrumbLevel: Breadcrumb.Level = when (priority) {
            Log.ASSERT -> Breadcrumb.Level.CRITICAL
            Log.ERROR -> Breadcrumb.Level.ERROR
            Log.WARN -> Breadcrumb.Level.WARNING
            Log.INFO -> Breadcrumb.Level.INFO
            Log.DEBUG -> Breadcrumb.Level.DEBUG
            Log.VERBOSE -> Breadcrumb.Level.DEBUG
            else -> return
        }
        val msgSplit = message.split(extrasDelimiter)
        val extras = mutableMapOf<String, Any>()
        val finalMessage = if (msgSplit.size >= 3) {
            val parsedExtras = msgSplit.subList(1, msgSplit.size)
            for (index in 0 until parsedExtras.size step 2) {
                if (index == parsedExtras.size - 1) return
                extras[parsedExtras[index]] = parsedExtras[index + 1]
            }
            msgSplit[0]
        } else message


        Sentry.getContext().addTag("LogTag", tag)

        if (priority >= minCapturePriority) {
            val eventLevel: Event.Level = when (priority) {
                Log.ASSERT -> Event.Level.FATAL
                Log.ERROR -> Event.Level.ERROR
                Log.WARN -> Event.Level.WARNING
                Log.INFO -> Event.Level.INFO
                Log.DEBUG -> Event.Level.DEBUG
                Log.VERBOSE -> Event.Level.DEBUG
                else -> return
            }
            val event = EventBuilder()
                .withLevel(eventLevel)
                .withMessage(finalMessage)
            extras.forEach { (lbl, extra) -> event.withExtra(lbl, extra) }
            t?.let { event.withSentryInterface(ExceptionInterface(it)) }
            Sentry.capture(event)
            if (addAllExtrasToContext) extras.forEach { (lbl, extra) ->
                Sentry.getContext().addExtra(lbl, extra)
            }
            if (clearContext) Sentry.clearContext()
        } else {
            extras.forEach { (lbl, extra) -> Sentry.getContext().addExtra(lbl, extra) }
            Sentry.getContext().recordBreadcrumb(
                BreadcrumbBuilder().setMessage(finalMessage).setLevel(breadcrumbLevel).build()
            )
        }
    }
}
