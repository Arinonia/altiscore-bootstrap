package fr.arinonia.bootstrap.updater.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;

public class DownloadUtils {
    @FunctionalInterface
    public interface ProgressCallback {
        void onProgress(double progress);
    }

    public static void downloadFile(final String url, final Path destination, final long expectedSize,
                                    final String expectedSha1, final ProgressCallback callback) throws IOException {
        Files.createDirectories(destination.getParent());
        final Path tempFile = destination.getParent().resolve(destination.getFileName() + ".temp");

        final URL fileUrl = new URL(url);
        final URLConnection connection = fileUrl.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        long totalSize = expectedSize > 0 ? expectedSize : connection.getContentLengthLong();

        try (final InputStream in = new BufferedInputStream(connection.getInputStream());
             final OutputStream out = new BufferedOutputStream(Files.newOutputStream(tempFile))) {

            byte[] buffer = new byte[8192];
            long downloaded = 0;
            int count;

            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
                downloaded += count;

                if (totalSize > 0 && callback != null) {
                    callback.onProgress((double) downloaded / totalSize);
                }
            }
        }

        if (expectedSha1 != null && !expectedSha1.isEmpty()) {
            final String downloadedSha1 = calculateSha1(tempFile);
            if (!downloadedSha1.equalsIgnoreCase(expectedSha1)) {
                Files.deleteIfExists(tempFile);
                throw new IOException("SHA-1 verification failed for " + destination.getFileName());
            }
        }

        Files.move(tempFile, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public static String calculateSha1(final Path file) throws IOException {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(Files.readAllBytes(file));

            final byte[] hash = digest.digest();
            final StringBuilder hexString = new StringBuilder();

            for (final byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (final Exception e) {
            throw new IOException("Failed to calculate SHA-1", e);
        }
    }

    public static boolean verifyFile(final Path file, final String expectedSha1, final long expectedSize) {
        try {
            if (!Files.exists(file)) return false;

            if (expectedSize > 0 && Files.size(file) != expectedSize) {
                return false;
            }

            if (expectedSha1 != null && !expectedSha1.isEmpty()) {
                final String currentSha1 = calculateSha1(file);
                return currentSha1.equalsIgnoreCase(expectedSha1);
            }

            return true;
        } catch (final IOException e) {
            return false;
        }
    }
}
