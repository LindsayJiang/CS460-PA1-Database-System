package photoshare;

import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * A simple Picture class.
 *
 * @author G. Zervas <cs460tf@bu.edu>
 */
public class Picture {

  public int id;
  public int aid;
  public String caption = "";
  public String contentType;
  public long size;
  public byte[] data;
  public byte[] thumbdata;
  public static final int THUMBNAIL_WIDTH = 80;

  public Picture() {
  }

  public int getAid(){
    return aid;
  }
  public void setAid(int aid){
    this.aid = aid;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public byte[] getThumbdata() {
    return thumbdata;
  }

  public void setThumbdata(byte[] thumbdata) {
    this.thumbdata = thumbdata;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public String getFormat() {
    if (getContentType() != null) {
      return getContentType().substring(getContentType().lastIndexOf('/') + 1).toLowerCase();
    } else {
      return "";
    }
  }

  public void createThumbnail() {
    if (getData() != null && getContentType() != null) {
      try {
        BufferedImage source = ImageIO.read(new ByteArrayInputStream(getData()));
        int width = THUMBNAIL_WIDTH;
        int height = THUMBNAIL_WIDTH;// * (source.getWidth() / source.getHeight());
        BufferedImage thumb = new BufferedImage(width, height, source.getType());
        thumb.createGraphics().drawImage(source, 0, 0, width, height, null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(thumb, getFormat(), bos);
        setThumbdata(bos.toByteArray());
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }
}
