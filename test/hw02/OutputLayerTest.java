/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 8, 2017
  * Time: 4:23:41 AM *
  * Project: csci205_hw
  * Package: hw01
  * File: OutputLayerTest
  * Description:
  *
  * ****************************************
 */
package hw02;

import hw01.*;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author haoyuxiong
 */
public class OutputLayerTest {

    static final double EPSILON = 1.0E-12;

    public OutputLayerTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of calculateLittleDeltaOfOutputLayer method, of class OutputLayer.
     */
    @Test
    public void testCalculateLittleDeltaOfOutputLayer() {
        System.out.println("calculateLittleDeltaOfOutputLayer");
        double expected = 1.0;
        double actual = 0.8;
        OutputLayer instance = new OutputLayer(3);
        double expResult = 0.032;
        double result = instance.calculateLittleDeltaOfOutputLayer(expected,
                                                                   actual);
        assertEquals(expResult, result, EPSILON);

    }
}
