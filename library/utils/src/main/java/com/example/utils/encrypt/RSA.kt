package com.example.utils.encrypt

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


private const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
private const val ENCRYPT_MAX_SIZE = 117

fun String.rsaEncrypt(publicKey: String): String {
  val result = rsaEncrypt(toByteArray(), publicKey)
  return result.base64Encode().replace("\n", "")
}

/**
 * 公钥加密
 */
fun rsaEncrypt(byteArray: ByteArray, publicKey: String): ByteArray {
  val key = string2PublicKey(publicKey)
  val cipher = Cipher.getInstance(TRANSFORMATION)
  cipher.init(Cipher.ENCRYPT_MODE, key)
  var temp: ByteArray?
  var offset = 0
  val outputStream = ByteArrayOutputStream()
  while (byteArray.size - offset > 0) {
    if (byteArray.size - offset >= ENCRYPT_MAX_SIZE) {
      temp = cipher.doFinal(byteArray, offset, ENCRYPT_MAX_SIZE)
      offset += ENCRYPT_MAX_SIZE
    } else {
      temp = cipher.doFinal(byteArray, offset, (byteArray.size - offset))
      offset = byteArray.size
    }
    outputStream.write(temp)
  }
  outputStream.close()
  return outputStream.toByteArray()
}

/**
 * 将base64编码后的公钥字符串转成PublicKey实例
 */
private fun string2PublicKey(publicKeyString: String): PublicKey {
  val byteArray: ByteArray = publicKeyString.base64Decode()
  val keySpec = X509EncodedKeySpec(byteArray)
  val keyFactory = KeyFactory.getInstance("RSA")
  return keyFactory.generatePublic(keySpec)
}

fun String.base64Decode(): ByteArray {
  return Base64.decode(this, Base64.DEFAULT)
}

fun ByteArray.base64Encode(): String {
  return Base64.encodeToString(this, Base64.DEFAULT)
}
