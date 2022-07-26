import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CarNumberCreator {
    private static final String PATH = "src/main/resources/";

    public void create(String fileName, int regionCodeStart, int regionCodeStop, char[] letters) {
        try {
            PrintWriter writer = new PrintWriter(PATH + fileName + ".txt");

            for (int code = regionCodeStart; code <= regionCodeStop; code++) {
                StringBuilder builder = new StringBuilder();
                generateNumber(letters, builder, code);
                writer.write(builder.toString().trim());
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void generateNumber(char[] letters, StringBuilder builder, int regionCode) {
        for (int number = 1; number < 1000; number++) {
            for (char firstLetter : letters) {
                for (char secondLetter : letters) {
                    for (char thirdLetter : letters) {
                        builder.append(firstLetter);

                        if (number < 100) {
                            builder.append(padNumber(number, 3));
                        } else {
                            builder.append(number);
                        }
                        builder.append(secondLetter)
                                .append(thirdLetter);

                        if (regionCode < 10) {
                            builder.append(padNumber(regionCode, 2));
                        } else {
                            builder.append(regionCode);
                        }
                        builder.append('\n');
                    }
                }
            }
        }
    }

    private String padNumber(int number, int numberLength) {
        StringBuilder numberStr = new StringBuilder(Integer.toString(number));
        int padSize = numberLength - numberStr.length();

        for (int i = 0; i < padSize; i++) {
            numberStr.insert(0, '0');
        }
        return numberStr.toString();
    }
}