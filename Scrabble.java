import java.util.Random;

public class Scrabble {

    static final String WORDS_FILE = "dictionary.txt";
    static final int[] SCRABBLE_LETTER_VALUES = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    static int HAND_SIZE = 10;
    static int MAX_NUMBER_OF_WORDS = 100000;
    static String[] DICTIONARY = new String[MAX_NUMBER_OF_WORDS];
    static int NUM_OF_WORDS;

    public static void init() {
        In in = new In(WORDS_FILE);
        System.out.println("Loading word list from file...");
        NUM_OF_WORDS = 0;
        while (!in.isEmpty()) {
            DICTIONARY[NUM_OF_WORDS++] = in.readString().toLowerCase();
        }
        System.out.println(NUM_OF_WORDS + " words loaded.");
    }
// Checks if the given word is present in the dictionary.
public static boolean isWordInDictionary(String word) {
    for (String dictionaryWord : DICTIONARY) {
        if (word.equals(dictionaryWord)) {
            return true;
        }
    }
    return false;
}

// Returns the Scrabble score of the given word.
// Adds 50 points if the word length equals the hand size.
// Adds 1000 points if the word contains the sequence "runi".
public static int wordScore(String word) {
    int totalScore = 0;

    // Sum the score based on letter values.
    for (int i = 0; i < word.length(); i++) {
        char letter = word.charAt(i);
        totalScore += SCRABBLE_LETTER_VALUES[letter - 'a'];
    }

    // Multiply the score by the word's length.
    totalScore *= word.length();

    // Add bonus points if the word uses all letters in the hand.
    if (word.length() == HAND_SIZE) {
        totalScore += 50;
    }

    // Add bonus points if the word contains "runi".
    if (MyString.subsetOf("runi", word)) {
        totalScore += 1000;
    }

    return totalScore;
}

// Creates a random hand with length (HAND_SIZE - 2) and inserts 'a' and 'e' at random positions.
public static String createHand() {
    String hand = MyString.randomStringOfLetters(HAND_SIZE - 2);
    hand = MyString.insertRandomly('a', hand);
    hand = MyString.insertRandomly('e', hand);
    return hand;
}

// Runs a single hand in a Scrabble game. Prompts the user for valid words or to end the hand.
public static void playHand(String hand) {
    int score = 0;

    // Input reader for user input.
    In inputReader = new In();

    while (!hand.isEmpty()) {
        System.out.println("Current Hand: " + MyString.spacedString(hand));
        System.out.println("Enter a word, or '.' to finish this hand:");

        String userInput = inputReader.readString();

        if (".".equals(userInput)) {
            break;
        }

        if (!userInput.isEmpty()) {
            if (MyString.subsetOf(userInput, hand)) {
                if (!isWordInDictionary(userInput)) {
                    System.out.println("Word not found in the dictionary. Try again.");
                } else {
                    int points = wordScore(userInput);
                    score += points;
                    System.out.println("%s earned %d points. Total score: %d points%n", userInput, points, score);
                    hand = MyString.remove(hand, userInput);
                }
            } else {
                System.out.println("Invalid word. Try again.");
            }
        }
    }

    // Display the final score when the hand ends.
    if (hand.isEmpty()) {
        System.out.printf("Ran out of letters. Total score: %d points%n", score);
    } else {
        System.out.println("End of hand. Total score: %d points%n", score);
		
    }
}

// Plays a Scrabble game by allowing the user to deal a new hand or end the game.
public static void playGame() {
    // Initialize the dictionary.

    // Input reader for user input.
    In inputReader = new In();

    while (true) {
       // System.out.println("Enter 'n' to deal a new hand, or 'e' to end the game:");

        String userInput = inputReader.readString();

        if ("n".equals(userInput)) {
            playHand(createHand());
        } else if ("e".equals(userInput)) {
            break;
        } else {
            System.out.println("Invalid input. Please enter 'n' or 'e'.");
        }
    }
}


    public static void main(String[] args) {
        testBuildingTheDictionary();
        testScrabbleScore();
        testCreateHands();
        testPlayHands();
        playGame();
    }

    public static void testBuildingTheDictionary() {
        init();
        for (int i = 0; i < 5; i++) {
            System.out.println(DICTIONARY[i]);
        }
        System.out.println(isWordInDictionary("mango"));
    }

    public static void testScrabbleScore() {
        System.out.println(wordScore("bee"));
        System.out.println(wordScore("babe"));
        System.out.println(wordScore("friendship"));
        System.out.println(wordScore("runi"));
    }

    public static void testCreateHands() {
        System.out.println(createHand());
        System.out.println(createHand());
        System.out.println(createHand());
    }

    public static void testPlayHands() {
        init();
        playHand("ocostrza");
        playHand("arbffip");
        playHand("aretiin");
    }
}
