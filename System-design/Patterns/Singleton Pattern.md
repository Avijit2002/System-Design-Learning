
1) **What is this? why do we need this?**

	Suppose an object is being used by multiple services, normally to use them each service will create its own object. We need a single object to be used across all services.

	The singleton pattern ensures that the class has only one instance throughout the application lifecycle and provides a global access point to that instance.

	Also Resource constraint 

	We can create a global variable but it can be modified by anyone easily. 

2) **Example -**
	Database, Logging, analytics

3) **Eager Loading and Lazy loading**
	
	Suppose we need to count how many times run and submit is done by users. Normally we create a object of judgeAnalytics that holds run and submit count variables and its associated methods. But if each service create new object each time, count will be local to that object, we need global run and submit count by all the services, so to fix this issue we need a global object

	The Singleton Pattern typically involves the following steps:

	- **Private constructor:** Prevents instantiation from outside the class.
	- **Static variable:** Holds the single instance of the class.
	- **Public static method:** Provides a global access point to get the instance.

	 **Eager Loading** -  In **Eager Loading**, the Singleton instance is created as soon as the class is loaded, regardless of whether it's ever used. Let's understand this with a real-life analogy.

```java
package Singleton_pattern;  
  
class JudgeAnalytics{  
  
    private static final JudgeAnalytics judge = new JudgeAnalytics();  
    private JudgeAnalytics(){}  
  
    public static JudgeAnalytics getInstance(){  
        return judge;  
    }  
  
}  
  
public class Main {  
    public static void main(String[] args){  
        JudgeAnalytics judge = JudgeAnalytics.getInstance();  
        System.out.println(judge);  
    }  
  
}
```

 ### **Understanding**
- The object is created **immediately** when the class is loaded.
- It's always available and inherently **thread-safe**.
#### Pros
- Very simple to implement.
- Thread-safe without any extra handling.
#### Cons
- Wastes memory if the instance is **never used**.
- Not suitable for **heavy** objects.

## Lazy Loading (On-Demand Initialization)

In **Lazy Loading**, the Singleton instance is created only **when it's needed** - the first time the `getInstance()` method is called.

###### Real-World Analogy: Coffee Machine

Imagine a coffee machine that only brews coffee when you press the button. It doesn't waste energy or resources until you actually want a cup. Similarly, lazy loading creates the Singleton instance only when it's requested.

#### Example Code:

```java
class JudgeAnalyticsLazyLoading{  
    private static JudgeAnalyticsLazyLoading judge;  
    private JudgeAnalyticsLazyLoading(){}  
  
    public static JudgeAnalyticsLazyLoading getInstance(){  
        if(judge == null){  
            judge = new JudgeAnalyticsLazyLoading();  
        }  
        return judge;  
    }  
  
}
```

#### Understanding

- The **instance** starts as `null`.
- It is only created when `getInstance()` is first called.
- Future calls return the already created instance.

#### Pros

- Saves memory if the instance is never used.
- Object creation is **deferred until required**.

#### Cons

Lazy Loading is **Not thread-safe** by default. Thus, it requires synchronization in multi-threaded environments.

## Thread Safety: A Critical Concern in Singleton Pattern

In a **single-threaded environment**, implementing a Singleton is straightforward. However, things get complicated in **multi-threaded applications**, which are very common in modern software (especially web servers, mobile apps, etc.).

#### The Problem

Let's say two threads **simultaneously** call `getInstance()` for the first time in a **lazy-loaded Singleton**. If the instance hasn't been created yet, both threads might pass the null check and end up **creating two different instances** - completely breaking the Singleton guarantee.  
  
This kind of bug is:

- **Hard to detect**, as it may not occur every time.
- **Severe**, because it defeats the whole purpose of the pattern.
- **Costly**, especially if the Singleton manages critical resources like logging, configuration, or DB connections.

  

---

## Different Ways to Achieve Thread Safety

There are several ways to make the Singleton pattern thread-safe. Here are a few common approaches:

#### 1. Synchronized Method

This is the simplest way to ensure thread safety. By **synchronizing the method** that creates the instance, we can prevent multiple threads from creating separate instances at the same time. However, this approach can lead to performance issues due to the overhead of synchronization.  
  
Consider the following code snippet for better understanding:  

Java

