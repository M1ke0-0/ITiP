package lab6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TopWords {
    public static void main(String[] args) {
        String filePath = "src/lab6/text.txt";
        File file = new File(filePath);

        Scanner scanner = null;
        try {
            scanner = new Scanner(file, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Map<String, Integer> wordCount = new HashMap<>();

        while (scanner.hasNext()) {
            String word = scanner.next().toLowerCase().replaceAll("[^а-яa-z]", "");
            if (!word.isEmpty()) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }

        scanner.close();

        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordCount.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        list.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        System.out.println("Топ-10 самых частых слов:");
        int count = 0;
        for (Map.Entry<String, Integer> entry : list) {
            if (count >= 10) break;
            System.out.println(entry.getKey() + ": " + entry.getValue());
            count++;
        }
    }
}
