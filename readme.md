# Arbitrary Precision Arithmetic - Java

This project is a Java library to do maths on very big numbers. It can do **add, subtract, multiply, divide** for both integer and float numbers without losing digits. We use **String** to store the number so it works even for big numbers.

## Files

- `AInteger.java` – for doing big integer math  
- `AFloat.java` – for doing float math  
- `MyInfArith.java` – main file to run with command line  
- `pom.xml` – for Maven build 
- `run_tests.py` - for test cases and to check whether maven exists and installed. 

## How to Build

You can build it with Maven:

```bash
mvn clean package
```

## How to Run

Running python file.Python file is created. Can add some more test cases there.

## Features

- Big int add, sub, mul, div
- Big float add, sub, mul, div
- Up to 30 digits after point 
- will perform arithmetic operations

## Git Info

Project is pushed to GitHub.

## Limitations

- Float multiply and divide is little slow  
- Not doing rounding, only cutting at 30 digits  
- Need more test cases  

## Learnings

I learn how to use Git, how to make class in Java, how to split float into integer and decimal part, and how to use Maven.

