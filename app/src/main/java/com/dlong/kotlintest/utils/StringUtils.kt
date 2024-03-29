package com.dlong.kotlintest.utils

/**
 * 字符串工具
 *
 * @author D10NG
 * @date on 2019-09-24 10:32
 */
object StringUtils {

    /**
     * 由于Java是基于Unicode编码的，因此，一个汉字的长度为1，而不是2。
     * 但有时需要以字节单位获得字符串的长度。例如，“123abc长城”按字节长度计算是10，而按Unicode计算长度是8。
     * 为了获得10，需要从头扫描根据字符的Ascii来获得具体的长度。如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255。
     * 因此，可以编写如下的方法来获得以字节为单位的字符串长度。
     */
    fun getWordCount(s: String): Int {
        var length = 0
        for (i in s.indices) {
            val ascii = Character.codePointAt(s, i)
            if (ascii in 0..255)
                length++
            else
                length += 2

        }
        return length
    }

    /**
     * 补全length位，不够的在前面加0
     * @param str
     * @return
     */
    fun upToNString(str: String, length: Int): String {
        var result = StringBuilder()
        if (str.length < length) {
            for (i in 0 until length - str.length) {
                result.append("0")
            }
            result.append(str)
        } else {
            result = StringBuilder(str)
        }
        return result.toString()
    }

    /**
     * 补全length位，不够的在后面加0
     * @param str
     * @return
     */
    fun upToNStringInBack(str: String, length: Int): String {
        var result = StringBuilder()
        if (str.length < length) {
            result.append(str)
            for (i in 0 until length - str.length) {
                result.append("0")
            }
        } else {
            result = StringBuilder(str)
        }
        return result.toString()
    }
}
