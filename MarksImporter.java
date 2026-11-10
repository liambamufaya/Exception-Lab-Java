
import java.io.*;
import java.util.*;
import java.util.logging.*;

// Custom exception
class DuplicateStudentException extends Exception {
    public DuplicateStudentException(String message) {
        super(message);
    }
}

public class MarksImporter {

    private static final Logger logger =
            Logger.getLogger(MarksImporter.class.getName());

    public static void importMarks(String filename) throws IOException {

        Set<String> seen = new HashSet<>();

        // Try-with-resources automatically closes the reader,
        // even if an exception occurs.
        try (BufferedReader br =
                     new BufferedReader(new FileReader(filename))) {

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                try {
                    // TODO 1: Check exactly two fields
                    if (parts.length != 2) {
                        throw new IllegalArgumentException(
                                "Expected exactly 2 fields");
                    }

                    String studentId = parts[0].trim();
                    String markStr = parts[1].trim();

                    if (studentId.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Student ID cannot be empty");
                    }

                    // TODO 2: Parse and validate mark
                    int mark = Integer.parseInt(markStr);

                    if (mark < 0 || mark > 100) {
                        throw new IllegalArgumentException(
                                "Mark out of range (0..100): " + mark);
                    }

                    // TODO 3: Detect duplicate student IDs
                    if (!seen.add(studentId)) {
                        throw new DuplicateStudentException(
                                "Duplicate student ID: " + studentId);
                    }

                    // TODO 4: Display accepted record
                    System.out.println(
                            "Accepted Record -> Student ID: "
                                    + studentId + ", Mark: " + mark);

                } catch (IllegalArgumentException |
                         DuplicateStudentException e) {

                    // Log technical details for debugging
                    logger.log(Level.WARNING,
                            "Skipping invalid record. Exception type: "
                                    + e.getClass().getSimpleName()
                                    + ", Technical cause: " + e.getMessage());

                    // Friendly message for ordinary users
                    System.out.println(
                            "Invalid record skipped: " + e.getMessage());
                }
            }
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        try {
            importMarks("marks.txt");

            System.out.println("Import completed successfully.");

        } catch (FileNotFoundException e) {

            logger.log(Level.SEVERE,
                    "File not found: " + e.getMessage(), e);

            System.out.println(
                    "Marks file not found; check the file name.");

        } catch (IOException e) {

            logger.log(Level.SEVERE,
                    "File reading error: " + e.getMessage(), e);

            System.out.println(
                    "An error occurred while reading the marks file.");

        } finally {

            System.out.println("Import attempt finished");
        }
    }
}
