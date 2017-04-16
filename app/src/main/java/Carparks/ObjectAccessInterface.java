package Carparks;

import java.util.ArrayList;

/**
 * interface used to access data in database
 */

public interface ObjectAccessInterface {
    ArrayList<Carpark> getCpList();
    void retrieveCarparks();
    void createHDBCarparkObjects(int id);
    void createDMCarparkObject(int id);
    void ObjectCreater(String owner, int id);
    void createURACarparkObject(int id);


}
