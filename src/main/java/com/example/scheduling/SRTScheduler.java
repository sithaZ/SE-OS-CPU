package com.example.scheduling;

import java.util.List;
import com.example.model.Process;

public class SRTScheduler {
    public static void schedule(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        
        while (completed != n) {
            int shortest = -1;
            int minBurst = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                if (p.getArrivalTime() <= currentTime && p.getRemainingBurstTime() < minBurst && p.getRemainingBurstTime() > 0) {
                    minBurst = p.getRemainingBurstTime();
                    shortest = i;
                }
            }

            if (shortest == -1) {
                currentTime++;
                continue;
            }

            Process currentProcess = processes.get(shortest);
             if (!currentProcess.hasStarted()) {
                currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setHasStarted(true);
            }

            currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
            currentTime++;

            if (currentProcess.getRemainingBurstTime() == 0) {
                completed++;
                currentProcess.setFinishTime(currentTime);
                currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
            }
        }
    }
}