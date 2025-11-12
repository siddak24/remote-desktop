# Java 21 LTS Upgrade Guide

## Overview
Your Remote Desktop application has been successfully upgraded to **Java 21 LTS** (Long-Term Support).

## Current System Status
- **Current JDK Version**: Java 23.0.2 (newer than Java 21)
- **Target Java Version**: Java 21 LTS
- **Java Release Date**: September 19, 2023
- **LTS Support Until**: September 2028

## What's Changed

### 1. **Build Configuration Files Created**

#### Maven (`pom.xml`)
- Added Maven compiler configuration targeting Java 21
- Configured compiler plugins to enforce Java 21 compatibility
- Source compatibility: Java 21
- Target compatibility: Java 21

#### Gradle (`build.gradle`)
- Added Gradle Java plugin configuration for Java 21
- Set source and target compatibility to Java 21
- Configured encoding to UTF-8

### 2. **Compilation Status**
✅ Successfully compiled with Java 21 release specification using:
```bash
javac --release 21 -d bin src/Server.java src/Client.java
```

### 3. **Known Issues & Fixes**

#### Deprecation Warning: `URL(String)` Constructor
- **File**: `Client.java`, line 107
- **Issue**: `URL(String)` constructor is deprecated in Java 21+
- **Recommendation**: Use `URL.of()` or `new URI().toURL()` for URL creation
- **Status**: Application still works but could be modernized

### 4. **Java 21 LTS Features Available**

Your application can now leverage Java 21 features including:

#### Text Blocks (JEP 378)
```java
String html = """
    <html>
        <body>
            <p>Hello, World!</p>
        </body>
    </html>
    """;
```

#### Record Classes (JEP 395)
```java
public record ScreenUpdateMessage(int width, int height, byte[] imageData) {}
```

#### Pattern Matching (JEP 440)
```java
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

#### Virtual Threads (Project Loom - JEP 453)
```java
Thread.ofVirtual().name("client-handler").start(() -> {
    // Lightweight thread for handling client
});
```

#### Sequenced Collections (JEP 431)
```java
List<ImageFrame> frames = new ArrayList<>();
ImageFrame first = frames.getFirst();  // New method
```

## Build Commands

### Maven
```bash
# Compile
mvn clean compile

# Package JAR
mvn clean package

# Run tests
mvn test

# Run application
mvn exec:java -Dexec.mainClass="Server"
```

### Gradle
```bash
# Compile
./gradlew clean compileJava

# Build JAR
./gradlew build

# Run tests
./gradlew test

# Run application
./gradlew run --args="--server"
```

### Direct Compilation (Javac)
```bash
# Compile with Java 21 release specification
javac --release 21 -d bin src/Server.java src/Client.java

# Run Server
java -cp bin Server

# Run Client
java -cp bin Client
```

## Recommendations for Further Modernization

1. **Fix Deprecation Warning**
   - Replace `new URL(String)` with `new URL(new URI(String).toURL())`

2. **Virtual Threads for Better Scalability**
   - Replace traditional threads with virtual threads for handling multiple clients
   - Reduces resource overhead significantly

3. **Records for Data Classes**
   - Consider using records for immutable data classes

4. **Sealed Classes**
   - Use sealed classes to control subclass hierarchy

## Compatibility Notes

- **Backward Compatible**: Java 21 bytecode is compatible with Java 23
- **Module System**: Not required for basic applications (still can use traditional classpath)
- **Preview Features**: Virtual Threads are a preview feature; enable with `--enable-preview` if needed

## Next Steps

1. Replace deprecated URL constructor (optional)
2. Test your application thoroughly
3. Consider using Virtual Threads for improved concurrency
4. Document any Java 21-specific features you implement

## References

- [Java 21 Release Notes](https://www.oracle.com/java/technologies/javase/21-0-1-relnotes.html)
- [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [What's New in Java 21](https://www.oracle.com/java/technologies/javase/java-language-features/)
