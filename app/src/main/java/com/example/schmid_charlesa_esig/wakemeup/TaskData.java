package com.example.schmid_charlesa_esig.wakemeup;

/**
 * Created by SCHMID_CHARLESA-ESIG on 06.03.2017.
 */

public class TaskData {
    //private variables
    int id;
    String title;
    String desc;

    // Empty constructor
    public TaskData(){

    }


    // constructor
    public TaskData(int id, String name, String email){
        this.id = id;
        this.title = name;
        this.desc = email;
    }

    // constructor
    public TaskData(String name, String email){
        this.title = name;
        this.desc = email;
    }


    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting name
    public String getName(){
        return this.title;
    }

    // setting name
    public void setName(String name){
        this.title = name;
    }

    // getting desc
    public String getDesc(){
        return this.desc;
    }

    // setting desc
    public void setDesc(String desc){
        this.desc = desc;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TaskInfo [name=" + title + ", desc=" + desc + "]";
    }

}
