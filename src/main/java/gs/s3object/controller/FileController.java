package gs.s3object.controller;

import gs.s3object.service.CephS3.CephS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
	
	private final CephS3Service s3Service;
	
	@GetMapping("/presign-upload")
	public ResponseEntity<String> presignUpload(
			@RequestParam String filename,
			@RequestParam(defaultValue = "image/jpeg") String contentType) {
		String objectKey = UUID.randomUUID() + "_" + filename;
		URL url = s3Service.generateUploadUrl(objectKey, contentType, 600);
		return ResponseEntity.ok(url.toString());
	}
	
	@GetMapping("/presign-download")
	public ResponseEntity<String> presignDownload(@RequestParam String key) {
		URL url = s3Service.generateDownloadUrl(key, 3600);
		return ResponseEntity.ok(url.toString());
	}
}
