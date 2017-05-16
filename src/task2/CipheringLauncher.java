package task2;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.nio.file.Paths;



public class CipheringLauncher {
    @Option(name = "-c", aliases = "-d", metaVar = "key", required = true, usage = "Coding/Decoding")
    private String key;
    @Option(name = "-o", metaVar = "outFileName", usage = "Output file name")
    private String outputFileName;
    @Argument(metaVar = "inFileName", required = true, usage = "Input file name")
    private String inputFileName;

    public static void main(String[] args) {
        new CipheringLauncher().launch(args);
    }

    private void launch(String[] args) throws IllegalArgumentException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            //проверить ключ
            if (getKey() == null)
                throw new IllegalArgumentException("Encryption key not set");
            if (getKey().length() % 2 != 0)
                throw new IllegalArgumentException("Encryption key length invalid");
            File inFile = new File(inputFileName);
            if (!inFile.exists())
                throw new IllegalArgumentException("Input file doesn't exist");

            File outFile = outputFileName == null
                    ? Paths.get(inFile.getParent(), "out_" + inFile.getName()).toFile()
                    : new File(outputFileName);

            int keyLength = getKey().length() / 2;
            byte[] keyBuffer = new byte[keyLength];
            for (int i = 0; i < keyLength; ++i) {
                keyBuffer[i] = (byte)Integer.parseInt(getKey().substring(i * 2, i * 2 + 2), 16);
            }

            try(OutputStream outputStream = new FileOutputStream(outFile)) {
                try (FileInputStream inputStream = new FileInputStream(inFile)) {
                    int current = inputStream.read();
                    int keyIndex = 0;
                    while (current != -1) {
                        outputStream.write(current ^ keyBuffer[keyIndex++]);
                        keyIndex %= keyLength;
                        current = inputStream.read();
                    }
                }
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("ciphxor [-c key] [-d key] inputname.txt[-o outputname.txt]");
            parser.printUsage(System.err);
            return;
        } catch (IOException fileException) {
            System.err.println("File excteption " + fileException);
        }
    }

    public String getKey() {
        return this.key;
    }

    public String getOutFileName() {
        return this.outputFileName;
    }

    public String getInFileName() {
        return this.inputFileName;
    }
}
