import Direction.*
import Person.Companion.suggestedNames
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * Code available at https://github.com/Sirrah/kotlin-intro
 */
fun main() {
    println("No arguments needed!")

    // fun main(args: Array<String>) { }

    forLoops()

    ifExpressions()

    whenExpressions()

    lambdas()

    nullSafety()

    classDefinitions()

    properties()

    operatorOverloading()

    extensionMethods()

    staticProperties()
}

/**
 * For Loops
 */
fun forLoops() {
    println("For Loops and Ranges")
    println("Works with Collections:")
    val collection = listOf(1, 5, 8)
    for (item in collection)
        print(item)

    println("\nRanges are also Collections")
    for (i in 1..4)
        print(i) // prints "1234"

    println("\nRanges can be created in reverse and/or skip numbers:")
    for (i in 4 downTo 1 step 2)
        print(i) // prints "42"
}

/**
 * If Expressions
 */
fun ifExpressions() {
    // No ternary operator but `if` is an expression (returns a value):
    val a = 1
    val b = 2
    val max = if (a > b) a else b
}

/**
 * When Expressions / Switch statements
 */
fun whenExpressions(x: Int = 4) {
    val mySpecialNumbers = setOf(1, 5, 8)

    // Instead of `switch` there is `when`:
    val myString = when (x) {
        0, 1 -> "x == 0 or x == 1"
        in 1..10 -> "x is in the range"
        in mySpecialNumbers -> { // Braces are optional
            val message = "x is special"
            print(message)
            message // The last expression is the returned result
        }

        !in 10..20 -> "x is outside the range"

        // Not allowed:
        // !3 -> "not 3"
        // >1 -> "more than 1"

        else -> "none of the above"
    }
    println("\nWhen x=$x => $myString")

    // No initial argument required, just add any boolean expression:
    when {
        x == 5 -> println("x is five")
        x != 3 -> println("x is not three")
    }

    // When expressions must be exhaustive, when-statements don't need to except
    // when used on an enum.
    // This is  OK:
    when (x) {
        1 -> println("x is one")
        4 -> println("x is four")
    }

    // but this won't compile if any branch, or else is missing:
    val direction = NORTH;
    when (direction) {
        NORTH -> println("North")
        SOUTH -> println("South")
        WEST -> println("West")
        EAST -> println("East")
    }

    // also NOT ok and will not compile:
//    val myString2 = when (x) {
//        1 -> "x is one"
//        4 -> "x is four"
//    }
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

fun lambdas() {
    val numbers = listOf(0, 1, 2, 3, 4)

    println("\nFunctional operators")
    numbers.filter { number -> number > 1 }
            .map { it * 2 }
            .map {
                print(it)
                it
            }
            .take(1) // prints '468', the operators are not lazy by default.

    println("\nUse lazy evaluation through the Sequence interface")
    numbers.asSequence()
            .filter { number -> number > 1 }
            .map { it * 2 }
            .map {
                print(it)
                it
            }
            .take(1)
            .toList() // prints only '4'

    println("\nCreate convenient helper methods")

    @OptIn(ExperimentalTime::class)
    fun <T> stopwatch(block: () -> T) {
        val mark = TimeSource.Monotonic.markNow()

        val result = block()

        val elapsed = mark.elapsedNow()

        println("Execution took $elapsed, result: '$result'")
    }

    tailrec fun factorial(n: Long = 10, accum: Long = 1): Long = when {
        n <= 1 -> accum
        else -> factorial(n - 1, n * accum)
    }

    // Wrap whatever we want to measure inside a lambda
    stopwatch({ factorial(10) })

    // if the last argument is a lambda, we can omit the braces
    stopwatch {
        factorial(10)
    }

    // Or use a method reference (same syntax as Java8) if there are no method arguments
    stopwatch(::factorial)

    // The standard library has many of these helper methods
    val immutableListOfNames = buildList {
        add("John")
        add("Doe")
        addAll(suggestedNames)
    }
    //immutableListOfNames.add("Can't touch this")

    // Lambdas combined with static extension methods form the building
    // blocks for relatively simple helper methods (.apply, .buildList)
    // all the way up to frameworks like Jetpack Compose.
}

/**
 * Null Safety
 *
 * Uncomment the method body, it contains compiler errors to illustrate null safety.
 */
fun nullSafety() {
    println("\nNull-safety")

//    // Non-null variable definition (default):
//    var a: String = "abc"
//    a = null // compilation error

    // Nullable variable definition:
    var b: String? = "abc"
    b = null // ok

//    val length = b.length // error: variable 'b' can be null

    // Besides using standard null - guard statements you have a few more options :

    // Safe call operator:
    b?.toInt()?.plus(5)?.toString()

    // Elvis operator:
    val lengthOrDefaultValue = b?.length ?: -1

    // !!Operator to force non-null:
//    val lengthOrThrow = b!!.length

    // Kotlin.Any is Kotlin's equivalent to 'Object'
    val someUnknownType: Any = "Mystery Box"

    // Safe casts:
    val c: String? = someUnknownType as? String

    // Can use the safe call operator as an alternative to null-guard statements
    c?.let {
        println("Found string: $it")
    }

    // Smart casts, the compiler will let you use a nullable type as a
    // non-null type whenever that's safe.
    val d: String? = "123"
    if (d != null)
        println(d.toInt())

    // With 'contracts' we can even provide the compiler with hints so this can work
    // with helper methods as well.
    // [Contracts](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.contracts/index.html)
    if (!d.isNullOrEmpty())
        println(d.toInt())
}

/**
 * Class Definitions
 */
fun classDefinitions() {
    // Basic Java class with getters and setters
    /*
    public class Person {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name) {
            this.name = name;
        }

        public Person(int age) {
            this.age = age;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
    */

    // Kotlin equivalent:
    class Person(var name: String, var age: Int)

    val jack = Person("Jack", 1)

    // With default values, named arguments and immutable members:
    class PersonV2(val name: String = "unknown", val age: Int = 0)

    val jackV2 = PersonV2("Jack")
    val johnDoeV2 = PersonV2(age = 21)

    // Even better with data classes which automatically creates `#equals`, `#hashCode`, `#toString` and `#toCopy`:
    data class PersonV3(val name: String = "", val age: Int = 0)

    val jackV3 = PersonV3(name = "Jack", age = 1)
    val olderJackV3 = jackV3.copy(age = 2)
    println(jackV3.toString()) // User(name=Jack, age=1)

    // Bonus [destructuring](https://kotlinlang.org/docs/reference/multi-declarations.html) through automatically
    // generated component functions:
    val (name, age) = jackV3
}

fun properties() {
    class Person {
        // Public members are accessed as properties, this is done automatically when interacting with
        // Java and there are getters and setters.
        var age: Int = 0

        var name: String = ""
            set(value) {
                if (value == "Bad Name")
                    throw IllegalArgumentException()
                field = value
            }

        private var numbers: Int = 1234
        private var letters: String = "AB"

        var postCode: String
            get() = "$numbers $letters"
            set(value) {
                // Triple quotes so we don't have to use double escapes
                val matches = Regex("""(\d{4})\s*([a-zA-Z]{2})""")
                        .find(value) ?: throw IllegalArgumentException()

                numbers = matches.groupValues[1].toInt()
                letters = matches.groupValues[2].uppercase()
            }
    }

    val user = Person()
    user.name = "Piet"
    user.postCode = "7141cd"

    println("""
        (Multi-line strings are supported as well)
        ${user.name} lives at:
        ${user.postCode}
    """.trimIndent()
    )

    // Or using #with: (also look at #let, #apply, #repeat, ...)
    with(user) {
        name = "Jan"
        postCode = "7142ed"

        println("$name lives at $postCode")
    }
}

/**
 * Extension Methods
 */
fun extensionMethods() {
    class Person(val name: String, val age: Int)

    val jack = Person("Jack", 1)

    // Easily add helper methods to existing classes:
    fun Person.sayHello() = println("Hello, my name is $name and I'm $age years old")

    jack.sayHello()

    // Properties can be added as well
    println("${3600.seconds} is the same as ${1.hours}")
}

/**
 * Operator Overloading
 */
fun operatorOverloading() {
    data class Person(val name: String = "", val age: Int = 0) {
        fun celebrateBirthday() = copy(name = name, age = age + 1)

        /**
         * Create a new Person by addition
         */
        operator fun plus(partner: Person) = Person(name = "$name ${partner.name} Junior", age = 0)
    }

    var junior = Person("Jack") + Person("Jill")

    // Or using extension methods:
    operator fun Person.inc(): Person = celebrateBirthday()
    junior++

    println("${junior.name} just turned ${junior.age}!")

    // All operators are available, e.g. +, *, ++, +=, [], ()
    // https://kotlinlang.org/docs/reference/operator-overloading.html
}

class Person(val name: String) {
    // Static properties are all placed in a companion object
    companion object {
        val suggestedNames = listOf("Jan", "Piet")
    }
}

object PersonAsSingleton {
    const val name = "Christopher"
}

fun staticProperties() {
    println("Some name suggestions: ${Person.suggestedNames}")

    println("There can be only one: ${PersonAsSingleton.name}")
}

/**
 * There is not much to this [PersonKDoc] but I'd *like* to show some [KDoc](https://kotlinlang.org/docs/kotlin-doc.html) features.
 *
 * It's a mixture of
 * 1. [Javadoc](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html) and
 * 2. [Markdown](https://www.markdownguide.org/)
 *
 * @param name how to refer to this [PersonKDoc] in a conversation
 * @property suggestedNames some name suggestions as a [List] of [Strings][String]
 * @constructor spawn a new [PersonKDoc]
 *
 * @throws NoSuchElementException if [suggestedNames] is empty
 *
 * @sample examplesForPerson
 *
 * @see staticProperties
 * @see properties
 */
data class PersonKDoc(val name: String = suggestedNames.first()) {
    companion object {
        val suggestedNames = listOf("Jan", "Piet")
    }
}

// This shows as formatted code snippet similar to @snippet in Java 18
fun examplesForPerson() {
    val jan = PersonKDoc()
    val john = PersonKDoc("John")
    val copy = jan.copy(name = john.name)
}

// Also look into coroutines:
// https://kotlinlang.org/docs/reference/coroutines-overview.html
//
// and Kotlin/Native:
// https://kotlinlang.org/docs/reference/native-overview.html
