package com.example.utils.ext

fun Long.toHexString(): String = java.lang.Long.toHexString(this)

fun Int.toHexString(): String = Integer.toHexString(this)

fun Long.toByteArray(bigEndian: Boolean = false): ByteArray {
  val size = 8
  val result = ByteArray(size)
  var temp = this
  for (i in 0 until size) {
    val index = if (bigEndian) size - 1 - i else i
    result[index] = (temp and 0xff).toByte()
    temp = temp.shr(8)
  }
  return result
}

fun Int.toByteArray(bigEndian: Boolean = false): ByteArray {
  val size = 4
  val result = ByteArray(size)
  var temp = this
  for (i in 0 until size) {
    val index = if (bigEndian) size - 1 - i else i
    result[index] = (temp and 0xff).toByte()
    temp = temp.shr(8)
  }
  return result
}

fun Short.toByteArray(bigEndian: Boolean = false): ByteArray {
  val size = 2
  val result = ByteArray(size)
  var temp = this.toInt()
  for (i in 0 until size) {
    val index = if (bigEndian) size - 1 - i else i
    result[index] = (temp and 0xff).toByte()
    temp = temp.shr(8)
  }
  return result
}