```java
public class Singleton {
    // Object declaration
    private static Singleton instance;

    // Private constructor
    private Singleton() {}

    // Synchronized keyword used
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

###### What `synchronized` keyword does?

The `synchronized` keyword ensures that **only one thread at a time** can execute the `getInstance()` method. This prevents multiple threads from entering the method simultaneously and creating multiple instances.  

###### Pros

- Simple and easy to implement.
- Thread-safe without needing complex logic.

###### Cons

- **Performance overhead:** Every call to `getInstance()` is synchronized, even after the instance is created.
- May slow down the application in high-concurrency scenarios.

  

---

#### 2. Double-Checked Locking

This is a more efficient way to achieve thread safety. The idea is to check if the instance is **null** before acquiring the lock. If it is, then we synchronize the block and check again. This reduces the overhead of synchronization after the instance has been created.  
  
Consider the following code snippet for better understanding:  

Java

```java
public class Singleton {
    // Volatile object declaration
    private static volatile Singleton instance;

    // Private constructor
    private Singleton() {}

    // Thread-safe method using double-checked locking
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

###### Understanding

- The outer `if` check avoids synchronization **once the instance is created**.
- The inner `if` inside `synchronized` ensures that **only one thread creates the instance**.
- `volatile` keyword ensures changes made by one thread are visible to others. Without `volatile`, one thread might create the Singleton instance, but **other threads may not see the updated value** due to caching. `volatile` ensures that the instance is always read from the **main memory**, so all threads see the most up-to-date version.

###### Pros

- **Efficient:** Synchronization only happens once, when the instance is created.
- Safe and fast in concurrent environments.

###### Cons

- Slightly more complex than the synchronized method.
- Requires Java 1.5 or above due to reliance on `volatile`.

  

---

#### 3. Bill Pugh Singleton (Best Practice for Lazy Loading)

This is a highly efficient way to implement the Singleton pattern. It uses a static inner helper class to hold the Singleton instance. The instance is created only when the inner class is loaded, which happens only when `getInstance()` is called for the first time.  
  
Consider the following code snippet for better understanding:  

Java

```java
public class Singleton {
    // Private constructor
    private Singleton() {}

    // Static inner class to hold the Singleton instance
    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    // Public method to return the Singleton instance
    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

###### Explanation

- The Singleton instance is not created until `getInstance()` is called.
- The **static inner class** `(Holder)` is **not loaded until referenced**, thanks to Java's class loading mechanism.
- It ensures **thread safety**, **lazy loading**, and **high performance** without synchronization overhead.

###### Pros

- Best of both worlds: **Lazy + Thread-safe**.
- No need for synchronized or volatile.
- Clean and efficient.

###### Cons

It is slightly less intuitive for beginners due to the use of a nested static class.  
  

---

#### 4. Eager Loading

As discussed earlier, eager loading does not face thread safety issues. This approach avoids thread issues altogether by creating the instance upfront - **at the cost of potential memory waste**. Thus, it is not a preferred method in most cases but is still a valid option.

### Pros of Singleton Pattern

- **Cleaner Implementation:** Singleton offers a straightforward and tidy way to manage a single instance of a class, especially when designed with thread safety and simplicity in mind.
- **Guarantees One Instance:** This pattern enforces that only one instance of the class can exist, making it ideal for shared resources.
- **Provides a Way to Maintain a Global Resource:** It allows centralized access to a global resource or service, which can be useful in managing application-wide configurations or state.
- **Supports Lazy Loading:** Many Singleton implementations allow the instance to be created only when it is first accessed, optimizing memory usage and startup performance.

  

---

### Cons of Singleton Pattern

- **Used with Parameters and Confused with Factory:** When a Singleton class requires parameters for instantiation, it may blur lines with the Factory pattern, leading to design confusion.
- **Hard to Write Unit Tests:** Since the Singleton holds a global state, it becomes difficult to isolate and mock for unit testing, thus potentially hindering testability.
- **Classes Using It Are Highly Coupled to It:** Components that depend on the Singleton become tightly coupled to its implementation, which reduces flexibility and makes code harder to maintain or refactor.
- **Special Cases to Avoid Race Conditions:** In multi-threaded environments, care must be taken to avoid race conditions during the instance creation phase, complicating implementation.
- **Violates the Single Responsibility Principle (SRP):** A Singleton often handles both instance control and its core functionality, thereby violating the SRP, a key principle of clean software design.