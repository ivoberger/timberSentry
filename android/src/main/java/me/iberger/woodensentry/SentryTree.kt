package me.iberger.woodensentry

import android.util.Log
import timber.log.Timber

class SentryTree(
    private val mMinPriority: Int = Log.INFO
) : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean = priority >= mMinPriority

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
