import Direction.*

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
    val mySpecialNumbers = listOf(1, 5, 8)

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
    numbers
        .filter { number -> number > 1 }
        .map { it * 2 }
        .map {
            print(it)
            it
        }
        .take(1) // This will still print '468', the operators are not lazy by default.

    println("\nUse lazy evaluation through the Sequence interface")
    numbers
        .asSequence()
        .filter { number -> number > 1 }
        .map { it * 2 }
        .map {
            print(it)
            it
        }
        .take(1)
        .toList() // will print only '4'

    println("\nCreate convenient helper methods")

    fun stopwatch(block: () -> Unit) {
        val t1 = System.currentTimeMillis()

        block()

        val t2 = System.currentTimeMillis()

        println("Execution took ${t2 - t1} ms")
    }

    fun factorial(n: Long, accum: Long = 1): Long {
        return if (n <= 1) {
            n * accum
        } else {
            factorial(n - 1, n * accum)
        }
    }

    // Use a lambda, if the last argument is a lambda, you can omit the braces
    stopwatch {
        factorial(1000)
    }

    // Or use a method reference (same syntax as Java8) if there are no method arguments
    // stopwatch(::factorial)

    println("Tail recursion benchmark")

    // In order to apply the compiler optimization the method needs
    // to be in the right form, and it needs the "tailrec" keyword.
    //
    // With these pre-conditions the compiler will be able to compile
    // this to a while-loop giving a speed boost and preventing a
    // stack overflow.
    tailrec fun factorialTailRec(n: Long, accum: Long = 1): Long {
        return if (n <= 1) {
            n * accum
        } else {
            factorialTailRec(n - 1, n * accum)
        }
    }

    val n = 50_000L
    stopwatch {
        try {
            factorial(n)
        } catch (e: StackOverflowError) {
            println(e)
        }
    }

    stopwatch {
        factorialTailRec(n)
    }
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

    // Safe casts:
    val someUnknownType: Any = "Mystery Box" // Kotlin.Any is Kotlin's equivalent to 'Object'
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
                val matches = Regex("""(\d{4})\s*([a-zA-Z]{2})""").find(value) ?: throw IllegalArgumentException()

                numbers = matches.groupValues[1].toInt()
                letters = matches.groupValues[2].toUpperCase()
            }
    }

    val user = Person()
    user.name = "Piet"
    user.postCode = "7141dc"

    println(
        """
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
    class Person(var name: String, var age: Int)

    val jack = Person("Jack", 1)

    // Easily add helper methods to existing classes:
    fun Person.sayHello() = println("Hello, my name is $name")
    jack.sayHello()

    // Easily display a Toast message in Android (example from Anko)
//    fun Context.toast(message: Int) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    toast("hello") // anywhere in a subclass of Context (Activity, Fragment, etc)
}

/**
 * Operator Overloading
 */
fun operatorOverloading() {
    data class Person(val name: String = "", val age: Int = 0) {
        fun celebrateBirthday(): Person = copy(name, age + 1)

        /**
         * Create a new Person by addition
         */
        operator fun plus(partner: Person): Person = Person(name = "$name ${partner.name} Junior", age = 0)
    }

    var junior = Person("Jack") + Person("Jill")

    // Or using extension methods:
    operator fun Person.inc(): Person = celebrateBirthday()
    junior++

    println("${junior.name} just turned ${junior.age}!")

    // All operators are available, e.g. +, *, ++, +=, [], ()
    // https://kotlinlang.org/docs/reference/operator-overloading.html
}

object PersonAsSingleton {
    const val name = "Christopher"
}

class PersonWithStaticProperties(val name: String) {
    // Static properties are all placed in a companion object
    companion object {
        val acceptableNames = listOf("Jan", "Piet")
    }
}

fun staticProperties() {
    PersonWithStaticProperties.acceptableNames

    println("There can be only one: ${PersonAsSingleton.name}")
}

// Also look into coroutines:
// https://kotlinlang.org/docs/reference/coroutines-overview.html
//
// and Kotlin/Native:
// https://kotlinlang.org/docs/reference/native-overview.html
