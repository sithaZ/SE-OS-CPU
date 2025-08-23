package com.example.scheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.example.model.Process;

public class SjfScheduler {
    public static void schedule(List<Process> processes) {
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completed = new ArrayList<>();
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        int currentTime = 0;
        int processIndex = 0;

        while (completed.size() < processes.size()) {
            while (processIndex < processes.size() && processes.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(processIndex));
                processIndex++;
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            readyQueue.sort(Comparator.comparingInt(Process::getBurstTime));
            Process currentProcess = readyQueue.remove(0);

            if (!currentProcess.hasStarted()) {
                currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setHasStarted(true);
            }
            
            currentTime += currentProcess.getBurstTime();
            currentProcess.setFinishTime(currentTime);
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
            currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

            completed.add(currentProcess);
        }
    }
}