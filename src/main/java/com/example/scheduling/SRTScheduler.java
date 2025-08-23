package com.example.scheduling;

import java.util.List;
import com.example.model.Process;

public class SRTScheduler {

    public static void schedule(List<Process> processes) {
        // Initialize remaining burst time for all processes
        for (Process p : processes) {
            p.setRemainingBurstTime(p.getBurstTime());
        }

        int currentTime = 0;
        int completedCount = 0;
        int totalProcesses = processes.size();

        while (completedCount < totalProcesses) {
            Process selectedProcess = null;
            int minRemainingTime = Integer.MAX_VALUE;

            // Find the process with the shortest remaining time that has arrived
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingBurstTime() > 0 && p.getRemainingBurstTime() < minRemainingTime) {
                    minRemainingTime = p.getRemainingBurstTime();
                    selectedProcess = p;
                }
            }

            if (selectedProcess == null) {
                // If no process is ready, advance time
                currentTime++;
                continue;
            }

            // Decrement remaining time for the selected process
            selectedProcess.setRemainingBurstTime(selectedProcess.getRemainingBurstTime() - 1);
            currentTime++;

            // Check if the process has completed
            if (selectedProcess.getRemainingBurstTime() == 0) {
                completedCount++;
                int finishTime = currentTime;
                int turnaroundTime = finishTime - selectedProcess.getArrivalTime();
                int waitingTime = turnaroundTime - selectedProcess.getBurstTime();

                selectedProcess.setFinishTime(finishTime);
                selectedProcess.setTurnaroundTime(turnaroundTime);
                selectedProcess.setWaitingTime(waitingTime);
            }
        }
    }
}