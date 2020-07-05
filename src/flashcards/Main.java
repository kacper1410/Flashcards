package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Flashcards flashcards = new Flashcards(scanner);
        String importFileName = "";
        String exportFileName = "";
        String fileName = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) {
                importFileName = args[i + 1];
            } else if (args[i].equals("-export")) {
                exportFileName = args[i + 1];
            }
        }
        if (!importFileName.equals("")) {
            flashcards.importCards(importFileName);
            System.out.println();
            Flashcards.log.add("");
        }
        boolean exit = false;
        do {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            Flashcards.log.add("Input the action (add, remove, import, export, ask, exit):");
            String input = scanner.nextLine();
            Flashcards.log.add(input);
            switch (input) {
                case "add":
                    flashcards.add();
                    break;
                case "remove":
                    flashcards.remove();
                    break;
                case "export":
                    System.out.println("File name:");
                    Flashcards.log.add("File name:");
                    fileName = scanner.nextLine();
                    Flashcards.log.add(fileName);
                    flashcards.exportCards(fileName);
                    break;
                case "import":
                    System.out.println("File name:");
                    Flashcards.log.add("File name:");
                    fileName = scanner.nextLine();
                    Flashcards.log.add(fileName);
                    flashcards.importCards(fileName);
                    break;
                case "ask":
                    flashcards.ask();
                    break;
                case "log":
                    flashcards.log();
                    break;
                case "hardest card":
                    flashcards.hardestCard();
                    break;
                case "reset stats":
                    flashcards.resetStats();
                    break;
                case "exit":
                    exit = true;
                    System.out.println("Bye bye!");
                    Flashcards.log.add("Bye bye!");
                    if (!exportFileName.equals("")) {
                        flashcards.exportCards(exportFileName);
                    }
                    break;
                default:
                    System.out.println("Unknown action.");
                    Flashcards.log.add("Unknown action.");
            }
            System.out.println();
            Flashcards.log.add("");
        } while (!exit);

    }
}

class Flashcards {
    Map<String, String> flashcards;
    Map<String, Integer> mistakes;
    static ArrayList<String> log = new ArrayList<>();
    Scanner scanner;

    public Flashcards(Scanner scanner) {
        this.flashcards = new HashMap<>();
        this.mistakes = new HashMap<>();
        this.scanner = scanner;
    }

    public void add() {
        System.out.println("The card:");
        Flashcards.log.add("The card:");
        String term = scanner.nextLine();
        Flashcards.log.add(term);
        if (flashcards.containsKey(term)) {
            System.out.printf("The card \"%s\" already exists.\n", term);
            Flashcards.log.add("The card \"" + term + "\" already exists.");
            return;
        }

        System.out.println("The definition of the card:");
        Flashcards.log.add("The definition of the card:");
        String def = scanner.nextLine();
        Flashcards.log.add(def);
        if (flashcards.containsValue(def)) {
            System.out.printf("The definition \"%s\" already exists.\n", def);
            Flashcards.log.add("The definition \"" + def + "\" already exists.");
            return;
        }

        flashcards.put(term, def);
        mistakes.put(term, 0);
        System.out.printf("The pair (\"%s\":\"%s\") has been added.\n", term, def);
        Flashcards.log.add("The pair (\"" + term + "\":\"" + def + "\") has been added.\n");

    }

