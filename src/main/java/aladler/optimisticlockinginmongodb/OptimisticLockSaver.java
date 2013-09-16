package aladler.optimisticlockinginmongodb;

import com.mongodb.*;
import org.bson.types.ObjectId;

/**
 * Created with IntelliJ IDEA.
 * User: Alain Adler
 * Date: 9/16/13
 * Time: 11:37 PM
 */
public class OptimisticLockSaver {
    private DBCollection collection;

    private static final String ID = "_id";
    private static final String READ_ID = "readId";

    public OptimisticLockSaver(DBCollection collection) {
        this.collection = collection;
    }

    public DBCollection getCollection(){
        return collection;
    }

    public WriteResult save(DBObject dbObject) {
        if (dbObject.get(ID) == null)
            return insert(dbObject);
        else
            return update(dbObject);
    }

    private WriteResult insert(DBObject dbObject) {
        dbObject.put(READ_ID, createReadId());
        return collection.insert(dbObject);
    }

    private String createReadId() {
        return ObjectId.get().toString();
    }

    private WriteResult update(DBObject dbObject) {
        String originalReadId = (String) dbObject.get(READ_ID);
        ObjectId id = (ObjectId)dbObject.get(ID);
        DBObject query = new BasicDBObject(READ_ID, originalReadId).append(ID, id);
        dbObject.put(READ_ID, createReadId());
        WriteResult result = collection.update(query, dbObject);
        int docsAffected = result.getN();
        if (docsAffected == 0) {
            DBObject existsWithIdQuery = new BasicDBObject(ID, id);
            DBObject existsObject = collection.findOne(existsWithIdQuery);
            if (existsObject != null) {
                dbObject.put(READ_ID, originalReadId);
                throw new MongoException("Document was modified by another writer");
            }
        }
        return result;
    }

}
