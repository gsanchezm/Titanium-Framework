package org.titanium.properties;

public class General {
    private String browserDriver;
    private String screenShot;
    private String customScreenShot;
    private String videoPath;

    public String getErrorException() {
        return errorException;
    }

    public void setErrorException(String errorException) {
        this.errorException = errorException;
    }

    private String errorException;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    private String video;

    public String getBrowserDriver() {
        return browserDriver;
    }

    public void setBrowserDriver(String browserDriver) {
        this.browserDriver = browserDriver;
    }

    public String getVideoPath(){return this.videoPath;}

    public void setVideoPath(String svideoPath){this.videoPath = svideoPath;}

    public String getScreenShot() {
        return this.screenShot;
    }

    public void setScreenShot(String sScreenShot) {
        this.screenShot = sScreenShot;
    }

    public String getCustomScreenShot() {
        return this.customScreenShot;
    }

    public void setCustomScreenShot(String sCustomScreenShot) {
        this.customScreenShot = sCustomScreenShot;
    }
}
