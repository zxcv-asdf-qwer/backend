package co.kr.compig.global.utils;

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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import co.kr.compig.api.presentation.board.response.SystemFileResponse;
import co.kr.compig.global.error.exception.UploadException;
import co.kr.compig.global.error.model.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Util {
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.folder}")
	private String folder;

	@Value("${cloud.aws.cloudfront.url}")
	private String cloudfront;

	public String upload(MultipartFile multipartFile) {
		if (multipartFile == null || multipartFile.isEmpty())
			return null;

		try {
			byte[] fileBytes = multipartFile.getBytes();
			String fileName = generateFileName(multipartFile.getOriginalFilename());
			String contentType = multipartFile.getContentType();
			putS3(fileBytes, fileName, contentType);
			String imageUrl = generateUnsignedUrl(fileName);
			return imageUrl;
		} catch (IOException e) {
			throw new UploadException(ErrorCode.INVALID_INPUT_VALUE, e);
		}
	}

	public List<String> uploads(List<MultipartFile> multipartFiles) {
		List<String> imageUrlList = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFiles) {
			try {
				byte[] fileBytes = multipartFile.getBytes();
				String fileName = generateFileName(multipartFile.getOriginalFilename());
				String contentType = multipartFile.getContentType();
				putS3(fileBytes, fileName, contentType);
				String imageUrl = generateUnsignedUrl(fileName);
				imageUrlList.add(imageUrl);
			} catch (IOException e) {
				throw new UploadException(ErrorCode.INVALID_INPUT_VALUE, e);
			}
		}
		return imageUrlList;
	}

	public void putS3(byte[] fileBytes, String fileName, String contentType) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(fileBytes.length);
		metadata.setContentType(contentType);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
		amazonS3.putObject(new PutObjectRequest(bucket + "/" + folder, fileName, inputStream, metadata));
	}

	public void delete(List<String> imageUrlList) {
		for (String imageUrl : imageUrlList) {
			if (StringUtils.hasText(imageUrl)) {
				String fileName = extractObjectKeyFromUrl(imageUrl);
				try {
					String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
					if (amazonS3.doesObjectExist(bucket + "/" + folder, decodedFileName)) {
						amazonS3.deleteObject(bucket + "/" + folder, decodedFileName);
					}
				} catch (IllegalArgumentException e) {
					throw new UploadException("파일 이름 디코딩에 실패했습니다.", e);
				}
			}
		}
	}

	public String extractObjectKeyFromUrl(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			return url.getPath().substring(1);
		} catch (Exception e) {
			throw new UploadException(ErrorCode.INVALID_INPUT_VALUE, e);
		}
	}

	public String generateFileName(String originalFilename) {
		if (StringUtils.hasText(originalFilename)) {
			return UUID.randomUUID().toString();
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
		throw new UploadException("확장자를 추출할 수 없습니다.");
	}

	public String generateUnsignedUrl(String objectKey) {
		return cloudfront + objectKey;
	}

	////////////////////////////////////////
	// base64
	public List<String> uploadBase64(Map<String, String> img) {
		List<MultipartFile> multipartFiles = new ArrayList<>();
		for (String key : img.keySet()) {
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

			MultipartFile multipartFile = new MockMultipartFile(originName, generateFileName(originName), contentsType,
				byteArrayInputStream.readAllBytes());

			return multipartFile;
		} catch (IOException e) {
		}
		return null;
	}

	public List<SystemFileResponse> uploadsToFile(List<MultipartFile> multipartFiles) {
		List<SystemFileResponse> imageUrlList = new ArrayList<>();

		for (MultipartFile multipartFile : multipartFiles) {
			try {
				byte[] fileBytes = multipartFile.getBytes();
				String fileName = generateFileName(multipartFile.getOriginalFilename());
				String contentType = multipartFile.getContentType();
				putS3(fileBytes, fileName, contentType);
				String imageUrl = generateUnsignedUrl(fileName);
				SystemFileResponse systemFileResponse = SystemFileResponse.builder()
					.filePath(imageUrl)
					.fileNm(fileName)
					.fileExtension(contentType)
					.build();
				imageUrlList.add(systemFileResponse);
			} catch (IOException e) {
				throw new UploadException(ErrorCode.INVALID_INPUT_VALUE, e);
			}
		}
		return imageUrlList;
	}

	public List<SystemFileResponse> uploadBase64ToFile(Map<String, String> img) {
		List<MultipartFile> multipartFiles = new ArrayList<>();
		for (String key : img.keySet()) {
			String contentType = img.get(key).substring(5).split(";")[0];
			MultipartFile multipartFile = createMultipartFile(img.get(key), contentType, key);
			multipartFiles.add(multipartFile);
		}
		List<SystemFileResponse> imageUrlList = uploadsToFile(multipartFiles);
		return imageUrlList;
	}

	public List<SystemFileResponse> uploadToFile(List<MultipartFile> img) {
		List<SystemFileResponse> imageUrlList = uploadsToFile(img);
		return imageUrlList;
	}
}
