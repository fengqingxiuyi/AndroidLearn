package com.example.utils.encrypt

import com.example.utils.ext.bytes2Hex
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val SHA_TYPE = "HmacSHA256"

fun String.hmacsha256(key: String): String {
  val mac = Mac.getInstance(SHA_TYPE)
  val utf8 = Charset.forName("UTF-8")
  val signingKey = SecretKeySpec(key.toByteArray(utf8), SHA_TYPE)
  mac.init(signingKey)
  val resultBytes = mac.doFinal(toByteArray(utf8))
  return resultBytes.base64Encode()
}

fun String.md5(): String {
  return encrypt(this, "MD5")
}

fun String.sha256(): String = encrypt(this, "SHA-256")

/**
 * Method to get encrypted string.
 */
private fun encrypt(string: String?, type: String): String {
  if (string.isNullOrEmpty()) {
    return ""
  }
  val md5: MessageDigest
  return try {
    md5 = MessageDigest.getInstance(type)
    val bytes = md5.digest(string.toByteArray())
    bytes.bytes2Hex()
  } catch (e: NoSuchAlgorithmException) {
    ""
  }
}