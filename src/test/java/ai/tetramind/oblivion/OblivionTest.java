package ai.tetramind.oblivion;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class OblivionTest {

    private static final Random RANDOM = new SecureRandom();

    private static final int KEY_NUMBER = 3;
    private static final int FILE_NUMBER = 5;

    public static @NotNull byte[] loadFile(String name) {

        byte[] result = null;

        try (var input = new BufferedInputStream(new FileInputStream("src" + File.separator + "test" + File.separator + "resources" + File.separator + name))) {
            result = input.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(result);

        return result;
    }

    @Test
    void testSimpleEncryption() {

        var oblivion = Oblivion.getInstance();

        var key = loadFile("key0.txt");

        for (var i = 0; i <= KEY_NUMBER; i++) {

            var inputData = loadFile("file" + i + ".txt");

            var encrypted = oblivion.encrypt(inputData, key);

            assertNotEquals(new String(inputData), new String(encrypted));
        }
    }

    @Test
    void testEncryptionDecryption() {

        var oblivion = Oblivion.getInstance();

        for (var i = 0; i <= KEY_NUMBER; i++) {

            var key = loadFile("key" + i + ".txt");

            for (var j = 0; j <= FILE_NUMBER; j++) {

                var inputData = loadFile("file" + j + ".txt");

                var encrypted = oblivion.encrypt(inputData, key);

                assertNotEquals(new String(inputData), new String(encrypted));

                var decrypted = oblivion.decrypt(encrypted, key);

                assertEquals(new String(inputData), new String(decrypted));
            }
        }
    }

    @Test
    void testRandomEncryptDecrypt() {

        var oblivion = Oblivion.getInstance();

        var data = new byte[RANDOM.nextInt(Short.MAX_VALUE * 2) + 1];

        RANDOM.nextBytes(data);

        var key = new byte[RANDOM.nextInt(Short.MAX_VALUE) + 1];

        RANDOM.nextBytes(key);

        var encrypted = oblivion.encrypt(data, key);

        assertNotEquals(new String(data), new String(encrypted));

        var decrypted = oblivion.decrypt(encrypted, key);

        assertEquals(new String(data), new String(decrypted));
    }
}