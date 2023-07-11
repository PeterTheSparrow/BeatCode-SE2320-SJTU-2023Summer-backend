package team.beatcode.auth.utils;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class TokenUtils {
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();
    public static byte[] generate() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static String BytesToString(byte[] bytes) {
        return encoder.encodeToString(bytes);
    }

    public static byte[] StringToBytes(String str) {
        try {
            return str == null ? null : decoder.decode(str);
        }
        catch (IllegalArgumentException e) {
            // 格式错误
            return null;
        }
    }
}
