# SIT-N-NEW - Integrated Transport System 🚆🚌🚢

**SIT-N-NEW** is a Java desktop application designed to model, visualize, and analyze a public transportation network. Developed as the final project for the Advanced Programming course at Instituto Politécnico de Setúbal (IPS), it utilizes graph data structures and Object-Oriented Programming (OOP) principles to find the most efficient travel routes.

## About the Project
The core of this application revolves around representing a metropolitan transport network as a **Graph**, where vertices represent stops and edges represent travel routes.nThe system allows users to interactively visualize the map, calculate shortest paths based on multiple criteria (distance, time, or environmental sustainability), and manipulate the network dynamically.

## Key Features

### Network Visualization & Metrics
* *Interactive Map:** Visualizes the entire transport network using the `JavaFXSmartGraph` library.
* **Network Metrics:** Automatically calculates network statistics, including the number of isolated stops, available transport modes, and the longest possible paths using specific vehicles (e.g., longest train ride).

### Route Optimization (Shortest Path)
* **Multi-criteria Routing:** Calculates the optimal path between two stops based on:
  * **Distance** (Physical distance)
  * **Duration** (Time spent)
  * **Sustainability** (Carbon footprint / ecological impact)
  * **Transport Filtering:** Users can filter routes by allowing or excluding specific transport modes (Train, Bus, Boat, Walk).

### Network Management
* **Dynamic Editing:** Users can insert new stops via GPS coordinates or add new walking routes between existing stops.
* **Undo System:** Implements the **Command Design Pattern** to allow users to undo their modifications (insertions, deletions, or deactivations).
* **Persistence:** Export and import custom network configurations using JSON datasets.

## Architecture & Tech Stack

**Core Technologies:**
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=java&logoColor=white)

**Design Patterns & Principles:**
* **MVC (Model-View-Controller):** Strict separation of concerns between the business logic (graph and data processing) and the JavaFX GUI.
* **Command Pattern:** Used to encapsulate network modifications, enabling a robust Undo mechanism and modification history.
* **Graph Algorithms:** Custom implementation of Shortest Path algorithms (e.g., Dijkstra's algorithm) adapted for multi-weight edges.

## How to Run the Project

1. Clone this repository:
   ```bash
   git clone [https://github.com/your-username/sit-transport-network.git](https://github.com/your-username/sit-transport-network.git)
2. Clone this repository:
   Ensure you have Java (JDK 17 or higher) and JavaFX installed and configured on your machine.
3. Import the project into your preferred IDE (e.g., IntelliJ IDEA).
4. Run the application through the main interface class.
5. The application will load the default dataset (stops.json and routes.json) located in the /dataset folder.

## Authors
* Gonçalo Barracha
