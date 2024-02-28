package co.kr.compig.common.utils;

import co.kr.compig.api.board.dto.FileResponse;
import co.kr.compig.common.exception.UploadException;
import co.kr.compig.common.exception.dto.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    if (StringUtils.hasText(originalFilename)) {
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
    String baseUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/";
    return baseUrl + objectKey;
  }

  ////////////////////////////////////////
  // base64
  public List<String> uploadBase64(Map<String, String> img){
    List<MultipartFile> multipartFiles = new ArrayList<>();
    for(String key : img.keySet()){
      String contentType = img.get(key).substring(5).split(";")[0];
      String fileName = key;
      MultipartFile multipartFile = createMultipartFile(img.get(key), contentType, key);
      multipartFiles.add(multipartFile);
    }
    List<String> imageUrlList = uploads(multipartFiles);
    return imageUrlList;
  }


  private MultipartFile createMultipartFile(String base64String, String contentsType, String originName) {
    byte[] image = Base64.getDecoder().decode((base64String.substring(base64String.indexOf(",") + 1)).getBytes());
    int totalCnt = 1024;
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(totalCnt)) {
      int offset = 0;
      while (offset < image.length) {
        int chunkSize = Math.min(totalCnt, image.length - offset);

        byte[] byteArray = new byte[chunkSize];
        System.arraycopy(image, offset, byteArray, 0, chunkSize);

        byteArrayOutputStream.write(byteArray);
        byteArrayOutputStream.flush();

        offset += chunkSize;
      }

      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

      MultipartFile multipartFile = new MockMultipartFile(originName, generateFileName(originName), contentsType, byteArrayInputStream.readAllBytes());

      return multipartFile;
    } catch (IOException e) {
    }
    return null;
  }
  /////////////
  // file 테이블 생성 후 uploads
  public List<FileResponse> uploadsToFile(List<MultipartFile> multipartFiles){
    List<FileResponse> imageUrlList = new ArrayList<>();

    for(MultipartFile multipartFile : multipartFiles){
      try{
        byte[] fileBytes = multipartFile.getBytes();
        String fileName = generateFileName(multipartFile.getOriginalFilename());
        String contentType = multipartFile.getContentType();
        putS3(fileBytes, fileName, contentType);
        String imageUrl = generateUnsignedUrl(fileName);
        FileResponse fileResponse = FileResponse.builder()
            .filePath(imageUrl)
            .fileNm(fileName)
            .fileExtension(contentType)
            .build();
        imageUrlList.add(fileResponse);
      }catch (IOException e){
        throw  new UploadException(ErrorCode.PATH_VARIABLE_VALUE, e);
      }
    }
    return imageUrlList;
  }

  public List<FileResponse> uploadBase64ToFile(Map<String, String> img){
    List<MultipartFile> multipartFiles = new ArrayList<>();
    for(String key : img.keySet()){
      String contentType = img.get(key).substring(5).split(";")[0];
      String fileName = key;
      MultipartFile multipartFile = createMultipartFile(img.get(key), contentType, key);
      multipartFiles.add(multipartFile);
    }
    List<FileResponse> imageUrlList = uploadsToFile(multipartFiles);
    return imageUrlList;
  }
}
