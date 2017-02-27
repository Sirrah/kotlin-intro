fun main(args: Array<String>) {
    print("Hello World!")

    forLoops()

    ifExpressions()

    whenExpressions()

    nullSafety()

    classDefinitions()

    operatorOverloading()

    extensionMethods()
}

/**
 * For Loops
 */
fun forLoops() {
    println("For Loops and Ranges")
    println("Works with Collections:")
    val collection = listOf(1, 5, 8)
    for (item in collection) print(item)

    println("\nRanges are also Collections")
    for (i in 1..4) print(i) // prints "1234"

    println("\nRanges can be created in reverse and/or skip numbers:")
    for (i in 4 downTo 1 step 2) print(i) // prints "42"
}

/**
 * If Expressions
 */
fun ifExpressions() {
    // No ternary operator but `if` is an expression (returns a value):
    val a = 1
    val b = 2
    val max = if (a > b) a else b

    class Client(val name: String?)

    val client: Client? = Client(null)

    // There is the 'Elvis' operator:
    val name = client?.name ?: "Unknown"
}

/**
 * When Expressions / Switch statments
 */
fun whenExpressions() {
    val mySpecialNumbers = listOf(1, 5, 8)

    // Instead of `switch` there is `when`:
    val x = 3
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

    // No initial argument required, just add any boolean expression:
    when {
        x == 5 -> print("x is five")
        x != 3 -> print("x is not three")
    }
}

/**
 * Null Safety
 *
 * Uncomment the method body, it contains compiler errors to illustrate null safety.
 */
fun nullSafety() {
//    // Non-null variable definition (default):
//    var a: String = "abc"
//    a = null // compilation error
//
//    // Nullable variable definition:
//    var b: String? = "abc"
//    b = null // ok
//
//    val l = b.length // error: variable 'b' can be null
//
//    // Besides using standard null - guard statements you have a few more options :
//
//    // Safe call operator:
//    train?.wreck?.averted?.maybe
//
//    // Elvis operator:
//    val l = b?.length ?: -1
//
//    // !!Operator:
//    val l = b!!.length
//
//    // Safe casts:
//    val c: String? = maybeNull as? String
}

/**
 * Class Definitions
 */
fun classDefinitions() {
    // Basic Java class with getters and setters
    /*
    public class User {
        private String name;
        private int age;

        public User() {
        }

        public User(String name) {
            this.name = name;
        }

        public User(int age) {
            this.age = age;
        }

        public User(String name, int age) {
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
    class User(var name: String, var age: Int)

    val jack = User("Jack", 1)

    // With default values and named arguments:
    class UserV2(var name: String = "unknown", var age: Int = 0)

    val jackV2 = UserV2(name = "Jack")

    // Even better with data classes which automatically creates `#equals`, `#hashCode`, `#toString` and `#toCopy`:
    data class UserV3(val name: String = "", val age: Int = 0)

    val jackV3 = UserV3(name = "Jack", age = 1)
    val olderJack = jackV3.copy(age = 2)
    print(jackV3.toString()) // User(name=Jack, age=1)

    // Bonus [destructuring](https://kotlinlang.org/docs/reference/multi-declarations.html) through automatically generated component functions:
    val (name, age) = jackV3
}

/**
 * Extension Methods
 */
fun extensionMethods() {
    class User(var name: String, var age: Int)

    val jack = User("Jack", 1)

    // Easily add helper methods to existing classes:
    fun User.sayHello() = print("Hello, my name is ${name}")
    jack.sayHello()

    // Easily display a Toast message in Android (example from Anko)
//    fun Context.toast(message: Int) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    toast("hello") // anywhere in a subclass of Context (Activity, Fragment, etc)
}

/**
 * Operator Overloading
 */
fun operatorOverloading() {
    // Use the mathematical operators as utility methods:
    class User(val name: String = "", val age: Int = 0) {
        operator fun plus(partner: User): User = User(name = "${name} Junior", age = 0)
    }

    val junior = User("Jack") + User("Jill")
}