# KotlinExceptions
An example of a bug in how Kotlin handles uncaught exceptions in Android.

## Reproduction:

This app has two buttons. A button that will correctly log an exception to the logcat, and a button that won't.   

The button that logs the exception has its own `CoroutineExceptionHandler` that simply rethrows any exception that it encounters, allowing the system to pick it up.

The other button does not log the exception. It will instead call the `Thread`'s default uncaught exception handler, which for Android is called the `KillApplicationHandler` which immediately kills the application.  

## Explanation
Android has an odd way of making sure uncaught exceptions are logged. It has a concept of a "Uncaught Exception PreHandler" which is called before the default uncaught exception handler when the system is handling an uncaught exception. This has the side-effect of making it so that calling the uncaught exception handler directly won't invoke the pre-handler. The relevant code for this can be found in `Thread`. Relevant methods in that class are `#getDefaultUncaughtExceptionHandler`, `#getUncaughtExceptionPreHandler` and `#dispatchUncaughtException` The actual handlers themselves are located in a class called `RuntimeInit`, with the pre-handler being `RuntimeInit$LoggingHandler` and the normal handler being `RuntimeInit$KillApplicationHandler`

## Solution:

Admittedly, rethrowing the exception is probably not a good idea. I don't claim to understand the logistics behind that, nor the performance penalties that might be incurred by doing it that way. However, for the short term, it does work. This has an unfortunate side-effect of including some coroutine specific code in the trace, but that isn't an issue in my opinion.

It is also possible to simply log the exception manually in a `CoroutineExceptionHandler`, mimicking what Android's uncaught exception pre-handler does for you.
