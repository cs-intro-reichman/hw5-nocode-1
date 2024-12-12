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
        if (word == null || word.isEmpty()) {
            return 0;
        }
        int score = 0;
        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (letter >= 'a' && letter <= 'z') {
                score += SCRABBLE_LETTER_VALUES[letter - 'a'];
            }
        }
        if (word.length() == HAND_SIZE) {
            score += 50;
        }
        if (word.contains("runi")) {
            score += 1000;
        }
        return score;
    }

    public static String createHand() {
        Random rand = new Random();
        StringBuilder hand = new StringBuilder();

        hand.append('a').append('e');
        while (hand.length() < HAND_SIZE) {
            char randomLetter = (char) ('a' + rand.nextInt(26));
            hand.append(randomLetter);
        }

        return shuffleString(hand.toString());
    }

    private static String shuffleString(String str) {
        Random rand = new Random();
        StringBuilder shuffled = new StringBuilder(str);

        for (int i = 0; i < shuffled.length(); i++) {
            int j = rand.nextInt(shuffled.length());
            char temp = shuffled.charAt(i);
            shuffled.setCharAt(i, shuffled.charAt(j));
            shuffled.setCharAt(j, temp);
        }

        return shuffled.toString();
    }

    public static void playHand(String hand) {
        int score = 0;
        In in = new In();
        while (!hand.isEmpty()) {
            System.out.println("Current Hand: " + MyString.spacedString(hand));
            System.out.print("Enter a word, or '.' to finish playing this hand:");
            String input = in.readString();
            if (input.equals(".")) {
                break;
            }
            if (isWordInDictionary(input) && canFormWordFromHand(input, hand)) {
                int wordScore = wordScore(input);
                score += wordScore;
                System.out.println(input + " earned " + wordScore + " points. Score: " + score + " points\n");
                hand = updateHand(hand, input);
            } else {
                System.out.println("Invalid word. Try again.");
            }
        }
        System.out.println("End of hand. Total score: " + score + " points");
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
        In in = new In();

        while (true) {
            System.out.println("Enter n to deal a new hand, or e to end the game:");
            String input = in.readString();
            if (input.equals("n")) {
                String hand = createHand();
                playHand(hand);
            } else if (input.equals("e")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'n' or 'e'.");
            }
        }
        System.out.println("Game over!");
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
