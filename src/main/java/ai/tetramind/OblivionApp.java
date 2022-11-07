package ai.tetramind;


import ai.tetramind.oblivion.Oblivion;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class OblivionApp {
    private static final String ENCRYPT_MODE = "encrypt";
    private static final String DECRYPT_MODE = "decrypt";
    private static final int ARGS_LENGTH = 4;
    private static final int MODE_ARG_ID = 0;

    private static final int INPUT_FILE_ID = 1;
    private static final int KEY_FILE_ID = 2;
    private static final int OUTPUT_FILE_ID = 3;

    private OblivionApp() {
    }

    private static void usage() {
        System.err.println("java -jar oblivion.jar <" + ENCRYPT_MODE + "/" + DECRYPT_MODE + "> <input file> <key file> <output file>");
        System.exit(-1);
    }

    public static void main(String[] args) {

        try {
            if (args.length != ARGS_LENGTH) {
                usage();
            }

            var mode = args[MODE_ARG_ID];
            var inputFile = new File(args[INPUT_FILE_ID]);
            var outputFile = new File(args[OUTPUT_FILE_ID]);
            var keyFile = new File(args[KEY_FILE_ID]);

            var inputData = loadDataFromFile(inputFile);
            var keyData = loadDataFromFile(keyFile);
            var outputData = new ArrayList<Byte>();

            switch (mode) {
                case ENCRYPT_MODE -> encryptMode(inputData, keyData, outputData);
                case DECRYPT_MODE -> decryptMode(inputData, keyData, outputData);
                default -> usage();
            }

            writeDataInFile(outputData, outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeDataInFile(@NotNull List<Byte> data, @NotNull File file) throws IOException {

        try (var output = new BufferedOutputStream(new FileOutputStream(file))) {
            for (var element : data) {
                var b = (byte) element;
                output.write(b);
            }
        }
    }

    private static @NotNull byte[] loadDataFromFile(@NotNull File file) throws IOException {

        byte[] result = null;

        try (var inputStream = new BufferedInputStream(new FileInputStream(file))) {
            result = inputStream.readAllBytes();
        }

        assert result != null;

        return result;
    }

    private static void decryptMode(@NotNull byte[] inputData, @NotNull byte[] keyData, @NotNull List<Byte> outputData) {

        var oblivion = Oblivion.getInstance();

        var bytes = oblivion.decrypt(inputData, keyData);

        for (var b : bytes) {
            outputData.add(b);
        }
    }

    private static void encryptMode(@NotNull byte[] inputData, @NotNull byte[] keyData, @NotNull List<Byte> outputData) {

        var oblivion = Oblivion.getInstance();

        var bytes = oblivion.encrypt(inputData, keyData);

        for (var b : bytes) {
            outputData.add(b);
        }
    }
}
