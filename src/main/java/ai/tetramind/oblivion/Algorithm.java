package ai.tetramind.oblivion;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Algorithm {

    private final String key;

    public Algorithm(@NotNull String key) {
        this.key = key;
    }

    public abstract byte @NotNull [] encrypt(byte @NotNull [] data, byte @NotNull [] password);

    public abstract byte @NotNull [] decrypt(byte @NotNull [] data, byte @NotNull [] password);

    @Override
    public String toString() {
        return key;
    }

    @Override
    public final boolean equals(Object obj) {

        if (this == obj) return true;

        if (!(obj instanceof Algorithm algorithm)) return false;

        return Objects.equals(key, algorithm.key);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(key);
    }

    public @NotNull String getKey() {
        return key;
    }
}
