package com.pullbots.app;


import java.util.PriorityQueue;
import java.util.Queue;

public class EQueue {
    public Queue<VkUser> queue;
    public String discipline, users = "";
    public int c = 1;

    public EQueue(String discipline) {
        this.queue = new PriorityQueue<>();
        this.discipline = discipline;
    }
    public String getStrParticipants(){
        return users;
    }
    public void addUser(VkUser user){
        queue.add(user);
        users += c + ". " + user.getName() + " " + user.getLastname() + "\n";
        c++;
    }
    public boolean userIsInQueue(Integer id){
        for (VkUser user : queue){
            if (user.getId() == id) return true;
        }
        return false;
    }
}
