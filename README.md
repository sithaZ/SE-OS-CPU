# CPU Scheduling Algorithm Visualizer

![CPU Visualizer](src/main/resources/image/cpus.jpg)

A JavaFX application that visualizes various CPU scheduling algorithms. Users can input processes, select an algorithm, and view the results in a summary table and a Gantt chart.

---

## Table of Contents

- [CPU Scheduling Algorithm Visualizer](#cpu-scheduling-algorithm-visualizer)
  - [Table of Contents](#table-of-contents)
  - [Setup Instructions](#setup-instructions)
    - [1. Install VS Code Extensions](#1-install-vs-code-extensions)
    - [2. Configure Project Settings](#2-configure-project-settings)
    - [3. Configure Launch Settings](#3-configure-launch-settings)
  - [Build and Run](#build-and-run)
  - [Algorithms Implemented](#algorithms-implemented)
  - [How to Use](#how-to-use)
  - [Features](#features)
  - [Project Structure](#project-structure)
  - [Developers Info](#developers-info)
  - [Guidance](#guidance)

---

## Setup Instructions

### 1. Install VS Code Extensions

Make sure you have the following extensions installed in Visual Studio Code:
- Language Support for Java(TM) by Red Hat
- Debugger for Java

### 2. Configure Project Settings

Update `.vscode/settings.json` to include the JavaFX library path. Replace `/path/to/your/javafx-sdk/` with the actual path to your JavaFX SDK.

```json
{
  "java.project.referencedLibraries": [
    "lib/**/*.jar",
    "/path/to/your/javafx-sdk/lib/*.jar"
  ]
}
```

### 3. Configure Launch Settings

Update `.vscode/launch.json` to include the necessary VM arguments for JavaFX.

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Launch App",
      "request": "launch",
      "mainClass": "com.example.App",
      "vmArgs": "--module-path /path/to/your/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml"
    }
  ]
}
```

> **Note:** Replace `/path/to/your/javafx-sdk/` with your actual JavaFX SDK path.

---

## Build and Run

Open a terminal in the project's root directory and run the following commands:

**Clean and install dependencies:**
```sh
mvn clean install
```

**Run the application:**
```sh
mvn javafx:run
```

---

## Algorithms Implemented

- **First-Come, First-Served (FCFS):** Processes are executed in the order they arrive.
- **Shortest Job First (SJF) - Non-Preemptive:** The process with the smallest burst time is executed next.
- **Shortest Remaining Time (SRT) - Preemptive:** Always chooses the process with the shortest remaining execution time.
- **Round Robin (RR):** Each process is assigned a fixed time quantum in a circular queue.
- **Multilevel Feedback Queue (MLFQ):** Processes are moved between several queues with different priorities and algorithms.
  - Queue 0 (Highest Priority): Round Robin with a custom time quantum.
  - Queue 1 (Medium Priority): Round Robin with a custom time quantum.
  - Queue 2 (Lowest Priority): FCFS with an aging threshold to prevent starvation.

---

## How to Use

1. **Enter Process Data:** Input space-separated arrival times and burst times for your processes.
2. **Add Processes:** Click the "Add Processes" button to load the data.
3. **Select an Algorithm:** Choose your desired scheduling algorithm from the dropdown menu.
4. **Provide Inputs:** If required, enter algorithm-specific parameters (e.g., Time Quantum for RR, Aging Threshold for MLFQ).
5. **Solve & View Results:** Click "Solve" to generate the results. The application will display:
    - A results table with Finish Time, Turnaround Time, and Waiting Time for each process.
    - A Gantt chart visualizing the execution timeline.
    - Average waiting time, turnaround time, and response time.

---

## Features

- **Export to CSV:** Save the results table to a `process_results.csv` file for analysis.
- **Reset:** Clear all inputs, results, and the Gantt chart to start over.

---

## Project Structure

```
CPU-Scheduling-Visualizer/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── App.java
├── lib/
├── .vscode/
│   ├── settings.json
│   └── launch.json
└── pom.xml
```

---

## Developers Info

- Huoth Sitha – Institute of Technology of Cambodia
- Kheang Ann – Institute of Technology of Cambodia
  
## Guidance
- This Project guidance by Mr. Heng RathPisey

