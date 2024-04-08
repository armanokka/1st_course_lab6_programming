package network;

import entities.Route;
import interfaces.Command;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 20L;
    Command command;
    String[] args;
    Route route;

    public Request(Command command, String[] args) {
        this.command = command;
        this.args = args;
    }

    public Request(Command command, Route route) {
        this.command = command;
        this.route = route;
    }

    public Command getCommand(){
        return command;
    }
    public String[] getArgs(){
        return args;
    }
    public Route getRoute() { return route;}
}

