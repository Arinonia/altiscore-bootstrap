package fr.arinonia.bootstrap.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaValidator {
    private static final Set<String> REQUIRED_DIRECTORIES = new HashSet<>(Arrays.asList(
            "bin",
            "conf",
            "include",
            "lib",
            "legal"
    ));

    private static final Set<String> REQUIRED_JAVAFX_MODULES = new HashSet<>(Arrays.asList(
            "javafx.base",
            "javafx.controls",
            "javafx.fxml",
            "javafx.graphics",
            "javafx.media",
            "javafx.swing",
            "javafx.web"
    ));

    public static class ValidationResult {
        private final boolean valid;
        private final String version;
        private final String errorMessage;
        private final boolean hasJavaFX;

        public ValidationResult(final boolean valid, final String version, final String errorMessage, final boolean hasJavaFX) {
            this.valid = valid;
            this.version = version;
            this.errorMessage = errorMessage;
            this.hasJavaFX = hasJavaFX;
        }

        public boolean isValid() {
            return this.valid;
        }
        public String getVersion() {
            return this.version;
        }
        public String getErrorMessage() {
            return this.errorMessage;
        }
        public boolean hasJavaFX() {
            return this.hasJavaFX;
        }
    }

    public static ValidationResult validateJavaInstallation(final Path javaPath) {
        if (!Files.exists(javaPath)) {
            return new ValidationResult(false, null, "Le dossier Java n'existe pas", false);
        }

        boolean hasAllDirectories = true;
        final StringBuilder missingDirs = new StringBuilder();
        for (final String dir : REQUIRED_DIRECTORIES) {
            if (!Files.exists(javaPath.resolve(dir))) {
                hasAllDirectories = false;
                missingDirs.append(dir).append(", ");
            }
        }
        if (!hasAllDirectories) {
            return new ValidationResult(false, null,
                    "Dossiers manquants: " + missingDirs.substring(0, missingDirs.length() - 2),
                    false);
        }

        final Path javaExe = javaPath.resolve("bin").resolve(getJavaExecutableName());
        if (!Files.exists(javaExe) || !Files.isExecutable(javaExe)) {
            return new ValidationResult(false, null,
                    "L'exécutable Java n'existe pas ou n'est pas exécutable", false);
        }

        try {
            final ProcessBuilder versionCheck = new ProcessBuilder(
                    javaExe.toString(),
                    "-version"
            );
            Process process = versionCheck.start();

            if (!process.waitFor(10, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return new ValidationResult(false, null,
                        "Timeout lors de la vérification de la version Java", false);
            }

            final String version = extractJavaVersion(process);
            System.out.println("Java version extracted: " + version);
            if (version == null) {
                return new ValidationResult(false, null,
                        "Impossible de déterminer la version Java", false);
            }

            final boolean hasJavaFX = checkJavaFX(javaExe);

            final ProcessBuilder moduleCheck = new ProcessBuilder(
                    javaExe.toString(),
                    "--list-modules"
            );
            process = moduleCheck.start();

            if (!process.waitFor(10, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return new ValidationResult(false, version,
                        "Timeout lors de la vérification des modules", hasJavaFX);
            }

            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            final StringBuilder modules = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                modules.append(line).append("\n");
            }

            return new ValidationResult(true, version, null, hasJavaFX);

        } catch (final IOException | InterruptedException e) {
            return new ValidationResult(false, null,
                    "Erreur lors de la vérification: " + e.getMessage(), false);
        }
    }

    private static String getJavaExecutableName() {
        return System.getProperty("os.name").toLowerCase().contains("win") ?
                "javaw.exe" : "java";
    }

    private static String extractJavaVersion(final Process process) {
        try {
            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            String line;
            final Pattern pattern = Pattern.compile("version \"([^\"]*)\"");

            while ((line = reader.readLine()) != null) {
                final Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean checkJavaFX(Path javaExe) {
        try {
            ProcessBuilder javafxCheck = new ProcessBuilder(
                    javaExe.toString(),
                    "--list-modules"
            );
            Process process = javafxCheck.start();

            if (!process.waitFor(10, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return false;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            Set<String> foundModules = new HashSet<>();

            while ((line = reader.readLine()) != null) {
                for (String module : REQUIRED_JAVAFX_MODULES) {
                    if (line.startsWith(module + "@")) {
                        foundModules.add(module);
                    }
                }
            }

            return foundModules.containsAll(REQUIRED_JAVAFX_MODULES);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}