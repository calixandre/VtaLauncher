package de.vta.vtalauncher.logic;

import de.vta.vtalauncher.Config;
import de.vta.vtalauncher.exception.vtaException;
import de.vta.vtalauncher.exception.vtaLogicException;
import de.vta.vtalauncher.logic.auth.MinecraftUser;
import de.vta.vtalauncher.logic.download.DownloadHelper;
import de.vta.vtalauncher.logic.version.MinecraftVersion;

import java.util.HashMap;
import java.util.List;
import java.util.Observer;

/**
 * Facade class separates logic and gui components / classes.
 *
 * @author redbeard
 * @author saschb2b
 */
public class Facade {
    // Implements the singleton pattern.
    private static Facade mInstance = new Facade();

    private LogicController mController;

    private Facade() {
        mController = new LogicController();
    }

    /**
     * Returns the facade instance.
     *
     * @return the facade instance
     */
    public static Facade getInstance() {
        return mInstance;
    }

    /**
     * Initializes the needed logic classes.
     * Need an {@see #UIParser} for init.
     * Has to be called first!
     *
     * @param config holds the command line arguments.
     * @throws vtaLogicException when something went wrong.
     */
    public void init(Config config) throws vtaLogicException {
        mController.init(config);
    }

    /**
     * If setForce is set to true, all Minecraft files will be downloaded fresh
     * and replaces the local files.
     * <p>
     * Default is false.
     *
     * @param force
     */
    public void setForce(boolean force) {
        DownloadHelper.setForce(force);
    }

    /**
     * Returns true of force login shall be used.
     *
     * @return
     */
    public boolean isForceLogin() {
        return mController.isForceLogin();
    }

    /**
     * Sets the username and corresponding password.
     *
     * @param username
     * @param password
     * @throws vtaLogicException when username or password are wrong or network failure
     */
    public void setUser(String username, char[] password) throws vtaLogicException {
        mController.setUser(username, password);
    }

    /**
     * Returns the current user. This user can be either a saved one, given by command line
     * or set by setUser().
     *
     * @return
     */
    public MinecraftUser getUser() throws vtaLogicException {
        return mController.getUser();
    }

    /**
     * Returns a list of current saved users.
     *
     * @return
     */
    // TODO Move AuthenticationSevice into facade class.
    public List<MinecraftUser> getUsers() {
        return mController.getUsers();
    }

    /**
     * Authenticate the current user.
     *
     * @throws vtaLogicException if authentication failed
     */
    public void authenticateUser() throws vtaException {
        mController.authenticateUser();
    }

    /**
     * Sets the Minecraft version name and starts the donwload process.
     *
     * @param version
     * @throws vtaLogicException will be thrown if this version is unknown or not startable.
     */
    public void setMinecraftVersion(String version) throws vtaLogicException {
        mController.setMinecraftVersion(version);
    }

    /**
     * Returns the current choosen Minecraft version.
     *
     * @throws vtaLogicException if no version was choosen.
     */
    public MinecraftVersion getMinecraftVersion() throws vtaLogicException {
        return mController.getMinecraftVersion();
    }

    /**
     * Returns a list of startable Versions.
     * This list contains all local versions, all minecraft.net versions and the version set through
     * command line argument.
     *
     * @return list of possible versions
     * @throws vtaLogicException if building the list was not possible.
     */
    public List<String> getMinecraftVersions() throws vtaLogicException {
        return mController.getMinecraftVersions();
    }

    /**
     * Starts minecraft in a new process.
     *
     * @throws vtaLogicException if start was not possible
     */
    public void startMinecraft() throws vtaLogicException {
        mController.startMinecraft();
    }

    /**
     * Starts minecraft in a new process.
     *
     * @param username
     * @throws vtaLogicException if start was not possible
     */
    public void startMinecraftWithoutLogin(String username) throws vtaLogicException {
        mController.startMinecraftWithoutLogin(username);
    }

    /**
     * Returns a Minecraft argument for the specified key.
     * e.g server = returns the server address which shall be joined on startup.
     *
     * @param key
     * @throws vtaLogicException is thrown if the key does not exist.
     */
    public String getMinecraftArgument(String key) throws vtaLogicException {
        return mController.getMinecraftArgument(key);
    }

    /**
     * Sets a new kv pair.
     *
     * @param key
     * @param value
     * @throws vtaLogicException if it was not possible.
     */
    public void setMinecraftArgument(String key, String value) throws vtaLogicException {
        mController.setMinecraftArguments(key, value);
    }

    /**
     * Returns all current arguments.
     *
     * @return
     * @throws vtaLogicException if something went wrong.
     */
    public HashMap<String, String> getMinecraftArguments() throws vtaLogicException {
        return mController.getMinecraftArguments();

    }

    /**
     * Sets a new observer to get information about the download status.
     *
     * @param server
     */
    public void setMinecraftDownloadObserver(Observer server) {
        mController.setDownloadObserver(server);
    }

    /**
     * Sets a new observer to get information about the skin download.
     * Also starts the skin download. This method can be invoked if the user
     * is authenticated.
     *
     * @param server
     */
    public void setSkinObserver(Observer server) {
        mController.setSkinObserver(server);
    }

    /**
     * Returns true if Minecraft was completely downloaded.
     *
     * @return
     */
    public boolean isMinecraftDownloaded() {
        return mController.isMinecraftDownloaded();
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        mController.logout();
    }

    /**
     * Returns true if minecraft should be started directly.
     *
     * @return boolean
     */
    public boolean isQuickPlay() {
        return mController.isQuickPlay();
    }


}
