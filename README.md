# Java Design Patterns
Java design patterns

## Requisitos

- JDK 25 (LTS) — [`C:\Dev\Bins\Java\jdk-25.0.4.1`](C:\Dev\Bins\Java\jdk-25.0.4.1)

## Build and execution

```
mvn package
java -cp target\classes com.jonathas.HelloWorld
```

## Build by on specific java version by path!

```
$env:JAVA_HOME = "C:\Dev\Bins\Java\jdk-25.0.4.1"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

java --version

mvn package
java -cp target\classes com.jonathas.HelloWorld
```

## SOLID Content

### Single-Responsibility Principle (SRP)

`com.jonathas.HelloWorld`

### Open/Closed Principle (OCP)

`com.jonathas.HelloWorld`

### Liskov Substitution Principle (LSP)

`com.jonathas.HelloWorld`

### Interface Segregation Principle (ISP)

`com.jonathas.HelloWorld`

### Dependency Inversion Principle (DIP)

`com.jonathas.HelloWorld`