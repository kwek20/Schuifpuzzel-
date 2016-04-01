package net.brord.schuifpuzzel.enums;

/**
 * Created by Brord on 4/1/2016.
 */
public enum Status {
    NO_ROOM(0),
    STARTED(1),
    SEARCHING(2);

    int id;

    Status(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Status getById(int id){
        for (Status s : values()){
            if (s.getId() == id) return s;
        }
        return null;
    }
}
