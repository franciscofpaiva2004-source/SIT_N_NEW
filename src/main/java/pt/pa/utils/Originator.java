package pt.pa.utils;

import pt.pa.model.Route;
import pt.pa.view.MapView;

public interface Originator {

    public Memento createMemento();

    public void setMemento(Memento savedState);
}
