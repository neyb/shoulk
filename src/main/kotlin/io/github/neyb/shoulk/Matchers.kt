package io.github.neyb.shoulk

import io.github.neyb.shoulk.matcher.*
import kotlin.test.fail

fun <T> equal(expected: T) = match<T>("""be equal to "$expected"""") { it == expected }

fun <T> be(expected: T) = match<T>("""be the same object as "$expected"@${System.identityHashCode(expected)}""")
{ it === expected } but { "its identity hashCode is @${System.identityHashCode(it)}" }

fun <T> contain(expected: T) = match<Iterable<T>>("""contain "$expected"""") { it.contains(expected) }

fun <T> contain(matcher: Matcher<T>) = match<Iterable<T>>("""contain an element matching "${matcher.description}"""")
{ it.any { matcher.match(it).success } }

fun <T> haveValueThat(matcher: Matcher<T>): Matcher<T?> = BasicMatcher("""have value matching "${matcher.description}"""") {
    if (it == null) fail("it has no value")
    else matcher.match(it)
}

fun <T : Throwable> hasMessage(expectedMessage:String): Matcher<T> = hasMessage(equal(expectedMessage))

fun <T : Throwable> hasMessage(matcher: Matcher<String>? = null): Matcher<T> = BasicMatcher(
        if (matcher != null) """have a message matching  "${matcher.description}"""""
        else "have a message" ) {
    val message = it.message
    if (message == null) fail("has no message")
    else matcher?.match(message) ?: MatchResult.ok
}

fun <T> matchInOrder(matchers: List<Matcher<T>>) = InOrderMatcher(matchers)
fun <T> matchInAnyOrder(matchers: List<Matcher<T>>) = InAnyOrderMatcher(matchers)

private fun <T> Iterable<T>.getFirstDismatchIndex(matchers: List<Matcher<T>>): Int? {
    this.forEachIndexed { index, curr ->
        if (!matchers[index].match(curr).success) return index
    }
    return null
}

fun <T> all(matcher: Matcher<T>): Matcher<Iterable<T>> = AllMatcher(matcher)

fun <T> noneMatch(matcher: Matcher<T>): Matcher<Iterable<T>> = NoneMatcher(matcher)

private fun haveSize(size: Int) = match<Iterable<*>>("have $size element(s)") { it.size() == size } but { "has ${it.size()} element(s)" }

private fun Iterable<*>.size(): Int {
    var size = 0
    this.forEach { ++size }
    return size
}

private operator fun <T> Iterable<T>.get(index: Int): T {
    if (index >= 0) this.forEachIndexed { currIndex, curr -> if (currIndex == index) return curr }
    throw IndexOutOfBoundsException("$this has no item at $index")
}