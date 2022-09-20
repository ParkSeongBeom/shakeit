package kr.co.mythings.shackit.core.util;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class CryptoUtil {

    public static String makeSHA3_256String(String text) {
        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest256();
        byte[] digest = digestSHA3.digest(text.getBytes(StandardCharsets.UTF_8));
        return Hex.toHexString(digest);
    }

    public static String makePasswordSalt() {
        return makeRandomString(64);
    }

    public static String makeRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    stringBuilder.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1:
                    // A-Z
                    stringBuilder.append((char) (rnd.nextInt(26) + 65));
                    break;
                case 2:
                    // 0-9
                    stringBuilder.append((rnd.nextInt(10)));
                    break;
            }
        }

        return stringBuilder.toString();
    }
}
