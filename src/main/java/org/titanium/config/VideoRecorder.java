package org.titanium.config;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

import java.awt.*;
import java.io.File;

import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.titanium.utils.DriverException;

public class VideoRecorder {
	private ScreenRecorder screenRecorder;
	
	public void startRecording(String videoPath) throws Exception
    {
		try{
		File file = new File(videoPath);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
                       
        Rectangle captureSize = new Rectangle(0,0, width, height);
                                 
         GraphicsConfiguration gc = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();
         
         this.screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
                 new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                 new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                      CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                      DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                      QualityKey, 1.0f,
                      KeyFrameIntervalKey, 15 * 60),
                 new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                      FrameRateKey, Rational.valueOf(30)),
                 null, file, "ScreenRecorded");
         
       this.screenRecorder.start();
		}catch(Exception ex){
			throw new DriverException("Class VideoRecorder | Method startRecording | Exception desc: Error creating video"+ ex.getMessage());
		}
    }

    public void stopRecording() throws Exception
    {
      this.screenRecorder.stop();
    }
}
