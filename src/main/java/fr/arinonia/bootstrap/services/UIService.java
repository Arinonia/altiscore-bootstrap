package fr.arinonia.bootstrap.services;

import fr.arinonia.bootstrap.ui.BootstrapFrame;
import fr.arinonia.bootstrap.ui.BootstrapPanel;
import fr.arinonia.bootstrap.ui.ErrorDialog;
import fr.flowarg.azuljavadownloader.Callback;

import javax.swing.*;

public class UIService {
    private BootstrapFrame frame;
    private BootstrapPanel panel;
    private boolean isDisplayed = false;

    public void display() {
        SwingUtilities.invokeLater(() -> {
            if (!this.isDisplayed) {
                this.frame = new BootstrapFrame();
                this.panel = new BootstrapPanel();
                this.frame.setContentPane(this.panel);
                this.frame.setVisible(true);
                this.isDisplayed = true;
            }
        });
    }

    public void updateProgress(final int progress, final String status) {
        SwingUtilities.invokeLater(() -> {
            if (this.panel != null) {
                this.panel.getRoundedProgressBar().setValue(progress);
                this.panel.updateStatusLabel(status);
            }
        });
    }

    public BootstrapFrame getFrame() {
        return this.frame;
    }

    public void showError(final String title, final String message, final Throwable error) {
        SwingUtilities.invokeLater(() -> {
            if (this.frame != null) {
                ErrorDialog.showError(this.frame, title, message, error);
            }
        });
    }

    public void dispose() {
        SwingUtilities.invokeLater(() -> {
            if (this.frame != null) {
                this.frame.dispose();
                this.isDisplayed = false;
            }
        });
    }

    public interface ProgressCallback {
        void onProgress(final int progress, final String status);
        void onProgress(final Callback.Step step);
        void onError(final String title, final String message, final Throwable error);

    }

    public ProgressCallback createProgressCallback(final String statusPrefix) {
        return new ProgressCallback() {
            @Override
            public void onProgress(final int progress, final String status) {
                updateProgress(progress, statusPrefix + " " + progress + "%");
            }

            @Override
            public void onProgress(final Callback.Step step) {
                switch (step) {
                    case QUERYING:
                        updateProgress(0, statusPrefix + " Recherche...");
                        break;
                    case DOWNLOADING:
                        updateProgress(25, statusPrefix + " Téléchargement...");
                        break;
                    case EXTRACTING:
                        updateProgress(65, statusPrefix + " Extraction...");
                        break;
                    case DONE:
                        updateProgress(100, statusPrefix + " Terminé !");
                        break;
                }
            }

            @Override
            public void onError(final String title, final String message, final Throwable error) {
                showError(title, message, error);
            }
        };
    }
}
