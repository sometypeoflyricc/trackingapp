package com.example.trackingapp.domain

/**
 * Computes progress percentage as an integer in [0, 100].
 *
 * @param total accumulated value of minutes done.
 * @param target target value to reach. Negative values are treated as 0% progress.
 * @return clamped percentage in [0, 100].
 */
fun percentComplete(total: Int, target: Int): Int {

    if (target <= 0) return 0
    val pct = (100.0 * total / target).toInt()
    return pct.coerceIn(0, 100)
}