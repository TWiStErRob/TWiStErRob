Abstract
--------
There's a library which wraps two other libraries. My app is using this library and one of the other referenced ones. Calling a specific method (kind of important one) forces me do depend on the other wrapped library too even though I'm not using anything from it and the library is not calling any code from that either (no `ClassNotFoundException` or `NoClassDefFoundError` or `NoSuchMethodError`).


Tell-tale
---------
Suppose I'm building a brand new earth-shattering app, let's call this `app`.
I looked online and I found a library which helps reaching my ~~evil~~ world-changing goal, let's call this `lib1`.

While I was working on the integration of `lib1` I found that it's too low level: I would have to write a lot of code to achieve a simple task. However it is very well established (well written and tested) and I'd like to utilize it.

Looking more on-the-line I found another library which wraps this library and provides a nice façade for me to use, let's call this `library`.

While writing my code I find myself in a weird situation:
I have an instance of a class (`o`) from `lib1` and I'd like to pass it to one of `library`'s methods (`which`): `h.which(o)`.
There's nothing wrong with the above, right?
 * I have `lib1` as a dependency, so far it was working
 * I have `library` as a dependency, so far it was working
 * I call "just another method", and now:
```
$ gradle app:build
:app:compileJava
.../app/src/main/java/my/app/Main.java:10: error: cannot access Obj2
                String s = h.which(o);
                            ^
  class file for org.lib2.Obj2 not found
1 error
:app:compileJava FAILED
```
Hmm, what is `Obj2` and what is `lib2`? I definitely didn't hear about those.

Well it turns out that `library` is compiled against that misterious `lib2` (competitor of `lib1`) too and provides the same façade for it.


Minimal repro
-------------
To simulate the following steps are done:
 * Create a "central repository": a folder called `repo`.
 * Build `lib1` and `lib2` and publish it in a "central repository".
 * Build `library` and publish it in a "central repository".
 * Build my app and reference the required libs from the "central repository"

_See [build.gradle](build.gradle) for more._

```
gradle app:build
```


Reality
-------
Now let's substitute:
 * `lib1` for `android.app`
 * `lib2` for `android.support.v4.app`
 * `library` for Glide
 * `Obj1`/`Obj2` for `Fragment`
 * `h.which` for `Glide.with`

It means that even if I'm not using the support library in my app, if I want to call `Glide.with` (the main entry point of Glide), I have to compile against the support library. See [Glide Issue #137](https://github.com/bumptech/glide/issues/137) for more details and followup comments.


Questions
---------
Do I really have to depend on v4 to use Glide?
How could Glide not break its interface, but don't force me to depend v4 at the same time?
Why is this happening in the first place? Java by-design? Is it fixed in Java 7/8? Is there a compiler switch?


Tried Solutions
---------------
 1. Adding dependency for v4 (`lib2`)<br/>
_This is unwelcome because it clutters the namespace, slows down build, make package bigger, etc..._
 2. Change `String which(Obj2 o)` to `<T extends Obj2> String which(T o)` hoping that erasure will relax the dependency.<br/>
_Same error_
 3. Call via reflection: `(String)h.getClass().getMethod("which", Obj1.class).invoke(h, new Object[] {o});`<br/>
_works, but seriously?!_
 4. Add another method for `lib2`: `String whichLib2(Obj2 o)` and remove `which(Obj2)` overload<br/>
_This is a breaking change since all users of the support library (`lib2`) would need to change their call to the new method. However it would make code cleaner communicate that they're using v4._
 5. Add another method which doesn't overload: `String whichLib1(Obj1 o)`<br/>
_This might work._
 6. _... tell me if you have one_
