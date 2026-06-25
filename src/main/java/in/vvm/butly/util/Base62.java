package in.vvm.butly.util;

public class Base62 {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int BASE = 62;

    public static String encode(long value) {
        if (value == 0)
            return "0";

        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            int rem = (int) (value % BASE);
            sb.append(ALPHABET.charAt(rem));
            value /= BASE;
        }

        return sb.reverse().toString();
    }
}
