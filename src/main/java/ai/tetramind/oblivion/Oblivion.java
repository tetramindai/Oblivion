package ai.tetramind.oblivion;

import ai.tetramind.oblivion.algo.AdvancedEncryptionStandard;
import ai.tetramind.oblivion.algo.OneTimePad;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class Oblivion {

    // https://docs.oracle.com/javase/8/docs/api/java/util/zip/Deflater.html

    private static final Oblivion INSTANCE = new Oblivion();
    private final Set<Algorithm> algorithms;

    private Oblivion() {
        algorithms = new TreeSet<>(Oblivion::compareAlgorithms);
        loadAlgorithms();
    }

    private Algorithm findAlgorithm(int id) {

        Algorithm result = null;

        for (var algorithm : algorithms) {
            if (id-- < 0) break;
            result = algorithm;
        }

        return result;
    }

    private static int compareAlgorithms(@NotNull Algorithm left, @NotNull Algorithm right) {

        var leftKey = left.getKey();
        var rightKey = right.getKey();

        return leftKey.compareTo(rightKey);
    }

    private void loadAlgorithms() {
        algorithms.add(new AdvancedEncryptionStandard());
        algorithms.add(new OneTimePad());
    }


    public byte @NotNull [] encrypt(byte @NotNull [] data, byte @NotNull [] password) {

        // data = compress(data);

        for (var i = 0; i < password.length; i++) {

            var id = (i + password[i]) % algorithms.size();

            var algorithm = findAlgorithm(id);

            assert algorithm != null;

            data = algorithm.encrypt(data, password);
        }

        return data;
    }

    public byte @NotNull [] decrypt(byte @NotNull [] data, byte @NotNull [] password) {

        for (var i = password.length - 1; i >= 0; i--) {

            var id = (i + password[i]) % algorithms.size();

            var algorithm = findAlgorithm(id);

            assert algorithm != null;

            data = algorithm.decrypt(data, password);
        }

        // data = uncompress(data);

        return data;
    }

    public static byte @NotNull [] uncompress(byte @NotNull [] data) {

        var decompresser = new Inflater();
        decompresser.setInput(data);
        byte[] result = new byte[100];

        try {
            int resultLength = decompresser.inflate(result);
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        decompresser.end();

        return new byte[0];
    }

    private static byte @NotNull [] compress(byte @NotNull [] data) {

        var buffer = new byte[data.length];
        Arrays.fill(buffer, (byte) 0);

        var deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();


        var length = deflater.deflate(buffer);
        deflater.end();

        var result = new byte[length];

        System.arraycopy(buffer, 0, result, 0, result.length);

        return result;
    }


    public static Oblivion getInstance() {
        return INSTANCE;
    }
}
