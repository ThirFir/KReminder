package com.thirfir.domain.util

/**
 *
 *
 * Example : "https://portal.koreatech.ac.kr/ctt/bb/bulletin".addQueryString("b", 1)
 *
 * Result : "https://portal.koreatech.ac.kr/ctt/bb/bulletin?b=1"
 *
 * @param query query string
 * @param number number of page
 * @return total url with query string
 */
fun String.addQueryString(query: String, number: Int): String {
    return if (this.contains("?"))
        "$this&$query=$number"
    else "$this?$query=$number"
}