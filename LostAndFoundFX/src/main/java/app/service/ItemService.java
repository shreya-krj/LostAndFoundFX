package app.service;

import app.model.Item;
import app.model.LostItem;
import app.model.FoundItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ItemService {

    private static final List<Item> items = new ArrayList<>();
    private static final String FILE_NAME = "lost_found_data.txt";
    private static boolean loaded = false; // ⭐ important

    /* =========================
       LOAD DATA ONCE
    ========================= */
    public static void init() {
        if (!loaded) {
            loadFromFile();
            loaded = true;
        }
    }

    /* =========================
       ADD ITEM
    ========================= */
    public static void addItem(Item item) {
        init();
        items.add(item);
        saveToFile();
    }

    public static List<Item> getAllItems() {
        init();
        return items;
    }

    /* =========================
       SAVE DATA TO FILE
    ========================= */
    private static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Item item : items) {
                bw.write(
                        item.getType() + ";" +
                                item.getTitle() + ";" +
                                item.getCategory() + ";" +
                                item.getLocation() + ";" +
                                item.getDescription() + ";" +
                                item.getImagePath() + ";" +
                                item.getReporterName() + ";" +
                                item.getPhone() + ";" +
                                item.getEmail() + ";" +
                                item.getStatus()
                );
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* =========================
       LOAD DATA FROM FILE
    ========================= */
    private static void loadFromFile() {

        items.clear(); // ⭐ prevent duplicates

        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");

                Item item;
                if (data[0].equals("LOST")) {
                    item = new LostItem(
                            data[1], data[2], data[3], data[4], data[5],
                            data[6], data[7], data[8]
                    );
                } else {
                    item = new FoundItem(
                            data[1], data[2], data[3], data[4], data[5],
                            data[6], data[7], data[8]
                    );
                }

                item.setStatus(data[9]);
                items.add(item);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* =========================
       AUTO-MATCH LOGIC
    ========================= */
    public static List<String> findPossibleMatches() {

        init();
        List<String> matches = new ArrayList<>();

        for (Item lost : items) {

            if (!lost.getType().equalsIgnoreCase("LOST") ||
                    lost.getStatus().equalsIgnoreCase("RETURNED")) {
                continue;
            }

            for (Item found : items) {

                if (!found.getType().equalsIgnoreCase("FOUND") ||
                        found.getStatus().equalsIgnoreCase("RETURNED")) {
                    continue;
                }

                int score = 0;

                if (lost.getCategory() != null &&
                        found.getCategory() != null &&
                        lost.getCategory().equalsIgnoreCase(found.getCategory())) {
                    score += 3;
                }

                if (lost.getLocation() != null &&
                        found.getLocation() != null) {

                    String lostLoc = lost.getLocation().toLowerCase();
                    String foundLoc = found.getLocation().toLowerCase();

                    if (lostLoc.contains(foundLoc) ||
                            foundLoc.contains(lostLoc)) {
                        score += 2;
                    }
                }

                if (lost.getTitle() != null &&
                        found.getTitle() != null) {

                    String[] lostWords =
                            lost.getTitle().toLowerCase().split("\\s+");

                    for (String word : lostWords) {
                        if (found.getTitle()
                                .toLowerCase()
                                .contains(word)) {
                            score += 2;
                            break;
                        }
                    }
                }

                if (lost.getDescription() != null &&
                        found.getDescription() != null &&
                        found.getDescription()
                                .toLowerCase()
                                .contains(
                                        lost.getDescription().toLowerCase()
                                )) {
                    score += 1;
                }

                if (score >= 5) {
                    matches.add(
                            "Possible match (" + score + "): " +
                                    "LOST [" + lost.getTitle() + "] <-> FOUND [" +
                                    found.getTitle() + "]"
                    );
                }
            }
        }

        return matches;
    }

    /* =========================
       DELETE ITEM
    ========================= */
    public static void deleteItem(Item item) {
        init();
        items.remove(item);
        saveToFile();
    }
    /* =========================
   UPDATE ITEM STATUS
========================= */
    public static void markAsReturned(Item item) {
        init();
        item.setStatus("RETURNED");
        saveToFile();   // ⭐ THIS WAS MISSING
    }

}
