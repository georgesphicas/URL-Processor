/*George Sphicas November 16, 2022*/
package com.usf.cs245.a2;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.SQLOutput;
import java.util.*;
import java.io.*;

public class TextAnalyzer {

    /**
     * first, a properties file is created, and each of the avoid words are
     * stored.
     * after the text is stored from the url of the website and split by spaces,
     * each value from the text is compared to the avoid words, and  only stores
     * the values in the contentWords linked list if they are allowed words.
     *
     * longest and shortest words as well as their respective counts are calculated
     * as the contentWords list is being created, comparing each String to the
     * current longest/shortest values until the list is finished
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Properties pf = new Properties();
        pf.put(0,"a");
        pf.put(1,"the");
        pf.put(2,"i");
        pf.put(3, "so");

        System.out.println("Please input a valid URL: ");
        Scanner in = new Scanner(System.in);
        String url = in.nextLine();
        String text = getWebpageContent(url);

        int letterCount = 0;

        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) != ' ') {
                letterCount++;
            }
        }

        String[] splitText = text.trim().split("\\s+");

        ArrayList<String> avoidWords = new ArrayList();
        for(int i = 0; i < pf.size(); i++) {
            avoidWords.add(pf.get(i).toString());
        }

        LinkedList<String> contentWords = new LinkedList<>();

        String longest = "";
        int longestCount = 0;
        String shortest = "placeholder";
        int shortestCount = 0;

        for (String s : splitText) {
            if(avoidWords.contains(s)) {
                contentWords = contentWords;
            }
            else {
                contentWords.add(s);
                if(s.equals(longest)) {
                    longestCount++;
                }
                if(s.equals(shortest)) {
                    shortestCount++;
                }
                if(s.length() > longest.length()) {
                    longest = s;
                    //longestCount++;
                }

                if(s.length() < shortest.length()) {
                    shortest = s;
                    //shortestCount++;
                }
            }
        }

        makeCommandQueue(contentWords, splitText, letterCount, longest, longestCount, shortest, shortestCount);

    }

    public static String getWebpageContent(String url) throws IOException {
        Connection conn = Jsoup.connect(url);
        Document doc = conn.get();
        return doc.body().text();
    }

    /**
     * taking the linkedlist of content words as input,this method
     * first creates a hashmap in order to maintain the words and their counts
     * if a word is known within the hashmap, the count is increased,
     * whereas if it is unknown, it is added to the known words. the
     *  count values are then compared and outputted
     *
     * @param contentWords
     */
    public static void displayTopTen(LinkedList<String> contentWords) {
        HashMap<String, Integer> wordCount = new HashMap<>();
        for(String contentWord : contentWords) {
            String word = contentWord.toLowerCase();
            if(wordCount.containsKey(word)) {
                wordCount.put(word, wordCount.get(word) + 1);
            }
            else {
                wordCount.put(word, 0);
            }
        }

        ArrayList<Map.Entry<String, Integer>> topTen = new ArrayList<>(wordCount.entrySet());
        Collections.sort(topTen, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return a.getValue().compareTo(b.getValue());
            }
        });

        System.out.print("Top 10 Words: ");
        for(int i = 0; i < 10; i++){
            if(i != 9) {
                System.out.print(topTen.get(topTen.size() - i - 1) + " | ");
            }
            else {
                System.out.print(topTen.get(topTen.size() - i - 1));
            }
        }
        System.out.println();
    }

    public static void displayLongest(String longest, int longestCount) {
        System.out.println("Longest: " + longest + ": 1");
    }

    public static void displayShortest(String shortest, int shortestCount) {
        System.out.println("Shortest: " + shortest + ": 1");
    }

    public static void displaySummary(String[] splitText, int letterCount) {
        System.out.print("Summary: Total Words: " + splitText.length + " | Total Letters: " + letterCount);
        System.out.println();
    }

    /**
     * this method first takes in the inputted text commands as one full String,
     * buffered by spaces, then splits the string into an array separated by spaces
     *
     * the commands are then added to a queue called "q" to be pulled from.
     *
     * the program then loops through the queue until it is empty,
     * using q.peek() to compare the head of the queue to the possible
     * allowed commands. once the corresponding command function is called,
     * the head of the queue is removed.
     */
    public static void makeCommandQueue(LinkedList<String> contentWords, String[] splitText, int letterCount, String longest, int longestCount, String shortest, int shortestCount) {
        System.out.println("Enter text commands:");
        Scanner in2 = new Scanner(System.in);
        String commands = in2.nextLine();
        String[] splitCommands = commands.trim().split("\\s+");

        Queue<String> q = new LinkedList<>();
        for(String s: splitCommands) {
            q.add(s);
        }

        while(!q.isEmpty()) {
            if(q.peek().equals("top10") || q.peek().equals("Top10")) {
                displayTopTen(contentWords);
                q.remove();
            }
            else if(q.peek().equals("Longest") || q.peek().equals("longest")) {
                displayLongest(longest, longestCount);
                q.remove();
            }
            else if(q.peek().equals("Shortest") || q.peek().equals("shortest")) {
                displayShortest(shortest, shortestCount);
                q.remove();
            }
            else if(q.peek().equals("Summary") || q.peek().equals("summary")) {
                displaySummary(splitText, letterCount);
                q.remove();
            }
            else {
                q.remove();
            }
        }
    }
}
