package com.idega.block.banner.business;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.banner.bean.ImageResizeAction;
import com.idega.block.banner.bean.ImageResizeRequest;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class BannerImageResizeService {

  /**
   * Resize a source image based on request parameters.
   * @param request
   * @return Resulting image
   * @throws Exception
   */
  public BufferedImage resize(ImageResizeRequest request) throws IOException {
    BufferedImage result = null;
    BufferedImage source = this.getSource(request);
    int sourceWidth = source.getWidth();
    int sourceHeight = source.getHeight();

    if (request.getTargetWidth() == null) {
      throw new IOException("No value set for image targetWidth");
    } else if (request.getTargetHeight() == null) {
      throw new IOException("No value set for image targetHeight");
    }
    int targetWidth = request.getTargetWidth();
    int targetHeight = request.getTargetHeight();

    boolean resizeImage = false;
    boolean cropImage = false;
    if (targetHeight == sourceHeight && targetWidth == sourceWidth) {
      resizeImage = false;  // Explicitly mop up the case where provided image is already the right size and needs neither crop nor resize
    } else if (request.getResizeAction() == ImageResizeAction.ALWAYS) {
      if (!request.isCropToAspect()) {
        if (targetHeight != sourceHeight || targetWidth != sourceWidth) {
          resizeImage = true;
        }
      } else { // Check if source different in both dimensions - if not, we can crop rather than resize
        if (targetHeight != sourceHeight && targetWidth != sourceWidth) {
          resizeImage = true;
        }
        if (targetHeight != sourceHeight || targetWidth != sourceWidth) {
          cropImage = true;
        }
      }
    } else if (request.getResizeAction() == ImageResizeAction.IF_SMALLER) {
      if (!request.isCropToAspect()) {
        if (targetHeight > sourceHeight || targetWidth > sourceWidth) {
          resizeImage = true;
        }
      } else { // Check if source smaller than both dimensions - if not, we can crop rather than resize
        if (targetHeight > sourceHeight && targetWidth > sourceWidth) {
          resizeImage = true;
        }
        if (targetHeight > sourceHeight || targetWidth > sourceWidth) {
          cropImage = true;
        }
      }
    } else if (request.getResizeAction() == ImageResizeAction.IF_LARGER)  {
      if (!request.isCropToAspect()) {
        if (targetHeight < sourceHeight || targetWidth < sourceWidth) {
          resizeImage = true;
        }
      } else {  // Check if source larger than both dimensions - if not, we can crop rather than resize
        if (targetHeight < sourceHeight && targetWidth < sourceWidth) {
          resizeImage = true;
        }
        if (targetHeight < sourceHeight || targetWidth < sourceWidth) {
          cropImage = true;
        }
      }
    } // else assume ImageResizeAction.NEVER

    request.setResized(resizeImage);
    request.setCropped(cropImage);

    if (resizeImage || cropImage) {
      double scaleX, scaleY;

      if (request.isMaintainAspect()) {
        result = source;
        if (request.isCropToAspect()) {
          if (resizeImage) {
            // Use aspect ratio to determine final image size and work back to determine which dimension to use when resizing
            float aspectRatio = (float)sourceHeight / (float)sourceWidth;
            if ( (targetHeight / aspectRatio) < targetWidth ) {
              scaleX = scaleY = (double) targetWidth / sourceWidth;
            } else {
              scaleX = scaleY = (double) targetHeight / sourceHeight;
            }
            sourceWidth = (int) Math.rint(scaleX * sourceWidth);
            sourceHeight = (int) Math.rint(scaleY * sourceHeight);

            result = this.doResize(sourceWidth, sourceHeight, scaleX, scaleY, source);
          }

          // We know our new image matches one target dimension so crop the overhanging part of the other dimension in equal parts
          if (sourceHeight > targetHeight) {    // Chop the extra height
            int yOffset = (int) Math.rint( (sourceHeight - targetHeight) / 2);
            result = result.getSubimage(0, yOffset, sourceWidth, targetHeight);
          } else if (sourceWidth > targetWidth) { // Chop the extra width
            int xOffset = (int) Math.rint( (sourceWidth - targetWidth) / 2);
            result = result.getSubimage(xOffset, 0, targetWidth, sourceHeight);
          }
        } else if (resizeImage) {
          if (sourceWidth > sourceHeight) {
            scaleX = scaleY = (double) targetWidth / sourceWidth;
          } else {
            scaleX = scaleY = (double) targetHeight / sourceHeight;
          }
          sourceWidth = (int) Math.rint(scaleX * sourceWidth);
          sourceHeight = (int) Math.rint(scaleY * sourceHeight);

          result = this.doResize(sourceWidth, sourceHeight, scaleX, scaleY, source);
        }
      } else {
        // Scale both axes to fit entire image and ignore maintaining aspect ratio
        scaleX = (double) targetWidth / sourceWidth;
        scaleY = (double) targetHeight / sourceHeight;

        result = this.doResize(targetWidth, targetHeight, scaleX, scaleY, source);
      }
    } else {
      // No resize necessary so skip that step and just return existing image after optionally saving it
      result = source;
    }

    if (result != null && request.getDestinationFilePath() != null) {
      this.writeJPEG(result, request);
    }

    return result;
  }

  /**
   * Perform actual resize based on provided parameters.
   *
   * @param newWidth The width of the new image
   * @param newHeight The height of the new image
   * @param scaleX The amount to scale source image by in the X axis
   * @param scaleY The amount to scale source image by in the Y axis
   * @param source The source image to resize
   * @return New BufferedImage being a scaled version of source
   */
  private BufferedImage doResize(int newWidth, int newHeight, double scaleX, double scaleY, BufferedImage source) {
    BufferedImage result = new BufferedImage(newWidth, newHeight, source.getType());
    Graphics2D g2d = null;
    try {
      g2d = result.createGraphics();
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2d.scale(scaleX, scaleY);
      g2d.drawImage(source, 0, 0, null);
    } finally {
      if (g2d != null) { g2d.dispose(); }
    }
    return result;
  }

  private void writeJPEG(BufferedImage input, ImageResizeRequest imageResizeRequest) throws IOException {
    Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("JPG");
    if (iter.hasNext()) {
      ImageWriter writer = iter.next();
      ImageWriteParam iwp = writer.getDefaultWriteParam();
      iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      iwp.setCompressionQuality(imageResizeRequest.getCompressionQuality());

      File outFile = new File(imageResizeRequest.getDestinationFilePath());
      FileImageOutputStream output = new FileImageOutputStream(outFile);
      writer.setOutput(output);
      IIOImage image = new IIOImage(input, null, null);
      writer.write(null, image, iwp);

      output.close();
    }
  }

  /**
   * Return BufferedImage based on source information provided in the request.
   */
  private BufferedImage getSource(ImageResizeRequest request) throws IOException {
    if (request.getSourceImage() != null) {
      return request.getSourceImage();
    }

    File sourceFile = null;
    if (request.getSourceFile() != null && request.getSourceFile().canRead()) {
      sourceFile = request.getSourceFile();
    } else if (request.getSourceFilePath() != null) {
      sourceFile = new File(request.getSourceFilePath());
      if (!sourceFile.canRead()) {
        throw new IOException("Cannot read source image file from provided path.");
      }
    }

    if (sourceFile == null) {
      throw new IOException("Unable to get a source image file from any of the provided information.");
    }

    return ImageIO.read(sourceFile);
  }
}
