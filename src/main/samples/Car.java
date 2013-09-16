
import com.mongodb.BasicDBObject;

/**
 * Created with IntelliJ IDEA.
 * User: Alain Adler
 * Date: 9/16/13
 * Time: 11:17 PM
 */
public class Car extends BasicDBObject {

    public String getColor() {
        return getString(CarPropertyNames.COLOR);
    }

    public void setColor(String color) {
        put(CarPropertyNames.COLOR, color);
    }

    public static class CarPropertyNames{
        public static final String COLOR = "color";
    }
}
