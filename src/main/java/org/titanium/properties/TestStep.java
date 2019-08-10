package org.titanium.properties;

public class TestStep extends Test {
    private String PageName;
    private String PageObject;
    private String ActionKeyword;
    private String Data;
    private String Link;

    public void setPageName(String sPageName) {
        this.PageName = sPageName;
    }

    public String getPageObject() {
        return this.PageObject;
    }

    public void setPageObject(String sPageObject) {
        this.PageObject = sPageObject;
    }

    public String getActionKeyword() {
        return this.ActionKeyword;
    }

    public void setActionKeyword(String sActionKeyword) {
        this.ActionKeyword = sActionKeyword;
    }

    public String getData() {
        return this.Data;
    }

    public void setData(String sData) {
        this.Data = sData;
    }

    public String getLink() {
        return this.Link;
    }

    public void setLink(String sLink) {
        this.Link = sLink;
    }
}
