package de.vta.vtalauncher.logic.version;

import de.vta.vtalauncher.logic.resources.Version;

public class MinecraftVersion {
    private String mVersion;
    private Version mVersionJson;

    public MinecraftVersion(String version) {
        this.mVersion = version;
    }

    public String getVersion() {
        return mVersion;
    }

    public String getAssets() {
        return mVersionJson.getAssets();
    }

    public boolean hasAssets() {
        return mVersionJson.hasAssets();
    }

    public void setVersionJson(Version versionJson) {
        this.mVersionJson = versionJson;
    }

    public Version getVersionJson() {
        return mVersionJson;
    }
}
