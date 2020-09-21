package Data;

import ObjectClass.Clinic;
import java.rmi.RemoteException;

public class ServerSort implements Sort { // класс, реализующий интерфейс sort

    public Clinic sort(Clinic a) throws RemoteException {
        a.sort();
        return a;
    }
}
