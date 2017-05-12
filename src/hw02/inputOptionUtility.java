/* *****************************************
  * CSCI205 - Software Engineering and Design
  * Spring 2017 *
  * Name: NAMES of team members
  * Date: Mar 21, 2017
  * Time: 5:14:57 PM *
  * Project: csci205_hw
  * Package: hw02
  * File: inputOptionUtility
  * Description:
  *
  * ****************************************
 */
package hw02;

import java.util.Scanner;

/**
 * Utility class for handling user input options
 *
 * @author Jingya
 */
public final class inputOptionUtility {

    /**
     * Test if the input is either 1 or 2, prompt error message and keep asking
     * until get the correct format input.
     *
     * @return int option of either 1 or 2
     */
    public static int oneOrTwo(Scanner in) {

        int option;

        do {
            System.out.print("Enter 1 or 2: ");

            String temp = in.next();

            try {
                option = Integer.parseInt(temp);

                if (option == 1 || option == 2) {
                    break;
                }
            } catch (Exception e) {
            }

            System.out.println("Invalid input, please try again!");

        } while (true);

        return option;
    }

    /**
     * Test if the input is either 1, 2, or 3, prompt error message and keep
     * asking until get the correct format input.
     *
     * @return int option of 1, 2, or 3
     */
    public static int oneTwoOrThree(Scanner in) {

        int option;

        do {
            System.out.print("Enter 1, 2, or 3: ");

            String temp = in.next();

            try {
                option = Integer.parseInt(temp);

                if (option == 1 || option == 2 || option == 3) {
                    break;
                }
            } catch (Exception e) {
            }

            System.out.println("Invalid input, please try again!");

        } while (true);

        return option;
    }
}
