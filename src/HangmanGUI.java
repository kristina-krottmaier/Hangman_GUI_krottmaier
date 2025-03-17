import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class HangmanGUI {
    private JPanel mainPanel;
    private JLabel wordLabel;
    private JLabel mistakeLabel;
    private JLabel historyLabel;
    private JTextField inputField;
    private JButton newGameButton;
    private JLabel imageLabel;
    private String wordToGuess;
    private StringBuilder currentGuess;
    private int mistakes;
    private final int maxMistakes = 9;
    private ArrayList<Character> guessedLetters = new ArrayList<>();
    private ArrayList<String> wordList;
    private static final String BILDER_PFAD = "./hangman/hangman";


    public HangmanGUI() {
        loadWords();
        startNewGame();

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!inputField.getText().isEmpty()) {
                    handleGuess(inputField.getText().toUpperCase().charAt(0));
                    inputField.setText("");
                }
            }
        });
    }

    private void loadWords() {
        wordList = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("words.txt"))) {
            while (scanner.hasNextLine()) {
                wordList.add(scanner.nextLine().toUpperCase());
            }
        } catch (FileNotFoundException e) {
            wordList.add("JAVA");
            wordList.add("PYTHON");
            wordList.add("COMPUTER");
            wordList.add("LAPTOP");
            wordList.add("KABEL");
            wordList.add("HANGMAN");
            wordList.add("GUESS");
        }
    }

    private void startNewGame() {
        Random rand = new Random();
        wordToGuess = wordList.get(rand.nextInt(wordList.size()));
        currentGuess = new StringBuilder("_".repeat(wordToGuess.length()));
        mistakes = 0;
        guessedLetters.clear();
        updateGUI();
    }

    private void handleGuess(char letter) {
        if (!Character.isLetter(letter) || guessedLetters.contains(letter)) {
            return;
        }
        guessedLetters.add(letter);

        if (wordToGuess.contains(String.valueOf(letter))) {
            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == letter) {
                    currentGuess.setCharAt(i, letter);
                }
            }
        } else {
            mistakes++;
        }
        updateGUI();
    }

    private void updateGUI() {
        wordLabel.setText("Wort: " + formatWordDisplay(currentGuess.toString()));
        mistakeLabel.setText("Fehlversuche: " + mistakes);
        historyLabel.setText("Historie: " + guessedLetters);

        // Correct path to images
        String imagePath = BILDER_PFAD + (int)(mistakes+1) + ".png";
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            imageLabel.setIcon(new ImageIcon(imagePath));
        } else {
            System.out.println("Image not found: " + imagePath);
        }

        // Game over check
        if (mistakes >= maxMistakes) {
            JOptionPane.showMessageDialog(null, "Game Over! Das Wort war: " + wordToGuess);
            startNewGame();
        }
        // Win condition check
        else if (!currentGuess.toString().contains("_")) {
            JOptionPane.showMessageDialog(null, "SUPIIII! Du hast das Wort erraten!");
            startNewGame();
        }
    }

    private String formatWordDisplay(String word) {
        return word.replace("", " ").trim();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hangman");
        HangmanGUI gui = new HangmanGUI();

        frame.setContentPane(gui.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
