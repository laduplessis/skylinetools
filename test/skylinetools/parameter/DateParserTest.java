package skylinetools.parameter;

import junit.framework.TestCase;
import org.junit.Test;
import skylinetools.parameter.DateParser;
import test.beast.BEASTTestCase;


/**
 * @author Louis du Plessis
 *         Date: 2018/12/28
 */
public class DateParserTest extends TestCase {

    @Test
    public void testDateParser1() {

        System.out.println("DateParser: Dates in different formats");

        DateParser parser1 = new DateParser(),
                parser2 = new DateParser(),
                parser3 = new DateParser();

        parser1.initByName("dates","10/02/2005 22/02/2000 15/11/1999 01/01/2004 31/12/2004 01/01/2015 31/12/2015","format","dd/MM/yyyy");
        parser2.initByName("dates", "10-Feb-2005 22-Feb-2000 15-Nov-1999 1-Jan-2004 31-Dec-2004 1-Jan-2015 31-Dec-2015", "format","d-MMM-yyyy");
        parser3.initByName("dates", "  2005.1095890411 2000.1420765027   1999.8712328767 2004.0000000000 2004.9972677596 2015.0000000000 2015.9972602740");

        Double [] values1 = parser1.getValues();
        Double [] values2 = parser2.getValues();
        Double [] values3 = parser3.getValues();
        Double [] expected = {2005.1095890411, 2000.1420765027, 1999.8712328767, 2004.0000000000,
                              2004.9972677596, 2015.0000000000, 2015.9972602740};

        for (int i = 0; i < values2.length; i++) {
            assertEquals(expected[i], values1[i], BEASTTestCase.PRECISION);
            assertEquals(expected[i], values2[i], BEASTTestCase.PRECISION);
            assertEquals(expected[i], values3[i], BEASTTestCase.PRECISION);
        }
        //System.out.println(Arrays.toString(values1));
        //System.out.println(Arrays.toString(values2));
        //System.out.println(Arrays.toString(values3));
    }

}
