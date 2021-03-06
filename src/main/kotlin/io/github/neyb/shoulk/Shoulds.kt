package io.github.neyb.shoulk

import io.github.neyb.shoulk.matcher.Matcher
import kotlin.reflect.KClass
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

infix fun <T> T.shouldEqual(expected: T) = should(equal(expected))
infix fun <T> T.shouldNotEqual(expected: T) = should(!equal(expected))

infix fun <T> T.shouldBe(expected: T) = should(be(expected))
infix fun <T> T.shouldNotBe(expected: T) = should(!be(expected))

infix fun <T> T?.shoudHaveValueThat(matcher: Matcher<T>) = should(haveValueThat(matcher))

infix fun <T> Iterable<T>.shouldContain(expected: T) = should(contain(expected))
infix fun <T> Iterable<T>.shouldContain(matcher: Matcher<T>) = should(contain(matcher))

infix fun <T> Iterable<T>.shouldNotContain(expected: T) = should(!contain(expected))

infix fun <T> Iterable<T>.shouldMatchInOrder(matchers: List<Matcher<T>>) = should(matchInOrder(matchers))

infix fun <T> Iterable<T>.shouldMatchInAnyOrder(matchers: List<Matcher<T>>) = should(matchInAnyOrder(matchers))



//infix fun <T> Iterable<T>.anyShouldMatch(matcher: matcher<T>) = should(anyMatch(matcher))
//infix fun <T> Iterable<T>.noneShouldMatch(matcher: matcher<T>) = should(anyMatch(matcher))
infix fun <T> Iterable<T>.shouldAll(matcher: Matcher<T>) = should(all(matcher))

@Suppress("UNUSED_PARAMETER") // for infix code
infix inline fun <reified E : Throwable> (() -> Any).shouldThrow(expectedType: KClass<E>) =
        assertFailsWith<E> { this() }
infix fun <E : Throwable> E.that(matcher: Matcher<E>) = should(matcher)

infix fun (()->Any).shouldFailWithAMessageThat(messageMatcher: Matcher<String>) =
        this.shouldThrow(Throwable::class).message.should(haveValueThat(messageMatcher))
infix fun (()->Any).shouldFailWithMessage(expectedMessage:String) = this.shouldFailWithAMessageThat(equal(expectedMessage))

infix fun <T> T.should(matcher: Matcher<T>) = matcher.match(this).check()

