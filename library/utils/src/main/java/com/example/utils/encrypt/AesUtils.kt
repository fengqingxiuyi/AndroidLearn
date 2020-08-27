package com.example.utils.encrypt

import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

/**
 * AES 实现加密解密
 */
object AesUtils {

    private const val DEFAULT_CODING = "utf-8"
    private const val AES = "AES"
    private const val MD5 = "MD5"
    //算法/模式/补码方式
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"

    /**
     * 加密
     */
    @Throws(Exception::class)
    fun encrypt(content: String, key: String): String {
        val input = content.toByteArray(charset(DEFAULT_CODING))
        val md = MessageDigest.getInstance(MD5)
        val digest = md.digest(key.toByteArray(charset(DEFAULT_CODING)))
        val skc = SecretKeySpec(digest, AES)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, skc)
        val cipherText = ByteArray(cipher.getOutputSize(input.size))
        return Base64Util.encode(cipherText)
    }

    /**
     * 将二进制转换成16进制
     */
    fun parseByte2HexStr(buf: ByteArray): String {
        val sb = StringBuffer()
        for (i in buf.indices) {
            var hex = Integer.toHexString((buf[i] and 0xFF.toByte()).toInt())
            if (hex.length == 1) {
                hex = "0$hex"
            }
            sb.append(hex.toUpperCase(Locale.getDefault()))
        }
        return sb.toString()
    }

    /**
     * 解密
     */
    @Throws(Exception::class)
    fun decrypt(encrypted: String, seed: String): String {
        val keyb = seed.toByteArray(charset(DEFAULT_CODING))
        val md = MessageDigest.getInstance(MD5)
        val thedigest = md.digest(keyb)
        val skey = SecretKeySpec(thedigest, AES)
        val dcipher = Cipher.getInstance(AES)
        dcipher.init(Cipher.DECRYPT_MODE, skey)
        val clearbyte = dcipher.doFinal(toByte(encrypted))
        return String(clearbyte)
    }

    /**
     * 字符串转字节数组
     */
    private fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).toByte()
        }
        return result
    }

    /**
     * 将16进制转换为二进制
     */
    fun parseHexStr2Byte(hexStr: String): ByteArray? {
        if (hexStr.isEmpty()) return null
        val result = ByteArray(hexStr.length / 2)
        for (i in 0 until hexStr.length / 2) {
            val high = hexStr.substring(i * 2, i * 2 + 1).toInt(16)
            val low = hexStr.substring(i * 2 + 1, i * 2 + 2).toInt(16)
            result[i] = (high * 16 + low).toByte()
        }
        return result
    }

    // 加密
    @Throws(Exception::class)
    fun Encrypt(sSrc: String, sKey: String?): String? {
        if (sKey == null) {
            println("Key为空null")
            return null
        }
        // 判断Key是否为16位
        if (sKey.length != 16) {
            println("Key长度不是16位")
            return null
        }
        val raw = sKey.toByteArray(charset(DEFAULT_CODING))
        val skeySpec = SecretKeySpec(raw, AES)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        val encrypted = cipher.doFinal(sSrc.toByteArray(charset(DEFAULT_CODING)))
        return Base64Util.encode(encrypted) //此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
}