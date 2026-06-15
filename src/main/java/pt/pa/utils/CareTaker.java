package pt.pa.utils;

import pt.pa.graph.TransportMap;
import pt.pa.model.Route;
import pt.pa.view.MapView;

import java.util.Stack;

public class CareTaker{
    private Stack<Memento> mementos;
    private Originator originator;

    public CareTaker(Originator originator) {
        this.mementos = new Stack<>();
        this.originator = originator;
    }

    public void saveState() {
        mementos.push(originator.createMemento());
    }

    public void restoreState() {
        if(!mementos.isEmpty()) {
            originator.setMemento(mementos.pop());

        }
    }
}