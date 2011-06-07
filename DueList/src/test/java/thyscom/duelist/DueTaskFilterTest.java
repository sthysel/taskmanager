package thyscom.duelist;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author thys
 */
public class DueTaskFilterTest {

    private static DueTaskFilter filter;

    public DueTaskFilterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        filter = new DueTaskFilter(null);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        filter = null;
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInterval method, of class DueTaskFilter.
     */
    @Test
    public void testGetInterval() {
        System.out.println("getInterval");
        DateTime now = new DateTime();
        int week = now.getWeekOfWeekyear();
        Interval interval = filter.getInterval(week);
        System.out.println(now);
        System.out.println(interval.getStart());
        System.out.println(interval.getEnd());
        assert (interval.containsNow());
    }
}
