import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: Alain Adler
 * Date: 9/16/13
 * Time: 9:08 PM
 */
public class TestApp {

    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
        DB database = client.getDB("locking");
        CarOptimisticLockDAO dao = new CarOptimisticLockDAO(database);

        //drop the collection
        dao.getCollection().drop();

        //create a new document
        Car car = new Car();
        car.setColor("red");
        dao.save(car);

        //simulate another writer that changes the document.
        DBObject redCar = dao.getCollection().findOne(new BasicDBObject(Car.CarPropertyNames.COLOR, "red"));
        redCar.put(Car.CarPropertyNames.COLOR, "redish");
        dao.save(redCar);

        //change the original object and try to save the changes. It will fail because the document is dirty.
        try{
        car.setColor("dark red");
        dao.save(car);
        }
        catch (MongoException exc){
            exc.printStackTrace();
        }
    }
}
