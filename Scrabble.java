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

    public static boolean isWordInDictionary(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        String input = word.toLowerCase();
        for (int i = 0; i < NUM_OF_WORDS; i++) {
            if (DICTIONARY[i].equals(input)) {
                return true;
            }
        }
        return false;
    }

    public static int wordScore(String word) {
    int totalScore = 0;

    for (int i = 0; i < word.length(); i++) {
        char letter = word.charAt(i);
        totalScore += SCRABBLE_LETTER_VALUES[letter - 'a'];
    }

    totalScore *= word.length();

    if (word.length() == HAND_SIZE) {
        totalScore += 50;
    }

    if (MyString.subsetOf("runi", word)) {
        totalScore += 1000;
    }

    return totalScore;
}


    public static String createHand() {
    String hand = "";

    hand = MyString.randomStringOfLetters(HAND_SIZE - 2);
    hand = MyString.insertRandomly('a', hand);
    hand = MyString.insertRandomly('e', hand);

    // Return the completed hand.
    return hand;
}

	
	private static String shuffleString(String str) {
		Random rand = new Random();
		char[] chars = str.toCharArray();
	
		for (int i = 0; i < chars.length; i++) {
			int randomIndex = rand.nextInt(chars.length);
			char temp = chars[i];
			chars[i] = chars[randomIndex];
			chars[randomIndex] = temp;
		}
	
		return new String(chars);
	}
	

    

    public static void playHand(String hand) {
    int handLength = hand.length();
    int totalScore = 0;
    In inputReader = new In();

    while (!hand.isEmpty()) {
        System.out.println("Current Hand: " + MyString.spacedString(hand));
        System.out.print("Enter a word, or '.' to finish playing this hand: ");

        String userInput = inputReader.readString();

        if (".".equals(userInput)) {
            break;
        }

        if (!userInput.isEmpty()) {
            if (MyString.subsetOf(userInput, hand)) {
                if (!isWordInDictionary(userInput)) {
                    System.out.println("Word not found in dictionary. Please try again.");
                } else {
                    int points = wordScore(userInput);
                    totalScore += points;
                    System.out.printf("%s earned %d points. Total Score: %d points%n", userInput, points, totalScore);
                    hand = MyString.remove(hand, userInput);
                }
            } else {
                System.out.println("Invalid word. Please try again.");
            }
        }
    }

    // Display the final score based on whether the user finished the letters or quit early.
    if (hand.isEmpty()) {
        System.out.printf("You ran out of letters! Total score: %d points%n", totalScore);
    } else {
        System.out.printf("Game ended. Total score: %d points%n", totalScore);
    }
}


    private static boolean canFormWordFromHand(String word, String hand) {
        StringBuilder handBuilder = new StringBuilder(hand);
        for (char c : word.toCharArray()) {
            int index = handBuilder.indexOf(String.valueOf(c));
            if (index == -1) {
                return false;
            }
            handBuilder.deleteCharAt(index);
        }
        return true;
    }

    private static String updateHand(String hand, String word) {
        StringBuilder handBuilder = new StringBuilder(hand);
        for (char c : word.toCharArray()) {
            int index = handBuilder.indexOf(String.valueOf(c));
            if (index != -1) {
                handBuilder.deleteCharAt(index);
            }
        }
        return handBuilder.toString();
    }

    public static void playGame() {
    init();

    In inputReader = new In();

    while (true) {
        System.out.println("Enter 'n' to deal a new hand, or 'e' to end the game:");

        String userInput = inputReader.readString();

        if ("n".equals(userInput)) {
            playHand(createHand());
        } else if (!"e".equals(userInput)) {
            System.out.println("Invalid input. Please enter 'n' or 'e'.");
        } else {
            break;
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
