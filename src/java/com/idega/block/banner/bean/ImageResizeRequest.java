package com.idega.block.banner.bean;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Set of parameters to control the resizing of an image.
 *
 * This is based heavily on the work done for image resizing in autopublish,
 * but without any of the benefits of being able to use ImageMagik.
 * In addition to parameters to influence the output, you must pass in the source information for the image
 * as one of the following, with increasing priority if multiple provided: absolute path to file, File object, BufferedImage object.
 * @author Future Medium Pty. Ltd - http://www.futuremedium.com.au/
 */
public class ImageResizeRequest {
  private ImageResizeAction resizeAction = ImageResizeAction.IF_LARGER;
  private boolean maintainAspect = true;
  private boolean cropToAspect = true;
  private Integer targetHeight;
  private Integer targetWidth;
  
  private String sourceFilePath;
  private File sourceFile;
  private BufferedImage sourceImage;

  private String destinationFilePath;
  private float compressionQuality = 0.95f;

  private boolean resized;
  private boolean cropped;

  /**
   * Empty, no-arg constructor.
   */
  public ImageResizeRequest() {

  }
  
  /**
   * Return type of resizing to perform if image dimensions don't match requested dimensions (Default: ImageResizeAction.IF_LARGER).
   *
   * @return the resizeAction
   */
  public ImageResizeAction getResizeAction() {
    return resizeAction;
  }

  /**
   * Set type of resizing to perform if image dimensions don't match requested dimensions.
   *
   * @param resizeAction the resizeAction to set
   */
  public void setResizeAction(ImageResizeAction resizeAction) {
    this.resizeAction = resizeAction;
  }

  /**
   * Return whether aspect ratio is being maintained when resizing (Default: true).
   *
   * @return the maintainAspect
   */
  public boolean isMaintainAspect() {
    return maintainAspect;
  }

  /**
   * Set whether aspect ratio is being maintained when resizing.
   *
   * @param maintainAspect the maintainAspect to set
   */
  public void setMaintainAspect(boolean maintainAspect) {
    this.maintainAspect = maintainAspect;
  }

  /**
   * Return whether image is cropped in order to maintain aspect and fill the requested dimensions completely (Default: true).
   * 
   * @return the cropToAspect
   */
  public boolean isCropToAspect() {
    return cropToAspect;
  }

  /**
   * Set whether image is cropped in order to maintain aspect and fill the requested dimensions completely.
   *
   * @param cropToAspect the cropToAspect to set
   */
  public void setCropToAspect(boolean cropToAspect) {
    this.cropToAspect = cropToAspect;
  }

  /**
   * Return the height requested for resized image.
   *
   * @return the targetHeight
   */
  public Integer getTargetHeight() {
    return targetHeight;
  }

  /**
   * Set the height for resized image.
   *
   * @param targetHeight the targetHeight to set
   */
  public void setTargetHeight(Integer targetHeight) {
    this.targetHeight = targetHeight;
  }

  /**
   * Return the width requested for resized image.
   *
   * @return the targetWidth
   */
  public Integer getTargetWidth() {
    return targetWidth;
  }

  /**
   * Set the width for resized image.
   *
   * @param targetWidth the targetWidth to set
   */
  public void setTargetWidth(Integer targetWidth) {
    this.targetWidth = targetWidth;
  }

  /**
   * Return the absolute full file path to source image.
   *
   * @return the sourceFilePath
   */
  public String getSourceFilePath() {
    return sourceFilePath;
  }

  /**
   * Set the absolute full file path to source image.
   *
   * @param sourceFilePath the sourceFilePath to set
   */
  public void setSourceFilePath(String sourceFilePath) {
    this.sourceFilePath = sourceFilePath;
  }

  /**
   * Return the source File object.
   *
   * @return the sourceFile
   */
  public File getSourceFile() {
    return sourceFile;
  }

  /**
   * Set the source File object.
   *
   * @param sourceFile the sourceFile to set
   */
  public void setSourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  /**
   * Return source BufferedImage.
   *
   * @return the sourceImage
   */
  public BufferedImage getSourceImage() {
    return sourceImage;
  }

  /**
   * Set source BufferedImage.
   *
   * @param sourceImage the sourceImage to set
   */
  public void setSourceImage(BufferedImage sourceImage) {
    this.sourceImage = sourceImage;
  }

  /**
   * Return the absolute full file path to destination image.
   *
   * @return the destinationFilePath
   */
  public String getDestinationFilePath() {
    return destinationFilePath;
  }

  /**
   * Set absolute path to write resulting image.
   *
   * This may be null, in which case no file is written and the caller can use the BufferedImage
   * returned by the 'resize' request to chain actions together to produce a final file.
   * 
   * @param destinationFilePath the destinationFilePath to set
   */
  public void setDestinationFilePath(String destinationFilePath) {
    this.destinationFilePath = destinationFilePath;
  }

  /**
   * Return the requested compression quality between 0 and 1.
   *
   * @return the compressionQuality
   */
  public float getCompressionQuality() {
    return compressionQuality;
  }

  /**
   * Set compression quality between 0 and 1, 1 being highest.
   * 
   * @param compressionQuality the compressionQuality to set
   */
  public void setCompressionQuality(float compressionQuality) {
    this.compressionQuality = compressionQuality;
  }

  /**
   * Return whether image was resized during Resize operation.
   * @return Whether image was resized
   */
  public boolean isResized() {
    return resized;
  }

  /**
   * Image resize service can set whether image actually resized.
   *
   * @param resized Whether image was resized
   */
  public void setResized(boolean resized) {
    this.resized = resized;
  }

  /**
   * Return whether image was cropped during Resize operation.
   * @return Whether image was cropped
   */
  public boolean isCropped() {
    return cropped;
  }

  /**
   * Image resize service can set whether image cropped as part of the process.
   *
   * @param cropped Whether image was cropped
   */
  public void setCropped(boolean cropped) {
    this.cropped = cropped;
  }
 
}