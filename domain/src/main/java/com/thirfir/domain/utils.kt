package com.thirfir.domain


fun String.addQueryString(query: String, number: Int): String {
    return if (this.contains("?"))
        "$this&$query=$number"
    else "$this?$query=$number"
}