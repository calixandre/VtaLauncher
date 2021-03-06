package de.vta.vtalauncher.logic.download.loader;

import java.io.File;
import java.io.IOException;

import de.vta.vtalauncher.exception.VtaDownloadException;
import de.vta.vtalauncher.logic.download.DownloadHelper;
import de.vta.vtalauncher.logic.download.DownloadUrls;
import de.vta.vtalauncher.logic.json.JSONReader;
import de.vta.vtalauncher.logic.minecraft.MinecraftPath;
import de.vta.vtalauncher.logic.resources.LibEntry;
import de.vta.vtalauncher.logic.resources.Version;
import de.vta.vtalauncher.logic.version.MinecraftVersion;
import de.vta.vtalauncher.logic.vm.DownloadVM;
import de.vta.util.OSHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LibraryDownloader implements Downloader {
    private static final Logger LOGGER = LogManager.getLogger(LibraryDownloader.class);
    private MinecraftPath mMinecraftPath;
    private MinecraftVersion mCurrentVersion;
    private DownloadVM mDownloadVM;

    public LibraryDownloader(MinecraftVersion version, MinecraftPath path, DownloadVM downloadVM) {
        this.mCurrentVersion = version;
        this.mMinecraftPath = path;
        this.mDownloadVM = downloadVM;
    }

    @Override
    public void download() throws VtaDownloadException {
        Version vers = JSONReader.readJsonFileFromSelectedVersion(mMinecraftPath.getMinecraftJarPath() + mCurrentVersion.getVersion() + ".json");

        String libDir = mMinecraftPath.getLibraryDir();

        for (LibEntry entry : vers.getLibraries().get()) {

            if (entry.isExternal()) {
                LOGGER.info("Not downloading library because external: " + entry.getName());
                continue;
            }

            if (entry.isNeeded()) {

                if (entry.getFileName().contains("$")) {
                    if (OSHelper.getOSArch().equals("32")) {
                        entry.setFilename(entry.getFileName().replace("${arch}", "32"));
                    } else if (OSHelper.getOSArch().equals("64")) {
                        entry.setFilename(entry.getFileName().replace("${arch}", "64"));
                    } else {
                        LOGGER.error("LibraryDownloader-> No OS arch detected!");
                    }
                }

                String replaced = entry.getPath().replace(File.separator, "/");
                String adress = DownloadUrls.URL_LIBRARIES + replaced + "/" + entry.getFileName();

                LOGGER.info("File download started: " + entry.getFileName());

                mDownloadVM.updateDownloadFile(entry.getFileName());
                mDownloadVM.updateProgress(1);

                try {
                    DownloadHelper.downloadFileToDiskWithCheck(adress, libDir + entry.getPath(), entry.getFileName());
                } catch (VtaDownloadException e) {
                    LOGGER.error("Could not download: " + entry.getFileName(), e);
                    throw new VtaDownloadException("Download failed: " + entry.getFileName());
                }

                if (entry.isExtractable()) {

                    String file = libDir + entry.getPath() + File.separator + entry.getFileName();
                    String natives = mMinecraftPath.getMinecraftJarPath() + this.mCurrentVersion.getVersion() + "-natives-131231";

                    new File(natives).mkdirs();

                    try {
                        DownloadHelper.unpackJarFile(file, natives);
                    } catch (IOException e) {
                        LOGGER.error("Unpacking jar ( " + file + " ) failed", e);
                        throw new VtaDownloadException("Unpacking jar failed: " + file);
                    }
                }
            }
        }
    }

}
