# Kotlin Introduction
This project illustrates some of the basics of Kotlin in order to use it as a 'better Java`.
It's accompanied by a live demo where Android Java code is translated to Kotlin.

## Language Features
The official [documentation](https://kotlinlang.org/docs/reference/) is very easy to read.
Much of it will be familiar from Java 8, Groovy (Gradle), Ruby or C#.
Further on we will convert Java code to Kotlin to introduce much more of the language.

### For Loops and Ranges
Works with Collections:
```
for (item in collection) print(item)
```
Ranges are also Collections:
```
for (i in 1..4) print(i) // prints "1234"
```
Ranges can be created in reverse and/or skip numbers:
```
for (i in 4 downTo 1 step 2) print(i) // prints "42"
```

### If Expression
No ternary operator but `if` is an expression (returns a value):
```
val max = if (a > b) a else b
```
There is the 'Elvis' operator:
```
val name = client?.getName() ?: "Unknown"
```

### When Expression
Instead of `switch` there is `when`:
```
val myString = when {
    0, 1 -> "x == 0 or x == 1"
    in 1..10 -> "x is in the range"
    in mySpecialNumbers -> {
        info("
        "x is special"
    }
    !in 10..20 -> "x is outside the range"
    else -> "none of the above"
}
```

No initial argument required, just add any boolean expression:
```
when {
    x.isOdd() -> print("x is odd")
    x.isEven() && y.isOdd() -> print("x is even but y is odd")
}
```

### Null Safety
Non-null variable definition (default):
```
var a: String = "abc"
a = null // compilation error
```
Nullable variable definition:
```
var b: String? = "abc"
b = null // ok

val l = b.length // error: variable 'b' can be null
```

Besides using standard null-guard statements you have a few more options:

Safe call operator:
```
train?.wreck?.averted?.maybe
```

Elvis operator:
```
val l = b?.length ?: -1
```

!! Operator:
```
val l = b!!.length
```

Safe casts:
```
val c: String? = maybeNull as? String
```

### Class definitions
Java:
```
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
```

Kotlin equivalent:
```
class User(var name: String, var age: Int)

val jack = User( "Jack", 1)
```

With default values and named arguments:
```
class User(var name: String = "unknown", var age: Int = 0)

val jack = User(name = "Jack")
```

Even better with data classes which automatically creates `#equals`, `#hashCode`, `#toString` and `#toCopy`:
```
data class User(val name: String = "", val age: Int = 0)

val jack = User(name = "Jack", age = 1)
val olderJack = jack.copy(age = 2)
jack.toString() // User(name=Jack, age=1)
```
Bonus [destructuring](https://kotlinlang.org/docs/reference/multi-declarations.html) through automatically generated component functions:
```
val (name, age) = jack
```

### Extension Methods

Easily add helper methods to existing classes:
```
fun User.sayHello() = print("Hello, my name is ${name}")
jack.sayHello()

fun Context.toast(message: Int) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
toast("hello") // anywhere in a subclass of Context (Activity, Fragment, etc)
```

### Operator Overloading
Use the mathematical operators as utility methods:
```
class User(val name: String = "", val age: Int = 0) {
	operator fun plus(partner: User): User = User(name = "${name} Junior", age = 0)
}

val junior = User("Jack") + User("Jackie")
```

## Automatic Kotlin Conversion

Via action menu:
Linux: `ctrl-shift-a`
Mac OS: `Command-Shift-a`

Direct key binding:
Linux: `ctrl-alt-shift-k`
Mac OS: `Option-Shift-Command-K`

Steps for conversion:
1. Apply automatic conversion
2. Move to correct location:
    from `/src/main/java/com/nedap/healthcare/.../*.java`
    to `/src/main/kotlin/com/nedap/healthcare/.../*.kt`
3. Commit to Git
4. Manual cleanup
5. Commit to Git
6. Profit

## Kotlin Android extensions
[Android plugin](https://kotlinlang.org/docs/tutorials/android-plugin.html)
Easily access Views through synthetic extensions:
```
import kotlinx.android.synthetic.main.<name of layout file>.*
```
For Fragments these views are accessible in `onViewCreated` or later in the lifecycle , i.e. they won't work in `onCreate`.

## Anko

[Anko](https://github.com/Kotlin/anko)

### Anko DSL
Might be nice to use in some cases and it's a good way to get a feel for what you can do with extension methods.

Normal Android code:
```
val act = this
val layout = LinearLayout(act)
layout.orientation = LinearLayout.VERTICAL
val name = EditText(act)
val button = Button(act)
button.text = "Say Hello"
button.setOnClickListener {
    Toast.makeText(act, "Hello, ${name.text}!", Toast.LENGTH_SHORT).show()
}
layout.addView(name)
layout.addView(button)
```
Anko DSL:
```
verticalLayout {
    val name = editText()
    button("Say Hello") {
        onClick { toast("Hello, ${name.text}!") }
    }
}
```

### Anko extension methods
Able to use the nice extension methods separate from the fancy DSL:
`compile "org.jetbrains.anko:anko-common:$anko_version"`
