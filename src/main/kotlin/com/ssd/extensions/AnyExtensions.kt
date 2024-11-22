package com.ssd.extensions

import java.util.Objects
import kotlin.reflect.KProperty1

/**
 * Extension functions for Any.
 *
 * Code copied from https://github.com/consoleau/kassava/tree/master
 */

/**
 * Checks the other object for equality with this one, based on the supplied properties.
 *
 * @param other the other object to test for equality with this object
 * @param properties the list of properties to use when calculating equality
 * @param T the type of the receiving class
 */
inline fun <reified T : Any> T.kotlinEquals(
    other: Any?,
    properties: Array<out KProperty1<T, Any?>>
): Boolean {
    return when {
        other === this -> true
        other !is T -> false
        else -> properties.all {
            val property = it.get(this)
            val otherProperty = it.get(other)
            if (property is Array<*>) {
                Objects.deepEquals(property, otherProperty)
            } else {
                Objects.equals(property, otherProperty)
            }
        }
    }
}

/**
 * Generates the hash of an object, based on the supplied properties.
 *
 * @param properties the list of properties to include
 * @param superHashCode lambda for calling super.hashCode() if required
 * @param T the type of the receiving class
 */
inline fun <reified T : Any> T.kotlinHashCode(
    properties: Array<out KProperty1<T, Any?>>,
    noinline superHashCode: (() -> Int)? = null
): Int {
    val values = Array(properties.size) { i ->
        val property = properties[i].get(this)
        if (property is Array<*>) {
            property.contentDeepHashCode()
        } else {
            property
        }
    }

    return if (superHashCode != null) {
        Objects.hash(*values, superHashCode())
    } else {
        Objects.hash(*values)
    }
}

/**
 * Generates the String representation of an object, based on the supplied properties.
 *
 * The implementation is based on the implementation provided by Guava's ToStringHelper (https://github.com/google/guava/wiki/CommonObjectUtilitiesExplained).
 *
 * @param properties the list of properties to include
 * @param omitNulls whether to exclude null values
 * @param superToString lambda for calling super.toString() if required
 * @param T the type of the receiving class
 */
inline fun <reified T : Any> T.kotlinToString(
    properties: Array<out KProperty1<T, Any?>>,
    omitNulls: Boolean = false,
    noinline superToString: (() -> String)? = null
): String {

    val builder = StringBuilder(32).append(T::class.java.simpleName).append("(")
    var nextSeparator = ""

    properties.forEach {
        val property = it.name
        val value = it.get(this)
        if (!omitNulls || value != null) {
            with(builder) {
                append(nextSeparator)
                nextSeparator = ", "
                append(property)
                append("=")
                if (value is Array<*>) {
                    val arrayString = arrayOf(value).contentDeepToString()
                    append(arrayString, 1, arrayString.length - 1)
                } else {
                    append(value)
                }
            }
        }
    }

    if (superToString != null) {
        with(builder) {
            append(nextSeparator)
            append("super=")
            append(superToString())
        }
    }

    return builder.append(")").toString()
}