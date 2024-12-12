/*
 * RUNI version of the Scrabble game.
 */
public class Scrabble {

	// Dictionary file for this Scrabble game
	static final String WORDS_FILE = "dictionary.txt";

	// The "Scrabble value" of each letter in the English alphabet.
	// 'a' is worth 1 point, 'b' is worth 3 points, ..., z is worth 10 points.
	static final int[] SCRABBLE_LETTER_VALUES = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3,
												  1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };

	// Number of random letters dealt at each round of this Scrabble game
	static int HAND_SIZE = 10;

	// Maximum number of possible words in this Scrabble game
	static int MAX_NUMBER_OF_WORDS = 100000;

    // The dictionary array (will contain the words from the dictionary file)
	static String[] DICTIONARY = new String[MAX_NUMBER_OF_WORDS];

	// Actual number of words in the dictionary (set by the init function, below)
	static int NUM_OF_WORDS;

	// Populates the DICTIONARY array with the lowercase version of all the words read
	// from the WORDS_FILE, and sets NUM_OF_WORDS to the number of words read from the file.
	public static void init() {
		// Declares the variable in to refer to an object of type In, and initializes it to represent
		// the stream of characters coming from the given file. Used for reading words from the file.  
		In in = new In(WORDS_FILE);
        System.out.println("Loading word list from file...");
        NUM_OF_WORDS = 0;
		while (!in.isEmpty()) {
			// Reads the next "token" from the file. A token is defined as a string of 
			// non-whitespace characters. Whitespace is either space characters, or  
			// end-of-line characters.
			DICTIONARY[NUM_OF_WORDS++] = in.readString().toLowerCase();
		}
        System.out.println(NUM_OF_WORDS + " words loaded.");
	}

	public static boolean isWordInDictionary(String word) {
    // Check if the word consists only of alphabetic characters
    if (!word.matches("[a-zA-Z]+")) {
        return false;
    }

    word = word.toLowerCase();

    for (int i = 0; i < NUM_OF_WORDS; i++) {
        if (DICTIONARY[i].equals(word)) {
            return true;
        }
    }
    return false;
}

public static void testBuildingTheDictionary() {
    init();
    // Prints a few words
    for (int i = 0; i < 5; i++) {
        System.out.println(DICTIONARY[i]);
    }
    System.out.println(isWordInDictionary("mango")); // should return true or false based on dictionary
    System.out.println(isWordInDictionary("cat"));   // should return true or false based on dictionary
    System.out.println(isWordInDictionary("xyz123")); // should return false
    System.out.println(isWordInDictionary("qwxz"));   // should return true or false based on dictionary
}


    public static int wordScore(String word) {
        int score = 0;
        for (char c : word.toCharArray()) {
            score += SCRABBLE_LETTER_VALUES[c - 'a'];
        }
        score *= word.length();
        if (word.length() == HAND_SIZE) score += 50;
        if (word.contains("r") && word.contains("u") && word.contains("n") && word.contains("i")) {
            score += 1000;
        }
        return score;
    }

    public static String createHand() {
        String hand = MyString.randomStringOfLetters(HAND_SIZE - 2);
        hand = MyString.insertRandomly('a', hand);
        hand = MyString.insertRandomly('e', hand);
        return hand;
    }
    private static boolean subsetOf(String word, String hand) {
		int[] handCount = new int[26];
		int[] wordCount = new int[26];
	
		// Count letters in the hand
		for (char c : hand.toLowerCase().toCharArray()) {
			if (c >= 'a' && c <= 'z') { 
				handCount[c - 'a']++;
			}
		}
		for (char c : word.toLowerCase().toCharArray()) {
			if (c >= 'a' && c <= 'z') { 
				wordCount[c - 'a']++;
			} else {
				return false;
			}
		}
		for (int i = 0; i < 26; i++) {
			if (wordCount[i] > handCount[i]) {
				return false;
			}
		}
		return true;
	}
	

	public static void playHand(String hand) {
		int totalScore = 0;
		In in = new In();
	
		while (!hand.isEmpty()) {
			System.out.println("Current Hand: " + MyString.spacedString(hand));
			System.out.println("Enter a word, or '.' to finish playing this hand:");
			String word = in.readString().trim();
	
			if (word.equals(".")) break;
	
			if (!subsetOf(word, hand)) {
				System.out.println("Invalid word. Try again.");
			} else if (!isWordInDictionary(word)) {
				System.out.println("No such word in the dictionary. Try again.");
			} else {
				int score = wordScore(word);
				totalScore += score;
				System.out.println(word + " earned " + score + " points. Score: " + totalScore + " points\n");
				hand = MyString.remove(hand, word);
			}
		}
	
		System.out.println("End of hand. Total score: " + totalScore + " points");
	}
	
    public static void playGame() {
        init();
        In in = new In();
        String hand = "";
        while (true) {
            System.out.println("Enter n to deal a new hand, or e to end the game:");
            String choice = in.readString();
            if (choice.equals("n")) {
                hand = createHand();
                playHand(hand);
            } else if (choice.equals("e")) {
                break;
            } else {
                System.out.println("Invalid command.");
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
		// Prints a few words
		for (int i = 0; i < 5; i++) {
			System.out.println(DICTIONARY[i]);
		}
		System.out.println(isWordInDictionary("mango"));
	}
	
	public static void testScrabbleScore() {
		System.out.println(wordScore("bee"));	
		System.out.println(wordScore("babe"));
		System.out.println(wordScore("friendship"));
		System.out.println(wordScore("running"));
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
