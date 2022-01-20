package com.example.utils.ext

import android.text.SpannableStringBuilder
import android.text.SpannedString
import java.io.File
import java.net.URI
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val String.containsLetters get() = matches(".*[a-zA-Z].*".toRegex())

val String.containsNumbers get() = matches(".*[0-9].*".toRegex())


val String.isAlphanumeric get() = matches("^[a-zA-Z0-9]*$".toRegex())

val String.isAlphabetic get() = matches("^[a-zA-Z]*$".toRegex())

/**
 * Check if String is Phone Number.
 */
val String.isPhone: Boolean
  get() {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
  }

/**
 * Check if String is Email.
 */
val String.isEmail: Boolean
  get() {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
  }


/**
 * Check if String is Number.
 */
val String.isNumeric: Boolean
  get() {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
  }


/**
 * Method to check String equalsIgnoreCase
 */
fun String.equalsIgnoreCase(other: String) = this.lowercase(Locale.getDefault()).contentEquals(other.lowercase(Locale.getDefault()))


/**
 * Returns a new File Object with the Current String as Its path
 */
val String.asFile get() = File(this)


/**
 * Returns a new File Object with the Current String as Its path
 */
fun String.toFile() = File(this)

/**
 * Encode String to URL
 */
val String.encodeToUrlUTF8: String
  get() {
    return URLEncoder.encode(this, "UTF-8")
  }

/**
 * Decode String to URL
 */
val String.decodeToUrlUTF8: String
  get() {
    return URLDecoder.decode(this, "UTF-8")
  }

/**
 * Encode String to URL
 */
fun String.encodeToUrl(charSet: String = "UTF-8"): String = URLEncoder.encode(this, charSet)

/**
 * Decode String to URL
 */
fun String.decodeToUrl(charSet: String = "UTF-8"): String = URLDecoder.decode(this, charSet)

/**
 * encode The String to Binary
 */
fun String.encodeToBinary(): String {
  val stringBuilder = StringBuilder()
  toCharArray().forEach {
    stringBuilder.append(Integer.toBinaryString(it.toInt()))
    stringBuilder.append(" ")
  }
  return stringBuilder.toString()
}

/**
 * Decode the String from binary
 */
val String.deCodeToBinary: String
  get() {
    val stringBuilder = StringBuilder()
    split(" ").forEach {
      stringBuilder.append(Integer.parseInt(it.replace(" ", ""), 2))
    }
    return stringBuilder.toString()
  }

fun String.replaceX(vararg str: String): String {
  val charSequence: CharSequence = this
  return charSequence.replaceX(*str).toString()
}

fun CharSequence.replaceX(vararg str: CharSequence): CharSequence {
  val result = SpannableStringBuilder(this)
  val replacedStr = "XXX"
  when (str.size) {
    0 -> {

    }
    1 -> {
      val p = result.indexOf(replacedStr)
      if (p >= 0) {
        result.replace(p, p + replacedStr.length, str.first())
      }
    }
    else -> {
      str.forEachIndexed { index, s ->
        val p = result.indexOf(replacedStr)
        if (p >= 0) {
          val r = "$replacedStr${index + 1}"
          result.replace(p, p + r.length, s)
        }
      }
    }
  }
  return SpannedString(result)
}

/**
 * Method to convert byteArray to String.
 */
fun ByteArray.bytes2Hex(): String {
  //方式一
//    return BigInteger(1, bts).toString(16).padStart(32, '0')
  //方式二
  return joinToString(separator = "") { byte -> "%02x".format(byte) }
}

fun String.toURL(): URL = URL(this)

fun String.toURI(): URI = URI(this)

fun String.convertDateFormat(fromFormat: String, toFormat: String): String {
  return if (this.isDateStringProperlyFormatted(fromFormat)) {
    try {
      this toDate fromFormat asString toFormat
    } catch (e: Exception) {
      e.printStackTrace()
      this
    }

  } else {
    this
  }
}

fun String.isDateStringProperlyFormatted(dateFormat: String): Boolean {
  var isProperlyFormatted = false
  val format = SimpleDateFormat(dateFormat, Locale.getDefault())
  //SetLenient means dateString will be checked strictly with dateFormat. Otherwise it will format as per wish.
  format.isLenient = false
  try {
    format.parse(this)
    isProperlyFormatted = true
  } catch (e: ParseException) {
    //exception means dateString is not parsable by dateFormat. Thus dateIsProperlyFormatted.
  }

  return isProperlyFormatted
}

infix fun Date.asString(parseFormat: String): String {
  return try {
    SimpleDateFormat(parseFormat, Locale.getDefault()).format(this)
  } catch (e: Exception) {
    e.printStackTrace()
    ""
  }

}

infix fun String.toDate(currentFormat: String): Date {
  return try {
    if (this.isEmptyString()) {
      Date()
    } else {
      SimpleDateFormat(currentFormat, Locale.getDefault()).parse(this)
    }
  } catch (e: Exception) {
    e.printStackTrace()
    //If It can not be parsed, return today's date instead of null. So return value of this method does not create null pointer exception.
    Date()
  }
}

fun CharSequence.isEmptyString(): Boolean {
  return this.isEmpty() || this.toString().equals("null", true)
}

/**
 * 阿语数字 转 阿拉伯数字
 */
fun CharSequence.convertArabic(): CharSequence {
  val sb = StringBuilder(length + 16)
  for (ch in this) {
    when {
      Character.isDigit(ch) -> {
        sb.append(Character.getNumericValue(ch))
      }
      else -> {
        sb.append(ch)
      }
    }
  }
  return sb
}


/**
 * If the string is a HTTP URL (ie. Starts with http:// or https://)
 */
fun String.isHttp(): Boolean {
  return this.matches(Regex("(http|https)://[^\\s]*"))
}

