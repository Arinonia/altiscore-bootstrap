package fr.arinonia.bootstrap.utils;

import fr.flowarg.azuljavadownloader.AzulJavaOS;

public class OSDetector {
    // should I call this methode only once in the Bootstrap class ?
    public static AzulJavaOS getCurrentPlatform() {
        final String osName = System.getProperty("os.name").toLowerCase();
        final String osArch = System.getProperty("os.arch").toLowerCase();

        System.out.printf("Detecting OS - Name: %s, Architecture: %s%n", osName, osArch);

        if (osName.contains("win")) {
            System.out.println("Detected Windows operating system");
            return AzulJavaOS.WINDOWS;
        }

        if (osName.contains("mac") || osName.contains("darwin")) {
            System.out.println("Detected macOS operating system");
            return AzulJavaOS.MACOS;
        }

        if (osName.contains("linux")) {
            if (isMusl()) {
                System.out.println("Detected Linux with musl libc");
                return AzulJavaOS.LINUX_MUSL;
            } else {
                System.out.println("Detected Linux with glibc");
                return AzulJavaOS.LINUX_GLIBC;
            }
        }

        if (osName.contains("sunos") || osName.contains("solaris")) {
            System.out.println("Detected Solaris operating system");
            return AzulJavaOS.SOLARIS;
        }

        if (osName.contains("aix")) {
            System.out.println("Detected AIX operating system");
            return AzulJavaOS.AIX;
        }

        if (osName.contains("qnx")) {
            System.out.println("Detected QNX operating system");
            return AzulJavaOS.QNX;
        }

        System.out.println("Unknown operating system detected, defaulting to standard Linux");
        return AzulJavaOS.LINUX;
    }

    private static boolean isMusl() {
        try {
            final Process process = new ProcessBuilder("ldd", "--version")
                    .redirectErrorStream(true)
                    .start();

            int exitCode = process.waitFor();
            return exitCode != 0;
        } catch (final Exception e) {
            System.out.printf("Error detecting libc type, assuming glibc: %s", e.getMessage());
            return false;
        }
    }

    /**
     * Check if the current system is a Unix system
     * @return true if the current system is a Unix system
     */
    public static boolean isUnixSystem() {
        final String os = System.getProperty("os.name").toLowerCase();
        return os.contains("nix") || os.contains("nux") || os.contains("aix");
    }
}