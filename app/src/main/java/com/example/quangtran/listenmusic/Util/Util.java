package com.example.quangtran.listenmusic.Util;

public class Util {
    //display total time
    public String TotalTime(long milliseconds) {
        long hours, minutes, seconds;
        String time;
        if(milliseconds > 0) {
            milliseconds = milliseconds / 1000;
            hours = milliseconds / 3600;
            minutes = (milliseconds % 3600) / 60;
            seconds = ((milliseconds % 3600) % 60);
            if (hours > 0) {
                time = hours + ":" + minutes + ":" + seconds;
            } else {
                time = minutes + ":" + seconds;
            }
        }
        else{
            time = "0:0";
        }
        return time;

    }

    //update progressbar
    public static int  getProgressPercentage(long currentDuration,long totalDuration){
        Double percentage = (double) 0;
        long currentSeconds = (int) (currentDuration/1000);
        long totalSeconds = (int) (totalDuration/1000);
        double phantram =(double) (currentSeconds*100/totalSeconds);
        percentage =  ((double)(currentSeconds* 100/totalSeconds));

        return  percentage.intValue();
    }
    //khi keo progress thi nhac se dung tai do
    public static int progressToTimer(int progress,int totalDuration){
        int currentDuration = 0;
        totalDuration = totalDuration/1000;
        currentDuration = (int) ((((double)progress)/100) * totalDuration);
        return currentDuration * 1000;
    }
}
