package gs.s3object.service.CephS3;

import java.net.URL;

public interface CephS3Service {
	URL generateUploadUrl(String objectKey, String contentType, int i);
	
	URL generateDownloadUrl(String key, int i);
}
