package de.vta.vtalauncher.logic.download.loader;

import de.vta.vtalauncher.exception.VtaDownloadException;

public interface Downloader {
    /**
     * Downloads the specified file.
     *
     * @throws VtaDownloadException if downloading the file fails
     */
    void download() throws VtaDownloadException;
}
