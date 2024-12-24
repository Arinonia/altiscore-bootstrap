package fr.arinonia.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Don't pay attention to this class, it's just used to retrieve information
 * that needs to be put in the JSON for the updater.
 */
public class RandomShitUsefullForTestingOmgThisNameIsTooooooLongLol {

    public static void main(String[] args) {
        final String startPath = args.length > 0 ? args[0] : ".";
        File directory = new File(startPath).getAbsoluteFile();
        if (!directory.exists()) {
            System.err.println("Directory does not exist: " + directory.getAbsolutePath());
            return;
        }
        if (!directory.isDirectory()) {
            System.err.println("Path is not a directory: " + directory.getAbsolutePath());
            return;
        }
        if (!directory.canRead()) {
            System.err.println("Cannot read directory: " + directory.getAbsolutePath());
            return;
        }
        analyzeDirectory(directory, directory.getAbsolutePath());
    }

    private static void analyzeDirectory(final File directory, final String basePath) {
        System.out.println("Analyzing directory: " + directory.getAbsolutePath());
        final File[] files = directory.listFiles();
        if (files == null) {
            System.err.println("Failed to list files in directory: " + directory.getAbsolutePath());
            return;
        }
        System.out.println("Found " + files.length + " files/directories");

        for (final File file : files) {
            if (file.isDirectory()) {
                analyzeDirectory(file, basePath);
            } else {
                analyzeFile(file, basePath);
            }
        }
    }

    private static void analyzeFile(final File file, final String basePath) {
        try {
            final String absolutePath = file.getAbsolutePath();
            String relativePath;

            try {
                Path base = Paths.get(basePath).toAbsolutePath().normalize();
                Path filePath = Paths.get(absolutePath).toAbsolutePath().normalize();
                relativePath = base.relativize(filePath).toString();
            } catch (Exception e) {
                relativePath = file.getPath();
            }

            final long size = file.length();
            final String sha1 = calculateSHA1(file);

            System.out.printf("File: %s%n", file.getName());
            System.out.printf("  Absolute path: %s%n", absolutePath);
            System.out.printf("  Relative path: %s%n", relativePath);
            System.out.printf("  Size: %d bytes%n", size);
            System.out.printf("  SHA-1: %s%n%n", sha1);

        } catch (final IOException | NoSuchAlgorithmException e) {
            System.err.printf("Error while analyzing file %s: %s%n",
                    file.getName(), e.getMessage());
        }
    }

    private static String calculateSHA1(final File file) throws IOException, NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-1");
        final FileInputStream fis = new FileInputStream(file);
        final byte[] byteArray = new byte[1024];
        int bytesCount;

        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        fis.close();

        final byte[] bytes = digest.digest();
        final StringBuilder sb = new StringBuilder();

        for (final byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}