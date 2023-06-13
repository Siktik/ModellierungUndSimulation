package Utils;

import java.security.SecureRandom;

public class NumberGenerator {

    /**
     * classic number generator
     */
    private static final SecureRandom random = new SecureRandom();


    public static int generateRandomNumber(int maxValue, int minValue) {
        return random.nextInt(maxValue - minValue + 1) + minValue;
    }


}
