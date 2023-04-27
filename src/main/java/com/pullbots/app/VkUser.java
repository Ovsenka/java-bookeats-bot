package com.pullbots.app;

public class VkUser implements Comparable<VkUser>{
    private int id;
    private String name, lastname;

    public VkUser(int id, String name, String lastname) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getLastname() {
        return lastname;
    }

    @Override
    public int compareTo(VkUser user) {
        //implement comparison here
        return 0;
    }

}
