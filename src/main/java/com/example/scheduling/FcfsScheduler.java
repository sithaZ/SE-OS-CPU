package com.example.scheduling;

import com.example.model.Process;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class FcfsScheduler {

    public static void schedule(List<Process> processes) {
        // Create a mutable copy and sort by arrival time
        List<Process> sortedProcesses = new ArrayList<>(processes);
        sortedProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        for (Process process : sortedProcesses) {
            // If the CPU is idle, jump time forward to the process's arrival
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            int waitingTime = currentTime - process.getArrivalTime();
            int finishTime = currentTime + process.getBurstTime();
            int turnaroundTime = finishTime - process.getArrivalTime();

            process.setWaitingTime(waitingTime);
            process.setFinishTime(finishTime);
            process.setTurnaroundTime(turnaroundTime);

            // The next process will start after the current one finishes
            currentTime = finishTime;
        }
    }
}