package observer;

public interface Observable {

    /**
     * notify observers to update their data
     */

    void notifyObservers();

    /**
     * add an observer to the observers list
     * @param observer the observer who needs to be added
     */

    void addObserver(Observer observer);

    /**
     * remove an observer from the observers list
     * @param observer the observer who needs to be removed
     */

    void removeObserver(Observer observer);
}
