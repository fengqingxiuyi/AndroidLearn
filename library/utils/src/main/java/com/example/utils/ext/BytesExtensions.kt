package com.example.utils.ext

fun ByteArray.toLong(bigEndian: Boolean = true): Long {
  var result = 0L
  for (i in 0 until 8) {
    val byte = if (bigEndian) this[i] else this[7 - i]
    result = result.or(byte.toLong().and(0xff).shl(64 - ((i + 1) * 8)))
  }
  return result
}

fun ByteArray.toInt(bigEndian: Boolean = true): Int {
  var result = 0
  for (i in 0 until 4) {
    val byte = if (bigEndian) this[i] else this[3 - i]
    result = result.or(byte.toInt().and(0xff).shl(32 - ((i + 1) * 8)))
  }
  return result
}