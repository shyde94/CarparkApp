package Carparks;

import java.util.ArrayList;

/**
 * Created by Shide on 31/3/17.
 */

public interface ObjectAccessInterface {
    ArrayList<Carpark> getCpList();
    void retrieveCarparks();
    void createHDBCarparkObjects(int id);
    void createDMCarparkObject(int id);
    void ObjectCreater(String owner, int id);

}
