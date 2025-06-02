# Flam-Android-Assignment-Set-2

This repository contains solutions for **three selected questions** from the Android Assignment Set 2. 
It includes:

- ✅ **Q1:** N-Queens Problem – Backtracking Algorithm  
- ✅ **Q2:** Circular Dependency Detection – Graph Cycle Detection  
- ✅ **Q4:** WeatherTrack App – Android App with MVVM Architecture, Room DB, and WorkManager

---

## ✅ Q1: N-Queens Puzzle – Backtracking Approach

📄 [Q1_Nqueens/Q1.java](./Q1_Nqueens/Q1.java)

### 🧠 Approach:

<p align="justify">
This solution is written in java language which uses a backtracking algorithm-based approach. In the algorithm, all the queens are placed one column at a time. This ensures that no two queens threaten each other at any point. Every valid queen placement is followed by a recursive move to the next column. If it successfully places a queen in each column (i.e., reaches the end of the board), it constructs the current board configuration into a list of strings and adds it to the final result. If a conflict is identified, the algorithm backtracks by removing the previously placed queen and trying the next possible position.
</p>

---


