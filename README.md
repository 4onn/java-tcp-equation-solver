# Java TCP Equation Solver

A university team project demonstrating TCP socket programming and multithreading in Java.

## Overview
The client sends four integers: A, B, C, and Target.
The server searches for a solution to:

Ax + By + Cz = Target

Each variable (x, y, z) is restricted to integers from 0 to 100.
The server returns the first solution found, or "No Answer" if none exists within this range.

## Features
- TCP client-server communication.
- Multiple clients handled through separate threads.
- Multiple requests over one connection.
- Client-side integer input validation.
- Request-response timing, including server processing.

## Requirements
- Java Development Kit (JDK).
- A terminal, or a Java IDE.

## Run
Both source files use the package `com.mycompany.cient`.

Before compiling, edit `REMOTE_HOST` in `Client.java`:
- Use `127.0.0.1` when running both programs on the same computer.
- Use the server computer's LAN IP when using two computers.

From the folder containing both Java files, compile:

```bash
javac -encoding UTF-8 -d out Server.java Client.java
```

Start the server:

```bash
java -cp out com.mycompany.cient.Server
```

In a second terminal, start the client:

```bash
java -cp out com.mycompany.cient.Client
```

The server listens on TCP port `46290`.
For separate computers, allow this port through the server's firewall on the private network.

## Usage
- Enter `N` to submit an equation.
- Enter the three coefficients and target.
- View the result and elapsed time.
- Enter `Q` to exit.

## Scope
This is an educational project using a bounded brute-force search.
"No Answer" means no solution was found within 0–100; it does not prove that no solution exists outside that range.

## Team
- Muath Ali Al-asmari
- Nawaf Sami Alzomaia
- Abdullah Mohammed Alharbi
