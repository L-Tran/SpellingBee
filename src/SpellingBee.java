import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee by Logan Tran
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);
    }

    // Generate helper method
    public void makeWords(String word, String letters) {
        // Base case when the letters
        if (letters.isEmpty()) {
            words.add(word);
            return;
        }
        // Generate word if there is one
        if (!word.isEmpty()) {
            words.add(word);
        }
        // Add each permutation of letters to the current word
        for (int i = 0; i < letters.length(); i++) {
            makeWords(word + letters.charAt(i), letters.substring(0,i) + letters.substring(i + 1));
        }
    }


    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // call recursive method
        words = mergeSort(words);
    }

    public ArrayList<String> mergeSort (ArrayList<String> list) {
        // Base case when the list is one long
        if (list.size() == 1) {
            return list;
        }
        // Declare left and right lists
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        // Add to each list each side split by the middle
        int i = 0;
        while(i < list.size()/2) {
            list1.add(list.get(i));
            i++;
        }
        while(i < list.size()) {
            list2.add(list.get(i));
            i++;
        }
        // Divide the lists
        list1 = mergeSort(list1);
        list2 = mergeSort(list2);
        // Sort and merge
        return merge(list1, list2);
    }

    public ArrayList<String> merge(ArrayList<String> list1,ArrayList<String> list2) {
        // Counter for each string
        int i = 0;
        int j = 0;
        // Big String to add to
        ArrayList<String> list = new ArrayList<String>();
        // While the lists are not fully run through
        while(i < list1.size() && j < list2.size()) {
            // add left it is smaller and shift index
            if(list1.get(i).compareTo(list2.get(j)) <= 0) {
                list.add(list1.get(i));
                i++;
            }
            // add right it is smaller and shift index
            else{
                list.add(list2.get(j));
                j++;
            }
        }
        // Finish adding the rest of the lists
        while(i < list1.size()){
            list.add(list1.get(i));
            i++;
        }
        while(j < list2.size()){
            list.add(list2.get(j));
            j++;
        }
        return list;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // Check each word in words
        for(int i = 0; i < words.size(); i++){
            if (!contains(words.get(i), 0, DICTIONARY_SIZE)){
                words.remove(i);
                i--;
            }
        }
    }

    public boolean contains(String word, int low, int high) {
        int mid;
        // until word found
        while(true) {
            // Initialize mid depending on high and low
            mid = low + (high - low) / 2;
            // If word found;
            if(word.compareTo(DICTIONARY[mid]) == 0) {
                return true;
            }
            // If word is to the left shorten dict to left side
            if(word.compareTo(DICTIONARY[mid]) < 0) {
                high = mid - 1;
            }
            // If word is to the left shorten dict to right side
            if(word.compareTo(DICTIONARY[mid]) > 0) {
                low = mid + 1;
            }
            // If the word doesn't exist
            if (low > high) {
                return false;
            }
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
