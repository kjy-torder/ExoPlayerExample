package com.ddeuda.exoplayerexample

fun Int.kiloByte(): Long = this * 1024L
fun Int.megaByte(): Long = this.kiloByte() * 1024L
fun Int.gigaByte(): Long = this.megaByte() * 1024L