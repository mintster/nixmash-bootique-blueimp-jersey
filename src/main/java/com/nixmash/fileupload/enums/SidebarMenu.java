package com.nixmash.fileupload.enums;

import java.io.Serializable;

public class SidebarMenu implements Serializable {

    private static final long serialVersionUID = 6383053677437314124L;

    private Boolean postsMenu = false;
    private Boolean tagsMenu = false;
    private Boolean searchMenu = false;
    private Boolean adminMenu = false;
    private Boolean adminPostsMenu = false;
    private Boolean adminNewPostMenu = false;
    private Boolean adminUsersMenu = false;
    private Boolean adminSiteMenu = false;
    private Boolean adminUtilitiesMenu = false;

    public SidebarMenu() {
    }

    public Boolean getPostsMenu() {
        return postsMenu;
    }

    public void setPostsMenu(Boolean postsMenu) {
        this.postsMenu = postsMenu;
    }

    public Boolean getTagsMenu() {
        return tagsMenu;
    }

    public void setTagsMenu(Boolean tagsMenu) {
        this.tagsMenu = tagsMenu;
    }

    public Boolean getSearchMenu() {
        return searchMenu;
    }

    public void setSearchMenu(Boolean searchMenu) {
        this.searchMenu = searchMenu;
    }

    public Boolean getAdminMenu() {
        return adminMenu;
    }

    public void setAdminMenu(Boolean adminMenu) {
        this.adminMenu = adminMenu;
    }

    public Boolean getAdminPostsMenu() {
        return adminPostsMenu;
    }

    public void setAdminPostsMenu(Boolean adminPostsMenu) {
        this.adminPostsMenu = adminPostsMenu;
    }

    public Boolean getAdminUsersMenu() {
        return adminUsersMenu;
    }

    public void setAdminUsersMenu(Boolean adminUsersMenu) {
        this.adminUsersMenu = adminUsersMenu;
    }

    public Boolean getAdminSiteMenu() {
        return adminSiteMenu;
    }

    public void setAdminSiteMenu(Boolean adminSiteMenu) {
        this.adminSiteMenu = adminSiteMenu;
    }

    public Boolean getAdminUtilitiesMenu() {
        return adminUtilitiesMenu;
    }

    public void setAdminUtilitiesMenu(Boolean adminUtilitiesMenu) {
        this.adminUtilitiesMenu = adminUtilitiesMenu;
    }

    public Boolean getAdminNewPostMenu() {
        return adminNewPostMenu;
    }

    public void setAdminNewPostMenu(Boolean adminNewPostMenu) {
        this.adminNewPostMenu = adminNewPostMenu;
    }
}
