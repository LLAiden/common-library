package com.common.utils

import android.os.Build
import android.text.Html
import android.util.Base64
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

object EncodeUtil {

    fun urlEncode(input: String?, charsetName: String? = "UTF-8"): String {
        return if (input == null || input.isEmpty()) "" else try {
            URLEncoder.encode(input, charsetName)
        } catch (e: UnsupportedEncodingException) {
            throw AssertionError(e)
        }
    }

    fun urlDecode(input: String?, charsetName: String? = "UTF-8"): String {
        return if (input == null || input.isEmpty()) "" else try {
            val safeInput = input.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25").replace("\\+".toRegex(), "%2B")
            URLDecoder.decode(safeInput, charsetName)
        } catch (e: UnsupportedEncodingException) {
            throw AssertionError(e)
        }
    }

    fun base64Encode(input: String): ByteArray {
        return base64Encode(input.toByteArray())
    }

    fun base64Encode(input: ByteArray?): ByteArray {
        return if (input == null || input.isEmpty()) ByteArray(0) else Base64.encode(
            input, Base64.NO_WRAP)
    }

    fun base64Encode2String(input: ByteArray?): String {
        return if (input == null || input.isEmpty()) "" else Base64.encodeToString(
            input, Base64.NO_WRAP)
    }

    fun base64Decode(input: String?): ByteArray {
        return if (input == null || input.isEmpty()) ByteArray(0) else Base64.decode(
            input, Base64.NO_WRAP)
    }

    fun base64Decode(input: ByteArray?): ByteArray {
        return if (input == null || input.isEmpty()) ByteArray(0) else Base64.decode(
            input, Base64.NO_WRAP)
    }

    fun htmlEncode(input: CharSequence?): String {
        if (input == null || input.isEmpty()) return ""
        val sb = StringBuilder()
        var c: Char
        var i = 0
        val len = input.length
        while (i < len) {
            c = input[i]
            when (c) {
                '<'  -> sb.append("&lt;") //$NON-NLS-1$
                '>'  -> sb.append("&gt;") //$NON-NLS-1$
                '&'  -> sb.append("&amp;") //$NON-NLS-1$
                '\'' ->                     //http://www.w3.org/TR/xhtml1
                    // The named character reference &apos; (the apostrophe, U+0027) was
                    // introduced in XML 1.0 but does not appear in HTML. Authors should
                    // therefore use &#39; instead of &apos; to work as expected in HTML 4
                    // user agents.
                    sb.append("&#39;") //$NON-NLS-1$
                '"'  -> sb.append("&quot;") //$NON-NLS-1$
                else -> sb.append(c)
            }
            i++
        }
        return sb.toString()
    }

    fun htmlDecode(input: String?): CharSequence {
        if (input == null || input.isEmpty()) return ""
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(input)
        }
    }

    fun binaryEncode(input: String?): String {
        if (input == null || input.isEmpty()) return ""
        val sb = StringBuilder()
        for (i in input.toCharArray()) {
            sb.append(Integer.toBinaryString(i.toInt())).append(" ")
        }
        return sb.deleteCharAt(sb.length - 1).toString()
    }

    fun binaryDecode(input: String?): String {
        if (input == null || input.isEmpty()) return ""
        val splits = input.split(" ").toTypedArray()
        val sb = StringBuilder()
        for (split in splits) {
            sb.append(split.toInt(2).toChar())
        }
        return sb.toString()
    }
}