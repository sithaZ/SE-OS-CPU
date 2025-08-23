package com.example.scheduling;

import com.example.model.Process;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MLFQScheduler {

    private static final int TIME_QUANTUM_Q1 = 8;
    private static final int TIME_QUANTUM_Q2 = 16;
    private static final int AGING_THRESHOLD = 20; // Time after which a process is promoted

    public static void schedule(List<Process> processes) {
        // Queues for MLFQ
        Queue<Process> q1 = new LinkedList<>(); // Highest priority queue
        Queue<Process> q2 = new LinkedList<>();
        Queue<Process> q3 = new LinkedList<>(); // Lowest priority queue (FCFS)

        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;

        // Initialize remaining burst time and last execution time
        for (Process p : processes) {
            p.setRemainingBurstTime(p.getBurstTime());
            p.setLastExecutionTime(p.getArrivalTime());
        }

        List<Process> processList = new ArrayList<>(processes);
        processList.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        int processIndex = 0;
        while (completedProcesses.size() < processes.size()) {
            // Add newly arrived processes to the highest priority queue (Q1)
            while (processIndex < processList.size() && processList.get(processIndex).getArrivalTime() <= currentTime) {
                q1.add(processList.get(processIndex));
                processIndex++;
            }

            Process currentProcess = null;
            int timeQuantum = 0;
            int currentQueue = 0;

            // Select a process from the highest priority non-empty queue
            if (!q1.isEmpty()) {
                currentProcess = q1.poll();
                timeQuantum = TIME_QUANTUM_Q1;
                currentQueue = 1;
            } else if (!q2.isEmpty()) {
                currentProcess = q2.poll();
                timeQuantum = TIME_QUANTUM_Q2;
                currentQueue = 2;
            } else if (!q3.isEmpty()) {
                currentProcess = q3.poll();
                currentQueue = 3; // FCFS, no time quantum
            } else {
                // If all queues are empty, jump to the next process arrival time
                if (processIndex < processList.size()) {
                    currentTime = processList.get(processIndex).getArrivalTime();
                } else {
                    break; // No more processes to schedule
                }
                continue;
            }

            // Execute the process
            int executeTime = currentProcess.getRemainingBurstTime();
            if (currentQueue < 3) { // Apply time quantum for Q1 and Q2
                executeTime = Math.min(executeTime, timeQuantum);
            }

            currentTime += executeTime;
            currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - executeTime);
            currentProcess.setLastExecutionTime(currentTime);

            // Check for newly arrived processes during execution
            while (processIndex < processList.size() && processList.get(processIndex).getArrivalTime() <= currentTime) {
                q1.add(processList.get(processIndex));
                processIndex++;
            }

            // Handle process completion or demotion
            if (currentProcess.getRemainingBurstTime() <= 0) {
                // Process finished
                int finishTime = currentTime;
                int turnaroundTime = finishTime - currentProcess.getArrivalTime();
                int waitingTime = turnaroundTime - currentProcess.getBurstTime();

                currentProcess.setFinishTime(finishTime);
                currentProcess.setTurnaroundTime(turnaroundTime);
                currentProcess.setWaitingTime(waitingTime);
                completedProcesses.add(currentProcess);
            } else {
                // Demote the process to a lower priority queue
                if (currentQueue == 1) {
                    q2.add(currentProcess);
                } else if (currentQueue == 2) {
                    q3.add(currentProcess);
                } else { // currentQueue == 3
                    q3.add(currentProcess); // Stays in the lowest queue
                }
            }

            // Aging: Promote processes that have been waiting for too long
            promoteProcesses(q2, q1, currentTime);
            promoteProcesses(q3, q2, currentTime);
        }
    }

    private static void promoteProcesses(Queue<Process> fromQueue, Queue<Process> toQueue, int currentTime) {
        List<Process> toPromote = new ArrayList<>();
        for (Process p : fromQueue) {
            if (currentTime - p.getLastExecutionTime() > AGING_THRESHOLD) {
                toPromote.add(p);
            }
        }
        for (Process p : toPromote) {
            fromQueue.remove(p);
            toQueue.add(p);
        }
    }
}
