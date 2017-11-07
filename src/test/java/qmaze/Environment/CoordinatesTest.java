package test.java.qmaze.Environment;

import qmaze.Environment.Location;

/**
 *
 * @author katharine
 */
public class CoordinatesTest {
    
    public static void main(String args[]) {
        CoordinatesTest ct = new CoordinatesTest();
        ct.testParseFromString();
    }
    
    public void testParseFromString() {
        Location c = Location.parseFromString("{\"col\":0,\"row\":0}");
        System.out.println(c);
    }
}
