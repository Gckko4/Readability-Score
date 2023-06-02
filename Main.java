package readability;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String filePath;
        try {
             filePath = args[0];
        } catch (ArrayIndexOutOfBoundsException AOBe){
            filePath = "C:\\Users\\gpi\\Desktop\\prueba.txt";
        }

        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        Scanner scanner1 = new Scanner(System.in);
        String text = "";
        while (scanner.hasNext()){
            text = text.concat(scanner.nextLine());
        }

        final DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.CANADA));

        Vowels vowels = new Vowels(text);
        double ari = vowels.getAri();
        double fkr = vowels.getFkr();
        double smog = vowels.getSmog();
        double colL = vowels.getColL();

        double[] points = {
                Double.parseDouble(df.format(ari)),
                Double.parseDouble(df.format(fkr)),
                Double.parseDouble(df.format(smog)),
                Double.parseDouble(df.format(colL))
        };

        String[] indexes = {
                ("Automated Readability Index: " + points[0] + vowels.getLevel(ari)),
                ("Flesch–Kincaid readability tests: " + points[1] + vowels.getLevel(fkr)),
                ("Simple Measure of Gobbledygook: " +  points[2] + vowels.getLevel(smog)),
                ("Coleman–Liau index: " + points[3] + vowels.getLevel(colL) )
        };


        System.out.println("The text is: \n" + text);
        System.out.println();
        System.out.println("Words: " + (int) Math.ceil(vowels.getWordCount()));
        System.out.println("Sentences: " + (int) Math.ceil(vowels.getSentencesCount()));
        System.out.println("Characters: " + (int) Math.ceil(vowels.getCharCount()));
        System.out.println("Syllables: " + vowels.getSyllables());
        System.out.println("Polysyllables: " + vowels.getPolySyllableCount());


        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String indexMode = scanner1.nextLine().toUpperCase();
        System.out.println();
        while (indexMode.isEmpty()){
            System.out.println("Sorry, choose again the Score you want to calculate");
            indexMode = scanner1.nextLine().toUpperCase();
        }


        switch (indexMode){
            case "ARI":
                System.out.println(indexes[0]);
                break;
            case "FK":
                System.out.println(indexes[1]);
            case "SMOG":
                System.out.println(indexes[2]);
                break;
            case "CL":
                System.out.println(indexes[3]);
                break;
            case "ALL":
                for (String index : indexes) {
                    System.out.println(index.replaceAll("[\\[\\]]", ""));
                }
        }


        System.out.println();
        System.out.println("this should be understood in average by "
                + df.format(vowels.getAgeAverage(vowels)) + "-year-olds.");



        scanner.close();

    }





}
