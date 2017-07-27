package de.vta.vtalauncher;

import com.tngtech.configbuilder.ConfigBuilder;
import de.vta.vtalauncher.exception.vtaLogicException;
import de.vta.vtalauncher.gui.MainController;
import de.vta.vtalauncher.logic.Facade;
import de.vta.util.OSHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * vta Launcher main class.
 * Logs several infos about the system and starts / initializes the logic and gui.
 *
 */
class Application {
    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    /**
     * The entry point of the application.
     *
     * @param args command line arguments
     * @throws VtaLogicException if launching the application fails
     */
    public static void main(String[] args) throws VtaLogicException {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        logSystemInfo();
        Config config = ConfigBuilder.on(Config.class).withCommandLineArgs(args).build();
        Facade.getInstance().init(config);
        startGUI();
    }

    /**
     * Logs several system information.
     */
    private static void logSystemInfo() {
        LOGGER.info("Launcher started!");

        Package objPackage = Application.class.getPackage();
        LOGGER.info("Launcher version: " + objPackage.getSpecificationVersion());

        LOGGER.debug("OS : " + System.getProperty("os.name"));
        LOGGER.debug("OS Arch: " + OSHelper.getOSArch());
        LOGGER.debug("OS Java Arch : " + System.getProperty("os.arch"));
        LOGGER.debug("OS Version : " + System.getProperty("os.version"));
        LOGGER.debug("Username : " + System.getProperty("user.name"));
        LOGGER.debug("Java Vendor : " + System.getProperty("java.vendor"));
        LOGGER.debug("Java Version : " + System.getProperty("java.version"));
        LOGGER.debug("Java Home : " + System.getProperty("java.home"));
        LOGGER.debug("Java Classpath : " + System.getProperty("java.class.path"));
        LOGGER.debug("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
        LOGGER.debug("Total memory (bytes): " + Runtime.getRuntime().totalMemory());
        LOGGER.info("Date: " + new SimpleDateFormat("dd.MM.yy").format(new Date()));
    }

    /**
     * Starts the gui by invoking the gui manager.
     */
    private static void startGUI() {
        LOGGER.debug("Starting GUI");

        invokeLater(new Runnable() {
            @Override
            public void run() {
                MainController.getInstance().openMainWindow();
            }
        });
    }
}
