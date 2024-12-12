
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
		
		In in = new In(WORDS_FILE);
        System.out.println("Loading word list from file...");
        NUM_OF_WORDS = 0;
		while (!in.isEmpty()) {
			
			DICTIONARY[NUM_OF_WORDS++] = in.readString().toLowerCase();
		}
        System.out.println(NUM_OF_WORDS + " words loaded.");
	}

	


public static void testBuildingTheDictionary() {
    init();
    for (int i = 0; i < 5; i++) {
        System.out.println(DICTIONARY[i]);
    }
    System.out.println(isWordInDictionary("mango")); 
    System.out.println(isWordInDictionary("cat"));   
    System.out.println(isWordInDictionary("xyz123")); 
    System.out.println(isWordInDictionary("qwxz"));  

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
		System.out.println("Testing playHand():");
		System.out.println("Loading word list from file...");
		init(); 
		System.out.println(NUM_OF_WORDS + " words loaded.");
	
		int totalScore = 0;
	
		String[] mockInput = {"train", "."};
		int inputIndex = 0;
	
		while (true) {
			System.out.println("Enter word, or '.' to indicate that you're finished:");
			String word = mockInput[inputIndex];
			inputIndex = (inputIndex + 1) % mockInput.length;
	
			if (word.equals(".")) {
				break;
			}
	
			if (isWordInDictionary(word) && subsetOf(word, hand)) {
				int wordScore = wordScore(word);
				totalScore += wordScore;
				System.out.println(word + " earned " + wordScore + " points. Total: " + totalScore + " points");
			} else {	
				System.out.println(word + " is not a valid word.");	
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

	

	public static boolean isWordInDictionary(String word) {
    if (!word.matches("[a-z]+")) {
        return false; 
    }

    for (int i = 0; i < NUM_OF_WORDS; i++) {
        if (DICTIONARY[i].equals(word)) {
            return true; 
        }
    }

    return false;  
}

	
	public static boolean getExpectedIsWordInDictionaryResult(String word) {
		switch (word) {
			case "": return false; 
			case "CAT": return false; 
			case "xyz123": return false; 
			case "qwxz": return false; 
			default: return false;
		}
	}
	
	

	public static void main(String[] args) {
		testBuildingTheDictionary();  
		testScrabbleScore();    
		testCreateHands();  
	    testPlayHands();
		playGame();
	}


	


	public static void testScrabbleScore() {
		System.out.println("Testing wordScore():");
		String[] words = {"cat", "dog", "quiz", "friendship", "running", "", "a"};
	
		for (String word : words) {
			int score = wordScore(word);
			System.out.println("'" + word + "' -> " + score + " (expected: " + getExpectedScore(word) + ")");
		}
	}
	
	public static int getExpectedScore(String word) {
		switch (word) {
			case "cat": return 15;
			case "dog": return 15;
			case "quiz": return 88;
			case "friendship": return 240;
			case "running": return 1056;
			case "": return 0;
			case "a": return 1;
			default: return -1; 
		}
	}
	
	
	public static void testCreateHands() {
		System.out.println("Testing createHand():");
	
		for (int i = 1; i <= 3; i++) {
			String hand = createHand();
			System.out.println("Hand " + i + ":");
			System.out.println("Length: " + hand.length() + " (expected: 10)");
			System.out.println("Contains 'a': " + hand.contains("a") + " (expected: true)");
			System.out.println("Contains 'e': " + hand.contains("e") + " (expected: true)");
			System.out.println("All lowercase letters: " + hand.equals(hand.toLowerCase()) + " (expected: true)");
			System.out.println("Valid Scrabble letters: " + isValidScrabbleHand(hand) + " (expected: true)");
			System.out.println();
		}
	}
	
	public static boolean isValidScrabbleHand(String hand) {
		for (char c : hand.toCharArray()) {
			if (!Character.isLowerCase(c) || c < 'a' || c > 'z') {
				return false;
			}
		}
		return true;
	}
	
	public static void testPlayHands() {
		init();
		playHand("ocostrza");
		playHand("arbffip");
		playHand("aretiin");
	}
}
