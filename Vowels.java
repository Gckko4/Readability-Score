package readability;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vowels {
    protected static ArrayList<String> words;
    protected static ArrayList<Integer> syllables = new ArrayList<>();

    protected static String textOriginal;
    protected double wordCount;
    protected double sentencesCount;
    protected double charCount;
    protected int syllablesCount;
    protected int polySyllablesCount;
    protected double charAverage;
    private double sentencesAverage;
    String[] originalWordArray;
    public Vowels(String text){
        originalWordArray = text.split(" ");
        String[] SeparateWords = originalWordArray.clone();
        ArrayList<String> wordCoded = new ArrayList<>();

        int vowels = 0;

        for (String word : SeparateWords){

            //? Vowels == "@"
            //? Consonants == "~"
            //* replace all symbols
            word = word.replaceAll("[^A-Za-z0-9]+","");

            //* if "e" at the end, deleted, to count easier the vowels
            if (word.matches("\\w+e\\b") && word.length() > 1){
                word = word.substring(0,word.length()-1);
            }

            //* replace all vowels
            word = word.replaceAll("[aeiouAEIOU]","@");

            //* replace y only if it's at the end
            word = word.replaceAll("y\\b|(?<=c)y","@");

            //* replace all consonants
            word = word.replaceAll("[bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ\\d]", "~");

            //* replace double vowels
            word = word.replaceAll("@(?=@)","");

            //* add to a list
            wordCoded.add(word);


        }
        words = wordCoded;
        textOriginal = text;

        charCount();
        sentencesCount();
        wordsCount();
        SyllablesCount();
        polySyllableCount();
        charAverage(wordCount, charCount);
        sentencesAverage(sentencesCount, wordCount);
    }


    //* calculates the Automated Readability Index
    private double ari(double words, double sentences, double characters){
        return  4.71 * (characters/words) + 0.5 * (words/sentences) - 21.43;
    }
    public double getAri(){
        return ari(wordCount, sentencesCount, charCount);
    }




    //* calculates the Flesch–Kincaid readability tests
    private double fkr(double words,double sentences,double syllables){
        return 0.39 * (words/sentences) + 11.8 * (syllables/words) - 15.59;
    }
    public double getFkr(){
        return fkr(wordCount, sentencesCount,syllablesCount);
    }



    //* Calculates the Simple Measure of Gobbledygook index (SMOG)
    private double smog(){
        return 1.043 * Math.sqrt(polySyllablesCount*(30/sentencesCount))+3.1291;
    }
    public double getSmog(){
        return smog();
    }



    //* Calculates the Coleman–Liau index (colL)
    private double colL(){
        return (0.0588 * charAverage) - (0.296 * sentencesAverage) - 15.8;
    }
    public double getColL(){
        return colL();
    }


    //? Down here are all the counters and its getters
    private void SyllablesCount(){
        int vowels = 0;
        int result = 0;

        //* loop for each word in the text
        for (String wordToDecode : words) {


            //* patter & matcher for the vowels and if it doesnt find any = +1
            Pattern p = Pattern.compile("@");
            Matcher m = p.matcher(wordToDecode);

            while (m.find()) {
                vowels++;
            }

            if (!wordToDecode.contains("@")) {
                vowels++;
            }
            //* added to an Arraylist the amount of vowels for each word
            syllables.add(vowels);
            vowels = 0;
        }
        //* loop the list to get syllables
        for (Integer s : syllables){
            result += s;
        }

        syllablesCount = result;
    }
    public int getSyllables(){
        return syllablesCount;
    }

    private void polySyllableCount(){
        int poly = 0;
        //* loop the total of syllables and if > 2 = polysyllable
        for (Integer i : syllables){
            if (i > 2){
                poly++;
            }
        }
        polySyllablesCount = poly;
    }

    public int getPolySyllableCount(){
        return polySyllablesCount;
    }


    private void charCount(){
        //* deletes every space or tab and then split it into each character.
        //* then count the size of the new array
        charCount =  textOriginal.replaceAll(" |\n|\t", "").split("").length;
    }
    public double getCharCount(){
        return charCount;
    }

    private void sentencesCount(){

        //* same as with the char count
        sentencesCount = textOriginal.split("[.?!]").length;
    }
    public double getSentencesCount(){
        return sentencesCount;
    }

    private void wordsCount(){
        //* same as with the char count
        wordCount = textOriginal.split(" ").length;
    }
    public double getWordCount(){
        return wordCount;
    }

    //* Calculates the average number of sentences per 100 words
    private void sentencesAverage(double sentencesCount, double wordCount){
        sentencesAverage = sentencesCount / wordCount * 100;
    }
    public double getSentencesAverage(){
        return sentencesAverage;
    }

    //* Calculates the average number of char every 100 words
    private void charAverage(double wordCount, double charCount){
        charAverage = charCount / wordCount * 100;
    }
    public double getCharAverage(){
        return charAverage;
    }

    public String getLevel(double score){
        double scoreRounded = Math.round(score);
        int type = (int) ( scoreRounded+ 1); //* +1 to round up
        int age = 0;
        switch (type){
            case 1 -> age = 6;
            case 2 -> age = 7;
            case 3 -> age = 8;
            case 4 -> age = 9;
            case 5 -> age = 10;
            case 6 -> age = 11;
            case 7 -> age = 12;
            case 8 -> age = 13;
            case 9 -> age = 14;
            case 10 -> age = 15;
            case 11 -> age = 16;
            case 12-> age = 17;
            case 13 -> age = 18;
            default -> {
                if (type > 13){
                    age = 22;
                } else {
                    age = 5;
                }
            }

        }
        return "(about " + age + "-year-olds).";
    }
    public double getAgeAverage(Vowels vowels){
        double ariAv = Double.parseDouble(getLevel(vowels.getAri()).replaceAll("\\D",""));
        double fkrAv = Double.parseDouble(getLevel(vowels.getFkr()).replaceAll("\\D",""));
        double smogAv = Double.parseDouble(getLevel(vowels.getSmog()).replaceAll("\\D",""));
        double colAv = Double.parseDouble(getLevel(vowels.getColL()).replaceAll("\\D",""));

        return (ariAv + fkrAv + smogAv + colAv) / 4;
    }
}
