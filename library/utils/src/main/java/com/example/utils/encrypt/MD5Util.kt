package com.example.utils.encrypt

import java.security.MessageDigest

object MD5Util {

    private val hexDigIts =
        arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

    /**
     * MD5加密
     */
    fun md5Encode(origin: String): String? {
        return md5Encode(origin, "utf-8")
    }

    /**
     * MD5加密
     *
     * @param origin      字符
     * @param charsetName 编码
     */
    fun md5Encode(origin: String, charsetName: String?): String? {
        var resultString = origin
        try {
            val md = MessageDigest.getInstance("MD5")
            resultString = if (charsetName.isNullOrEmpty()) {
                byteArrayToHexString(md.digest(origin.toByteArray()))
            } else {
                byteArrayToHexString(md.digest(origin.toByteArray(charset(charsetName))))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultString
    }

    private fun byteArrayToHexString(b: ByteArray): String {
        val resultSb = StringBuilder()
        for (i in b.indices) {
            resultSb.append(byteToHexString(b[i]))
        }
        return resultSb.toString()
    }

    private fun byteToHexString(b: Byte): String? {
        var n = b.toInt()
        if (n < 0) {
            n += 256
        }
        val d1 = n / 16
        val d2 = n % 16
        return hexDigIts[d1] + hexDigIts[d2]
    }

}