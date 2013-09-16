
import aladler.optimisticlockinginmongodb.OptimisticLockSaver;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * Created with IntelliJ IDEA.
 * User: Alain Adler
 * Date: 9/16/13
 * Time: 11:17 PM
 */
public class CarOptimisticLockDAO extends OptimisticLockSaver {

    private static final String CollectionName = "Cars";

    public CarOptimisticLockDAO(DB database) {
        super(database.getCollection(CollectionName));
    }

}
