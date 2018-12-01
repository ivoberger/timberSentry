package me.iberger.woodensentry

fun Iterable<Pair<String, Any>>.toExtras(): String =
    map { "|${it.first}|${it.second}" }.reduce { extras, current -> "$extras$current" }

fun Map<String, Any>.toExtras(): String =
    map { "|${it.key}|${it.value}" }.reduce { extras, current -> "$extras$current" }
