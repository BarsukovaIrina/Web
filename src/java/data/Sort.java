package Data;

import ObjectClass.Clinic;
import java.rmi.RemoteException;
import java.rmi.Remote;

public interface Sort extends Remote {

    public Clinic sort(Clinic a) throws RemoteException;
}
