/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 8, 2017
  * Time: 4:23:41 AM *
  * Project: csci205_hw
  * Package: hw01
  * File: PerceptronTest
  * Description:
  *
  * ****************************************
 */
package hw01;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author haoyuxiong
 */
public class PerceptronTest {

    static final double EPSILON = 1.0E-12;

    public PerceptronTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of calculateDeltaWeight method, of class Perceptron.
     */
    @Test
    public void testCalculateDeltaWeight() {
        System.out.println("calculateDeltaWeight");
        double prevFnet = 0.532;
        double littleDelta = 0.324;
        double expResult = 0.0344736;
        double result = Perceptron.calculateDeltaWeight(prevFnet, littleDelta);
        assertEquals(expResult, result, EPSILON);
    }

    /**
     * Test of step_function method, of class Perceptron.
     */
    @Test
    public void testStep_function() {
        System.out.println("step_function");
        double net = 0.00334;
        int expResult = 0;
        int result = Perceptron.step_function(net);
        assertEquals(expResult, result);
        net = 0.9992823;
        expResult = 1;
        result = Perceptron.step_function(net);
        assertEquals(expResult, result, EPSILON);
    }

    /**
     * Test of sigmoidal_activation_func method, of class Perceptron.
     */
    @Test
    public void testSigmoidal_activation_func() {
        System.out.println("sigmoidal_activation_func");
        Perceptron instance = new Perceptron(3);
        double net = 0.3;
        double expResult = 1.0 / (1 + Math.pow(Math.E, (-1) * net));
        double result = instance.sigmoidal_activation_func(net);
        assertEquals(expResult, result, EPSILON);
    }

}
