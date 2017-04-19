package task2;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;

import java.io.File;


//Класс, который разбирает командную строку
public class CipheringLauncher {
    @Option(name = "-c", aliases = "-d", metaVar = "flag", required = true, usage = "Шифрация/Дешифрация")
    private String flag;
    @Option(name = "-o", metaVar = "outFileName", usage = "Имя выходного файла")
    private String outputFileName;
    @Argument(metaVar = "inFileName", required = true, usage = "Имя входного файла")
    private String inputFileName;

    public static void main(String[] args) {
        new CipheringLauncher().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            //проверить ключ
            // throw new IllegalArgumentException("Неверный ключ");
            if (!(new File(inputFileName).exists()))
                throw new IllegalArgumentException("Input file doesn't exist");
            if (outputFileName == null) {
                String s[] = inputFileName.split("\\.");
                outputFileName= s[0]+"_out.txt";
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("ciphxor [-c key] [-d key] inputname.txt[-o outputname.txt]");
            parser.printUsage(System.err);
            return;

        }
    }

    public String getFlag() {
        return this.flag;
    }

    public String getOutFileName() {
        return this.outputFileName;
    }

    public String getInFileName() {
        return this.inputFileName;
    }
}
