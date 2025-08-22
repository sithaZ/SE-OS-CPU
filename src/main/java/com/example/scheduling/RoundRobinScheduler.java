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
        
        // Reset remaining burst time for all processes before scheduling
        for(Process p : processes) {
            p.setRemainingBurstTime(p.getBurstTime());
        }

        processList.sort(Comparator.comparingInt(Process::getArrivalTime));
        
        int currentTime = 0;
        int completedCount = 0;
        int processIndex = 0;

        while(completedCount < processes.size()) {
            // Add any newly arrived processes to the ready queue
            while (processIndex < processList.size() && processList.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(processList.get(processIndex));
                processIndex++;
            }

            if (readyQueue.isEmpty()) {
                // If the ready queue is empty but there are still processes to arrive, jump time
                if (processIndex < processList.size()) {
                    currentTime = processList.get(processIndex).getArrivalTime();
                } else {
                    // All processes have arrived and the queue is empty, so we are done.
                    break;
                }
                continue; // Re-check for arrived processes at the new time
            }
            
            Process currentProcess = readyQueue.poll();
            
            // Determine how long this process will run in this turn
            int executeTime = Math.min(quantum, currentProcess.getRemainingBurstTime());
            currentTime += executeTime;
            currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - executeTime);
            
            
            while (processIndex < processList.size() && processList.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(processList.get(processIndex));
                processIndex++;
            }

            if (currentProcess.getRemainingBurstTime() > 0) {
                // If the process is not finished, add it to the back of the queue
                readyQueue.add(currentProcess);
            } else {
                // Process has finished
                int finishTime = currentTime;
                int turnaroundTime = finishTime - currentProcess.getArrivalTime();
                int waitingTime = turnaroundTime - currentProcess.getBurstTime();
                
                currentProcess.setFinishTime(finishTime);
                currentProcess.setTurnaroundTime(turnaroundTime);
                currentProcess.setWaitingTime(waitingTime);
                completedCount++;
            }
        }
    }
}