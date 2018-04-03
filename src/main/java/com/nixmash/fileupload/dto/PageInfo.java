package com.nixmash.fileupload.dto;

import com.github.mustachejava.functions.TranslateBundleFunction;
import com.nixmash.fileupload.enums.ActiveMenu;
import com.nixmash.fileupload.enums.SidebarMenu;

import java.io.Serializable;

/**
 * Created by daveburke on 7/3/17.
 */
public class PageInfo implements Serializable {

    private static final long serialVersionUID = 5250923182891103240L;

    // region properties

    private Integer page_id;
    private String page_key;
    private String page_title;
    private String heading;
    private String subheading;
    private Boolean inProductionMode = false;
    private String menu;
    private ActiveMenu activeMenu;
    private SidebarMenu sidebarMenu;
    private TranslateBundleFunction trans;

    // endregion

    // region getter setters

    public Integer getPage_id() {
        return page_id;
    }

    public void setPage_id(Integer page_id) {
        this.page_id = page_id;
    }

    public String getPage_key() {
        return page_key;
    }

    public void setPage_key(String page_key) {
        this.page_key = page_key;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public Boolean getInProductionMode() {
        return inProductionMode;
    }

    public void setInProductionMode(Boolean inProductionMode) {
        this.inProductionMode = inProductionMode;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public ActiveMenu getActiveMenu() {
        return activeMenu;
    }

    public void setActiveMenu(ActiveMenu activeMenu) {
        this.activeMenu = activeMenu;
    }

    public SidebarMenu getSidebarMenu() {
        return sidebarMenu;
    }

    public void setSidebarMenu(SidebarMenu sidebarMenu) {
        this.sidebarMenu = sidebarMenu;
    }

    public TranslateBundleFunction getTrans() {
        return trans;
    }

    public void setTrans(TranslateBundleFunction trans) {
        this.trans = trans;
    }

    // endregion

    // region Builder

    public static Builder getBuilder(Integer page_id, String page_key, String page_title) {
        return new Builder(page_id, page_key, page_title);
    }

    public static class Builder {

        private PageInfo built;

        public Builder(Integer page_id, String page_key, String page_title) {
            built = new PageInfo();
            built.setPage_id(page_id);
            built.setPage_key(page_key);
            built.setPage_title(page_title);
        }

        public Builder heading(String heading) {
            built.setHeading(heading);
            return this;
        }

        public Builder subheading(String subheading) {
            built.setSubheading(subheading);
            return this;
        }

        public Builder inProductionMode(Boolean inProductionMode) {
            built.setInProductionMode(inProductionMode);
            return this;
        }

        public Builder sidebarMenu(SidebarMenu sidebarMenu) {
            built.setSidebarMenu(sidebarMenu);
            return this;
        }

        public Builder activeMenu(ActiveMenu activeMenu) {
            built.setActiveMenu(activeMenu);
            return this;
        }

        public Builder resourceBundle(TranslateBundleFunction translateBundleFunction) {
            built.setTrans(translateBundleFunction);
            return this;
        }

        public PageInfo build() {
            return built;
        }
    }

    // endregion

    // region toString()

    @Override
    public String toString() {
        return "PageInfo{" +
                "page_id=" + page_id +
                ", page_key='" + page_key + '\'' +
                ", page_title='" + page_title + '\'' +
                ", heading='" + heading + '\'' +
                ", subheading='" + subheading + '\'' +
                ", inProductionMode=" + inProductionMode +
                ", menu='" + menu + '\'' +
                ", activeMenu=" + activeMenu.toString() +
                ", sidebarMenu =" + sidebarMenu.toString()+
                '}';
    }


    // endregion

}
