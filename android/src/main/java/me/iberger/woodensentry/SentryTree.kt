package me.iberger.woodensentry

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
    context: Context? = null,
    sentryDSN: String? = null,
    private val mMinCapturePriority: Int = Log.ERROR,
    private val mMinPriority: Int = Log.INFO,
    private val mExtrasDelimiter: Char = '|'
) : Timber.Tree() {

    init {
        when {
            context != null && sentryDSN != null ->
                Sentry.init(sentryDSN, AndroidSentryClientFactory(context))
            context != null && sentryDSN == null ->
                Sentry.init(AndroidSentryClientFactory(context))
            context == null && sentryDSN != null ->
                Sentry.init(sentryDSN)
            else -> Sentry.init()
        }
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean = priority >= mMinPriority

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
        val msgSplit = message.split(mExtrasDelimiter)
        val finalMessage = if (msgSplit.size == 3) {
            Sentry.getContext().addExtra(msgSplit[1], msgSplit[2])
            msgSplit[0]
        } else message


        Sentry.getContext().addTag("LogTag", tag)

        if (priority >= mMinCapturePriority) {
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
            t?.let { event.withSentryInterface(ExceptionInterface(it)) }
            Sentry.capture(event)
        } else Sentry.getContext().recordBreadcrumb(
            BreadcrumbBuilder().setMessage(finalMessage).setLevel(breadcrumbLevel).build()
        )
    }
}
