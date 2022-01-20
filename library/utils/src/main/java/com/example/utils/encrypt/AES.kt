package com.example.utils.encrypt

import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES加密
 * @param keyString 加密的Key
 * @param stringToEncode 需要加密的String
 * @return
 * @throws NullPointerException
 */
private const val AES_MODE = "AES/CBC/PKCS7Padding"
fun String.aesEncrypt(keyString: String, iv: ByteArray): String {
  require(keyString.isNotEmpty())
  require(isNotEmpty())
  try {
    val keySpec = getKey(keyString)
    val clearText = toByteArray(charset("UTF8"))
    // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
    val ivParameterSpec = IvParameterSpec(iv)

    val cipher = Cipher.getInstance(AES_MODE)
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)
    return android.util.Base64.encodeToString(cipher.doFinal(clearText), android.util.Base64.DEFAULT).replace("\n", "")
  } catch (e: InvalidKeyException) {
    e.printStackTrace()
  } catch (e: UnsupportedEncodingException) {
    e.printStackTrace()
  } catch (e: NoSuchAlgorithmException) {
    e.printStackTrace()
  } catch (e: BadPaddingException) {
    e.printStackTrace()
  } catch (e: NoSuchPaddingException) {
    e.printStackTrace()
  } catch (e: IllegalBlockSizeException) {
    e.printStackTrace()
  } catch (e: InvalidAlgorithmParameterException) {
    e.printStackTrace()
  } catch (e: NullPointerException) {
    e.printStackTrace()
  }

  return ""
}

fun String.aesDecrypt(keyString: String, iv: ByteArray): String {
  require(keyString.isNotEmpty())
  require(isNotEmpty())
  try {
    val keySpec = getKey(keyString)
    val clearText = base64Decode()
    val ivParameterSpec = IvParameterSpec(iv)
    val cipher = Cipher.getInstance(AES_MODE)
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)
    return String( cipher.doFinal(clearText))
  } catch (e: InvalidKeyException) {
    e.printStackTrace()
  } catch (e: UnsupportedEncodingException) {
    e.printStackTrace()
  } catch (e: NoSuchAlgorithmException) {
    e.printStackTrace()
  } catch (e: BadPaddingException) {
    e.printStackTrace()
  } catch (e: NoSuchPaddingException) {
    e.printStackTrace()
  } catch (e: IllegalBlockSizeException) {
    e.printStackTrace()
  } catch (e: InvalidAlgorithmParameterException) {
    e.printStackTrace()
  } catch (e: NullPointerException) {
    e.printStackTrace()
  }

  return ""
}

/**
 * Generates a SecretKeySpec for given password
 *
 * @param password
 * @return SecretKeySpec
 * @throws UnsupportedEncodingException
 */
@Throws(UnsupportedEncodingException::class)
private fun getKey(password: String): SecretKeySpec {

  // You can change it to 128 if you wish
  val keyLength = 256
  val keyBytes = ByteArray(keyLength / 8)
  Arrays.fill(keyBytes, 0x0.toByte())
  // if password is shorter then key length, it will be zero-padded
  // to key length
  val passwordBytes = password.toByteArray(charset("UTF-8"))
  val length = keyBytes.size
  System.arraycopy(passwordBytes, 0, keyBytes, 0, length)
  return SecretKeySpec(password.toByteArray(charset("UTF-8")), "AES")
}

fun generateAesKey(): String {
  return UUID.randomUUID().toString().replace("-", "")
}
