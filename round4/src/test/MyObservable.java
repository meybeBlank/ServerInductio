package test;

import java.util.Observable;

/**
 * 被观察者
 *
 * @author fengzhen
 * @version v1.0, 2017/9/5 17:17
 */
public class MyObservable extends Observable {

    public void commit(){
        setChanged();
        notifyObservers("commit");
    }

    public void rollback(){
        setChanged();
        notifyObservers("rollback");
    }

    public void close(){
        setChanged();
        notifyObservers("close");
    }

    public void changeState(boolean auto){
        setChanged();
        if (auto) {
            notifyObservers("aotu");
        } else {
            notifyObservers("unauto");
        }
    }
}
