package com.ankitwala.myapplication

data class RawTransaction(
    val x: X
)

data class X(
    val out: List<Out>,
    val hash: String,
    val time: Long
)

data class Out(
    val value: Long
)
