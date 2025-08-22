package com.example.scheduling;

import com.example.model.Process;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SjfScheduler {

    public static void schedule(List<Process> processes) {
        List<Process> remainingProcesses = new ArrayList<>(processes);
        List<Process> completed = new ArrayList<>();
        int currentTime = 0;

        if (!remainingProcesses.isEmpty()) {
            remainingProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));
            currentTime = remainingProcesses.get(0).getArrivalTime();
        }

        while (completed.size() < processes.size()) {
            List<Process> availableProcesses = new ArrayList<>();
            for (Process p : remainingProcesses) {
                if (p.getArrivalTime() <= currentTime) {
                    availableProcesses.add(p);
                }
            }

            if (availableProcesses.isEmpty()) {

                if (!remainingProcesses.isEmpty()) {
                    currentTime = remainingProcesses.get(0).getArrivalTime();
                }
                continue;
            }

            availableProcesses.sort(Comparator.comparingInt(Process::getBurstTime));
            Process currentProcess = availableProcesses.get(0);

            int waitingTime = currentTime - currentProcess.getArrivalTime();
            int finishTime = currentTime + currentProcess.getBurstTime();
            int turnaroundTime = finishTime - currentProcess.getArrivalTime();

            currentProcess.setWaitingTime(waitingTime);
            currentProcess.setFinishTime(finishTime);
            currentProcess.setTurnaroundTime(turnaroundTime);

            currentTime = finishTime;
            completed.add(currentProcess);
            remainingProcesses.remove(currentProcess);
        }
    }
}