package ai.tetramind.oblivion.algo;

import ai.tetramind.oblivion.Algorithm;
import org.jetbrains.annotations.NotNull;

public final class OneTimePad extends Algorithm {
    private static final String ALGO_KEY = "AES";

    public OneTimePad() {
        super(ALGO_KEY);
    }

    @Override
    public byte @NotNull [] encrypt(byte @NotNull [] data, byte @NotNull [] password) {

        var result = new byte[data.length];

        for (var i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ password[i % password.length]);
        }

        return result;
    }

    @Override
    public byte @NotNull [] decrypt(byte @NotNull [] data, byte @NotNull [] password) {

        var result = new byte[data.length];

        for (var i = 0; i < data.length; i++) {
            result[i] = (byte) (password[i % password.length] ^ data[i]);
        }

        return result;
    }
}
