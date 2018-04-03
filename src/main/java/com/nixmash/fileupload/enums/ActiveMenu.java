package com.nixmash.fileupload.enums;

/**
 * Created by daveburke on 7/5/17.
 */
public class ActiveMenu {

    private Boolean uploadsMenu = false;
    private Boolean downloadsMenu = false;

    public ActiveMenu() {
    }

    public Boolean getUploadsMenu() {
        return uploadsMenu;
    }

    public void setUploadsMenu(Boolean uploadsMenu) {
        this.uploadsMenu = uploadsMenu;
    }

    public Boolean getDownloadsMenu() {
        return downloadsMenu;
    }

    public void setDownloadsMenu(Boolean downloadsMenu) {
        this.downloadsMenu = downloadsMenu;
    }
}
