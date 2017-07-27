package de.vta.vtalauncher.logic.download.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.vta.vtalauncher.exception.vtaDownloadException;
import de.vta.vtalauncher.logic.download.DownloadHelper;
import de.vta.vtalauncher.logic.download.DownloadUrls;
import de.vta.vtalauncher.logic.resources.Index;
import de.vta.vtalauncher.logic.resources.ResEntry;
import de.vta.vtalauncher.logic.version.MinecraftVersion;
import de.vta.vtalauncher.logic.vm.DownloadVM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RessourceDownloader implements Downloader {
    private static final Logger LOGGER = LogManager.getLogger(RessourceDownloader.class);
    private String mResDir, mResURL, mIndexesURL;
    private DownloadVM mAccess;
    private MinecraftVersion mVersion;
    private Index mIndex;
    private boolean mDownloadable, mStop;

    private ExecutorService executor;

    public RessourceDownloader(MinecraftVersion version, String resDir, DownloadVM access) {
        this.mResURL = DownloadUrls.URL_RESOURCES;
        this.mIndexesURL = DownloadUrls.URL_INDEXES;
        this.mResDir = resDir;
        this.mAccess = access;
        this.mVersion = version;

        int poolSize = Runtime.getRuntime().availableProcessors();

        if (poolSize <= 4) {
            poolSize *= 2;
        }

        LOGGER.debug("RessourceDownloader: PoolSize " + poolSize);

        this.executor = Executors.newFixedThreadPool(poolSize);

        downloadIndexFile();
    }

    private void downloadIndexFile() {
        String fs = File.separator;
        boolean force = DownloadHelper.getForce();

        DownloadHelper.setForce(true);
        String assets = "";

        if (mVersion.getVersionJson().hasAssets()) {
            assets = mVersion.getVersionJson().getAssets();
        } else {
            assets = "legacy";
        }

        try {
            DownloadHelper.downloadFileToDiskWithCheck(mIndexesURL + assets + ".json", mResDir + fs + "indexes" + fs, assets + ".json");
            mIndex = new Index(mResDir + fs + "indexes" + fs + assets + ".json");
            mDownloadable = true;
        } catch (vtaDownloadException e) {
            LOGGER.error("Could not download " + assets + ".json", e);
        }

        DownloadHelper.setForce(force);
    }

    @Override
    public void download() throws vtaDownloadException {
        ArrayList<String> files = new ArrayList<String>();
        ArrayList<Future<String>> futures = new ArrayList<Future<String>>();


        if (!mDownloadable) {
            throw new VtaDownloadException("Ressource not downloadable!");
        }

        for (final ResEntry res : mIndex.getRes()) {
            if (mStop) {
                break;
            }
            files.add(mResDir + res.getPath());

            Future<String> f = executor.submit(getCallable(res));
            futures.add(f);
        }

        //TODO Logging this on a lower warn level!
        for (Future<String> s : futures) {
            try {
                files.remove(s.get());
            } catch (Exception e) {
                LOGGER.error("Error while handling future", e);
            }
        }

        checkDownloadedFilesNumber(files);
        executor.shutdown();
    }

    private Callable<String> getCallable(final ResEntry res) {
        final String file = mResDir + res.getPath();

        return new Callable<String>() {

            @Override
            public String call() throws Exception {
                LOGGER.info("File Download started: " + file);
                mAccess.updateDownloadFile(res.getName());

                try {
                    DownloadHelper.downloadFileToDiskWithoutCheck(mResURL + res.getDownloadPath(), file);
                } catch (VtaDownloadException e) {
                    LOGGER.error("Could not download " + res.getName(), e);
                }
                mAccess.updateProgress(1);
                return file;
            }
        };
    }

    private void checkDownloadedFilesNumber(ArrayList<String> files) {
        if (files.size() == 0) {
            LOGGER.info("Finished with all Ressource-Downloads");
        } else {
            LOGGER.error("Not all files where removed from files-list");
        }
    }

    public void stopDownload() {
        mStop = true;
        executor.shutdown();
    }
}
