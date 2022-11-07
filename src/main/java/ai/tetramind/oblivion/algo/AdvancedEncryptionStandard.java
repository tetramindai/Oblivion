package ai.tetramind.oblivion.algo;

import ai.tetramind.oblivion.Algorithm;
import org.jetbrains.annotations.NotNull;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public final class AdvancedEncryptionStandard extends Algorithm {
    private static final String ALGO_KEY = "AES";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5PADDING";
    private static final byte[] IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int MAX_ITERATION = 65535;
    private static final int KEY_LENGTH = 256;

    private static final IvParameterSpec IV_SPEC = new IvParameterSpec(IV);

    public AdvancedEncryptionStandard() {
        super(ALGO_KEY);
    }


    private static SecretKeySpec buildSecretKey(byte @NotNull [] password) {

        SecretKeySpec result = null;

        var encoder = Base64.getEncoder();

        var keyBase64 = new String(encoder.encode(password));

        try {

            var factory = SecretKeyFactory.getInstance(
                    SECRET_KEY_ALGORITHM);

            var spec = new PBEKeySpec(keyBase64.toCharArray(), keyBase64.getBytes(), MAX_ITERATION, KEY_LENGTH);

            var secretKey = factory.generateSecret(spec);

            var encoded = secretKey.getEncoded();

            result = new SecretKeySpec(encoded, ALGO_KEY);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public byte @NotNull [] encrypt(byte @NotNull [] data, byte @NotNull [] password) {

        byte[] result = data;

        var secretKey = buildSecretKey(password);

        try {

            var cipher = Cipher.getInstance(TRANSFORMATION);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV_SPEC);

            result = cipher.doFinal(data);

        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public byte @NotNull [] decrypt(byte @NotNull [] data, byte @NotNull [] password) {

        byte[] result = data;

        var secretKey = buildSecretKey(password);

        try {

            var cipher = Cipher.getInstance(TRANSFORMATION);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, IV_SPEC);

            result = cipher.doFinal(data);

        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return result;
    }
}