    public void ask() {
        System.out.println("How many times to ask?");
        Flashcards.log.add("How many times to ask?");
        int n = Integer.parseInt(scanner.nextLine());
        Flashcards.log.add(String.valueOf(n));
        String[] terms = new String[flashcards.keySet().size()];
        flashcards.keySet().toArray(terms);
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            String term = terms[random.nextInt(terms.length)];

            System.out.printf("Print the definition of \"%s\":\n", term);
            Flashcards.log.add("Print the definition of \"" + term + "\":");
            String input = scanner.nextLine();
            Flashcards.log.add(input);

            if (input.equals(flashcards.get(term))) {
                System.out.println("Correct answer.");
                Flashcards.log.add("Correct answer.");
            } else if (flashcards.containsValue(input)){
                String otherTerm  = "";
                for (String s : flashcards.keySet()) {
                    if (flashcards.get(s).equals(input)) {
                        otherTerm = s;
                    }
                }
                System.out.printf("Wrong answer. The correct one is \"%s\",", flashcards.get(term));
                System.out.printf("you've just written the definition of \"%s\".\n", otherTerm);
                Flashcards.log.add("Wrong answer. The correct one is \"" + flashcards.get(term) +
                        "\",you've just written the definition of \"" + otherTerm + "\".");
                mistakes.replace(term, mistakes.get(term) + 1);
            } else {
                System.out.printf("Wrong answer. The correct one is \"%s\".\n", flashcards.get(term));
                Flashcards.log.add("Wrong answer. The correct one is \"" + flashcards.get(term) + "\".");
                mistakes.replace(term, mistakes.get(term) + 1);
            }

        }
    }

    public void remove() {
        System.out.println("The card:");
        Flashcards.log.add("The card:");
        String term = scanner.nextLine();
        if (flashcards.containsKey(term)) {
            flashcards.remove(term);
            mistakes.remove(term);
            System.out.println("The card has been removed.");
            Flashcards.log.add("The card has been removed.");
        } else {
            System.out.printf("Can't remove \"%s\": there is no such card.", term);
            Flashcards.log.add("Can't remove \"" + term + "\": there is no such card.");
        }
    }

    public void importCards(String fileName) {
        File file = new File(fileName);
        String term;
        String def;
        int mistake;
        int count = 0;
        try (Scanner fileScanner = new Scanner(file)){
            while (fileScanner.hasNext()) {
                term = fileScanner.nextLine();
                def = fileScanner.nextLine();
                mistake = Integer.parseInt(fileScanner.nextLine());
                flashcards.put(term, def);
                mistakes.put(term, mistake);
                count++;
            }
            System.out.printf("%d cards have been loaded.", count);
            Flashcards.log.add(count + " cards have been loaded.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            Flashcards.log.add("File not found.");
        }
    }

    public void exportCards(String fileName) {
        File file = new File(fileName);
        int count = 0;
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String s : flashcards.keySet()) {
                printWriter.println(s);
                printWriter.println(flashcards.get(s));
                printWriter.println(mistakes.get(s));
                count++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            Flashcards.log.add("File not found.");
        }
        System.out.printf("%d cards have been saved", count);
        Flashcards.log.add(count + " cards have been saved.");

    }

    public void hardestCard() {
        int max = 0;
        ArrayList<String> cards = new ArrayList<>();
        for (String s : mistakes.keySet()) {
            if (mistakes.get(s) > max) {
                cards.clear();
                max = mistakes.get(s);
                cards.add(s);
            } else if (mistakes.get(s) == max) {
                cards.add(s);
            }
        }
        if (max == 0) {
            System.out.println("There are no cards with errors.");
            Flashcards.log.add("There are no cards with errors.");
        } else if (cards.size() == 1) {
            String mess = "The hardest card is \"" + cards.get(0) + "\". You have " +
                    mistakes.get(cards.get(0)) + " errors answering it.";
            System.out.println(mess);
            Flashcards.log.add(mess);
        } else {
            StringBuilder mess = new StringBuilder("The hardest cards are ");
            for (int i = 0; i < cards.size() - 1; i++) {
                mess.append("\"");
                mess.append(cards.get(i));
                mess.append("\", ");
            }
            mess.append("\"").append(cards.get(cards.size() - 1)).append("\". ");
            mess.append("You have ").append(max).append(" errors answering them.");
            System.out.println(mess);
            Flashcards.log.add(mess.toString());
        }
    }

    public void log() {
        System.out.println("File name:");
        Flashcards.log.add("File name:");
        String name = scanner.nextLine();
        Flashcards.log.add(name);
        File file = new File(name);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String s : log) {
                printWriter.println(s);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            Flashcards.log.add("File not found.");
        }
        System.out.println("The log has been saved");
        Flashcards.log.add("The log has been saved");
    }

    public void resetStats() {
        for (String s : mistakes.keySet()) {
            mistakes.replace(s, 0);
        }
        System.out.println("Card statistics has been reset.");
        Flashcards.log.add("Card statistics has been reset.");
    }
}