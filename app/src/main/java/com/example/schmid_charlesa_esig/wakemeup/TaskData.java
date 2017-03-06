package com.example.schmid_charlesa_esig.wakemeup;

/**
 * Created by SCHMID_CHARLESA-ESIG on 06.03.2017.
 */

public class TaskData {
    //private variables
    int _id;
    String _title;
    String _desc;

    // Empty constructor
    public TaskData(){

    }


    // constructor
    public TaskData(int id, String name, String email){
        this._id = id;
        this._title = name;
        this._desc = email;
    }

    // constructor
    public TaskData(String name, String email){
        this._title = name;
        this._desc = email;
    }


    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._title;
    }

    // setting name
    public void setName(String name){
        this._title = name;
    }

    // getting email
    public String getEmail(){
        return this._desc;
    }

    // setting email
    public void setEmail(String email){
        this._desc = email;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TaskInfo [name=" + _title + ", email=" + _desc + "]";
    }

}
