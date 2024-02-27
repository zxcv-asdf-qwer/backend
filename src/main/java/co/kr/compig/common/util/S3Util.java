package co.kr.compig.common.util;

import co.kr.compig.common.exception.UploadException;
import co.kr.compig.common.exception.dto.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Util {
  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String upload(MultipartFile multipartFile){
    if(multipartFile == null || multipartFile.isEmpty()) return null;

    try{
      byte[] fileBytes = multipartFile.getBytes();
      String fileName = generateFileName(multipartFile.getOriginalFilename());
      String contentType = multipartFile.getContentType();
      putS3(fileBytes, fileName, contentType);
      String imageUrl = generateUnsignedUrl(fileName);
      return imageUrl;
    }catch (IOException e){
      throw new UploadException(ErrorCode.PATH_VARIABLE_VALUE, e);
    }
  }

  public List<String> uploads(List<MultipartFile> multipartFiles){
    List<String> imageUrlList = new ArrayList<>();

    for(MultipartFile multipartFile : multipartFiles){
      try{
        byte[] fileBytes = multipartFile.getBytes();
        String fileName = generateFileName(multipartFile.getOriginalFilename());
        String contentType = multipartFile.getContentType();
        putS3(fileBytes, fileName, contentType);
        String imageUrl = generateUnsignedUrl(fileName);
        imageUrlList.add(imageUrl);
      }catch (IOException e){
        throw  new UploadException(ErrorCode.PATH_VARIABLE_VALUE, e);
      }
    }
    return imageUrlList;
  }

  public void putS3(byte[] fileBytes, String fileName, String contentType){
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(fileBytes.length);
    metadata.setContentType(contentType);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
    amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
  }

  public void delete(List<String> imageUrlList){
    for (String imageUrl : imageUrlList){
      if (StringUtils.hasText(imageUrl)){
        String fileName = extractObjectKeyFromUrl(imageUrl);
        try{
          String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
          if (amazonS3.doesObjectExist(bucket, decodedFileName)){
            amazonS3.deleteObject(bucket, decodedFileName);
          }
        }
        catch (IllegalArgumentException e){
          throw new UploadException(ErrorCode.FILE_DECODE_FAIL, e);
        }
      }
    }
  }

  public String extractObjectKeyFromUrl(String imageUrl){
    try{
      URL url = new URL(imageUrl);
      return url.getPath().substring(1);
    }catch (Exception e){
      throw new UploadException(ErrorCode.INVALID_INPUT_VALUE, e);
    }
  }
  public String generateFileName(String originalFilename){
    if (org.springframework.util.StringUtils.hasText(originalFilename)) {
      String extension = extractExtension(originalFilename);
      String uniqueId = UUID.randomUUID().toString();
      return uniqueId + "." + extension;
    }
    throw new UploadException(ErrorCode.INVALID_INPUT_VALUE);
  }

  public String extractExtension(String originalFilename) {
    if (org.springframework.util.StringUtils.hasText(originalFilename)) {
      int extensionIndex = originalFilename.lastIndexOf(".");
      if (extensionIndex != -1) {
        return originalFilename.substring(extensionIndex + 1);
      }
    }
    throw new UploadException(ErrorCode.EXTRACT_INVALID);
  }

  public String generateUnsignedUrl(String objectKey){
    String baseUrl = "https://" + bucket + ".s3.amazons.com/";
    return baseUrl + objectKey;
  }
}
