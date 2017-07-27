package de.vta.vtalauncher.logic.download.loader;

import java.io.File;

import de.vta.vtalauncher.exception.vtaDownloadException;
import de.vta.vtalauncher.logic.download.DownloadHelper;
import de.vta.vtalauncher.logic.download.DownloadUrls;
import de.vta.vtalauncher.logic.minecraft.MinecraftPathImpl;
import de.vta.vtalauncher.logic.version.MinecraftVersion;

public class JarDownloader implements Downloader {

    private MinecraftVersion mVersion;
    private MinecraftPathImpl mMinecraftPath;

    /**
     * @param version
     * @param mcPath
     */
    public JarDownloader(MinecraftVersion version, MinecraftPathImpl mcPath) {
        this.mVersion = version;
        this.mMinecraftPath = mcPath;
    }

    @Override
    public void download() throws VtaDownloadException {
        String version = this.mVersion.getVersion();
        String path = mMinecraftPath.getMinecraftJarPath();

        new File(path).mkdirs();

        DownloadHelper.downloadFileToDiskWithoutCheck(DownloadUrls.URL_VERSION + version + "/" + version
                + ".jar", path, version + ".jar");
    }

}
