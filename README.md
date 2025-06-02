# Flam-Android-Assignment-Set-2

This repository contains solutions for **three selected questions** from the Android Assignment Set 2. 
It includes:

- ✅ **Q1:** N-Queens Problem – Backtracking Algorithm  
- ✅ **Q2:** Circular Dependency Detection – Graph Cycle Detection  
- ✅ **Q4:** WeatherTrack App – Android App with MVVM Architecture, Room DB, and WorkManager

---

## Q1: N-Queens Puzzle – Backtracking Approach

📄 [Q1_Nqueens/Q1.java](./Q1_Nqueens/Q1.java)

### Approach:

<p align="justify">
This solution is written in Java language which uses a backtracking algorithm-based approach. In this algorithm, all the queens are placed in one column at a time. This ensures that no two queens threaten each other at any point. Every valid queen placement is followed by a recursive move to the next column. If all the queens are successfully placed in every column, the current board configuration is converted into a list of strings and added to the final result. If a conflict is identified, the algorithm backtracks by removing the previously placed queen and tries the next possible position.
</p>

---

## Q2: Module Dependency – Cycle Detection

📄 [Q2_ModuleLoader/Q2.java](./Q2_ModuleLoader/Q2.java)

### Approach:

<p align="justify">
This solution is written in Java language which solves the problem of detecting circular dependencies in a module loading system. The dependencies are considered as a directed graph where each module is a node and a dependency is a directed edge. The algorithm uses a DFS approach to traverse the graph and identify cycles. To identify cycles, it maintains two data structures: a visited list and a recursion stack. In the DFS traversal, if a node is encountered that is already in the current recursion stack and a cycle is detected, the function returns true. If all nodes are processed without detecting such a case, the graph is confirmed to be acyclic.
</p>

---

## Q4: WeatherTrack App

📄 [Q4_WeatherTrack](./Q4_WeatherTrack/)

---

<p align="justify">
The application is built using Java and Android Studio with the MVVM architecture. It is an android weather tracking app that fetches, stores, and displays current and weekly weather data with automatic background syncing every 6 hours and offline support through the usage of Room database.
</p>

---

### Key Features

- Displays weather data such as Temperature, Humidity, Condition, and Wind Speed.
- Fetches and saves weather data in the local Room Database with a timestamp.
- Auto sync weather data every 6 hours automatically in the background using WorkManager.
- Manual refresh option to update weather data.
- Displays temperature trends over the past 7 days.
- Detailed daily weather info is available by clicking on specific days.
- Implemented MVVM architecture with separate layers for Database, Model, Repository, Service, and ViewModel.
- Error handling for network failures.

---

### Tech Stack

- Java
- Android SDK
- MVVM Architecture
- Room Database
- WorkManager (for background sync)
- Mock API Service (for network calls)

---

### Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/fbd1aaee-7c72-4e58-b4c1-9e989fc4a216" width="200" />
  <img src="https://github.com/user-attachments/assets/a32bf31d-683a-4739-b0fc-e48df260e01f" width="200" />
  <img src="https://github.com/user-attachments/assets/67061bcc-804c-4ed3-b2e6-bc189be71465" width="200" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/ca5114cd-fa30-478b-878b-445abd67ff1d" width="200" />
  <img src="https://github.com/user-attachments/assets/722c0702-eb89-4bef-a36f-54b9e27b4aa7" width="200" />
</p>

---


### 🚀 How to Run

1. Clone the repository.
2. Open in Android Studio.
3. Build and run on an emulator or physical device.
4. The app fetches mock weather data and updates every 6 hours automatically.
5. Use the manual refresh button to trigger immediate updates.


