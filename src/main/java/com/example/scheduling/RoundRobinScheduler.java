package com.example.scheduling;

import com.example.model.Process;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Comparator;

public class RoundRobinScheduler {

    public static void schedule(List<Process> processes, int quantum) {
    List<Process> processList = new ArrayList<>(processes);
    Queue<Process> readyQueue = new LinkedList<>();

    for (Process p : processes) {
        p.setRemainingBurstTime(p.getBurstTime());
        p.setHasStarted(false); // Ensure hasStarted is reset
    }

    processList.sort(Comparator.comparingInt(Process::getArrivalTime));

    int currentTime = 0;
    int completedCount = 0;
    int processIndex = 0;

    while (completedCount < processes.size()) {
        // Add any newly arrived processes to the ready queue
        while (processIndex < processList.size() && processList.get(processIndex).getArrivalTime() <= currentTime) {
            readyQueue.add(processList.get(processIndex));
            processIndex++;
        }

        if (readyQueue.isEmpty()) {
            if (processIndex < processList.size()) {
                currentTime = processList.get(processIndex).getArrivalTime();
            } else {
                break;
            }
            continue;
        }

        Process currentProcess = readyQueue.poll();

        // **FIX**: Calculate response time on first execution
        if (!currentProcess.hasStarted()) {
            currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
            currentProcess.setHasStarted(true);
        }

        int executeTime = Math.min(quantum, currentProcess.getRemainingBurstTime());
        currentTime += executeTime;
        currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - executeTime);

        // **FIX**: Add newly arrived processes that came in DURING the execution
        while (processIndex < processList.size() && processList.get(processIndex).getArrivalTime() <= currentTime) {
            readyQueue.add(processList.get(processIndex));
            processIndex++;
        }

        if (currentProcess.getRemainingBurstTime() > 0) {
            // **FIX**: Add the current (preempted) process back to the queue
            readyQueue.add(currentProcess);
        } else {
            // Process has finished
            currentProcess.setFinishTime(currentTime);
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
            currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
            completedCount++;
        }
    }
}
}