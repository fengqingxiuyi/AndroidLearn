package com.example.utils.encrypt

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * 通用Base64加解密
 */
object Base64Util {

    private val legalChars =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray()

    /**
     * Base64加密
     */
    fun encode(data: String?): String {
        return encode(data?.toByteArray())
    }

    /**
     * Base64加密
     */
    fun encode(data: ByteArray?): String {
        if (data == null || data.isEmpty()) {
            return ""
        }
        val len = data.size
        val buf = StringBuilder(len * 3 / 2)
        val end = len - 3
        var i = 0
        var n = 0
        while (i <= end) {
            val d = (data[i].toInt() and 0x0ff shl 16
                    or (data[i + 1].toInt() and 0x0ff shl 8)
                    or (data[i + 2].toInt() and 0x0ff))
            buf.append(legalChars[d shr 18 and 63])
            buf.append(legalChars[d shr 12 and 63])
            buf.append(legalChars[d shr 6 and 63])
            buf.append(legalChars[d and 63])
            i += 3
            if (n++ >= 14) {
                n = 0
                buf.append(" ")
            }
        }
        if (i == len - 2) {
            val d = (data[i].toInt() and 0x0ff shl 16
                    or (data[i + 1].toInt() and 255 shl 8))
            buf.append(legalChars[d shr 18 and 63])
            buf.append(legalChars[d shr 12 and 63])
            buf.append(legalChars[d shr 6 and 63])
            buf.append("=")
        } else if (i == len - 1) {
            val d = data[i].toInt() and 0x0ff shl 16
            buf.append(legalChars[d shr 18 and 63])
            buf.append(legalChars[d shr 12 and 63])
            buf.append("==")
        }
        return buf.toString()
    }

    /**
     * Base64解密
     */
    fun decodeGetStr(s: String?): String {
        return String(decode(s))
    }

    /**
     * Base64解密
     */
    fun decode(s: String?): ByteArray {
        var decodedBytes = ByteArray(0)
        if (s.isNullOrEmpty()) {
            return decodedBytes
        }
        val bos = ByteArrayOutputStream()
        try {
            decode(s, bos)
            decodedBytes = bos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return decodedBytes
    }

    @Throws(IOException::class)
    private fun decode(s: String, os: OutputStream) {
        var i = 0
        val len = s.length
        while (true) {
            while (i < len && s[i] <= ' ') i++
            if (i == len) break
            val tri = ((decode(s[i]) shl 18)
                    + (decode(s[i + 1]) shl 12)
                    + (decode(s[i + 2]) shl 6)
                    + decode(s[i + 3]))
            os.write(tri shr 16 and 255)
            if (s[i + 2] == '=') break
            os.write(tri shr 8 and 255)
            if (s[i + 3] == '=') break
            os.write(tri and 255)
            i += 4
        }
    }

    private fun decode(c: Char): Int {
        return when (c) {
            in 'A'..'Z' -> c.toInt() - 65
            in 'a'..'z' -> c.toInt() - 97 + 26
            in '0'..'9' -> c.toInt() - 48 + 26 + 26
            else -> when (c) {
                '+' -> 62
                '/' -> 63
                '=' -> 0
                else -> throw RuntimeException("unexpected code: $c")
            }
        }
    }
}