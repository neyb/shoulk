package io.github.neyb.shoulk

infix fun <T> ((T) -> Boolean).describedAs(description: String) = FluentMatcher(description, matcher = this)

class FluentMatcher<T>(
        private val description: String = "match a not described criteria",
        private val dismatchDescriptionBuilder: ((T) -> String)? = null,
        private val positive: Boolean = true,
        private val matcher: (T) -> Boolean
) : Matcher<T> {

    override fun match(actual: T) = matcher(actual)

    override fun getDismatchDescriptionFor(actual: T) =
            if (dismatchDescriptionBuilder == null) expectedDescription(actual)
            else "${expectedDescription(actual)} but ${dismatchDescription(actual, dismatchDescriptionBuilder)}"

    infix fun but(dismatchDescriptionBuilder: ((T) -> String)) = FluentMatcher(description, dismatchDescriptionBuilder, positive, matcher)

    operator fun not() = FluentMatcher(description, dismatchDescriptionBuilder, !positive, { actual -> !matcher.invoke(actual) })

    private fun expectedDescription(actual: T) = """"$actual" ${if(positive) "should" else "should not"} $description"""

    private fun dismatchDescription(actual: T, dismatchDescriptionBuilder: (T) -> String) = dismatchDescriptionBuilder.invoke(actual)
}