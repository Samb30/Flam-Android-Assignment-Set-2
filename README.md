# Flam-Android-Assignment-Set-2

This repository contains solutions for **three selected questions** from the Android Assignment Set 2. 
It includes:

- ✅ **Q1:** N-Queens Problem – Backtracking Algorithm  
- ✅ **Q2:** Circular Dependency Detection – Graph Cycle Detection  
- ✅ **Q4:** WeatherTrack App – Android App with MVVM Architecture, Room DB, and WorkManager

---

## Q1: N-Queens Puzzle – Backtracking Approach

📄 [Q1_Nqueens/Q1.java](./Q1_Nqueens/Q1.java)

### 🧠 Approach:

<p align="justify">
This solution is written in Java language which uses a backtracking algorithm-based approach. In this algorithm, all the queens are placed in one column at a time. This ensures that no two queens threaten each other at any point. Every valid queen placement is followed by a recursive move to the next column. If all the queens are successfully placed in every column, the current board configuration is converted into a list of strings and added to the final result. If a conflict is identified, the algorithm backtracks by removing the previously placed queen and tries the next possible position.
</p>

---

## Q2: Module Dependency – Cycle Detection

📄 [Q2_ModuleLoader/Q2.java](./Q2_ModuleLoader/Q2.java)

### 🧠 Approach:

<p align="justify">
This solution is written in Java language which solves the problem of detecting circular dependencies in a module loading system. The dependencies are considered as a directed graph where each module is a node and a dependency is a directed edge. The algorithm uses a DFS approach to traverse the graph and identify cycles. To identify cycles, it maintains two data structures: a visited list and a recursion stack. In the DFS traversal, if a node is encountered that is already in the current recursion stack and a cycle is detected, the function returns true. If all nodes are processed without detecting such a case, the graph is confirmed to be acyclic.
</p>


