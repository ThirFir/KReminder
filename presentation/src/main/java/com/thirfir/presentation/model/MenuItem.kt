package com.thirfir.presentation.model

data class MenuItem(
    val icon: Int? = null,
    val title: String,
    val onClick: () -> Unit
)