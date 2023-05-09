import java.util.Random;

public class NumberGenerator {


    private static final Random random = new Random();


    public static int generateRandomNumber(int maxValue, int minValue) {
        return random.nextInt(maxValue - minValue + 1) + minValue;
    }


}
