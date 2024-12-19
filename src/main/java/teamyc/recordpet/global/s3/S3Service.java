package teamyc.recordpet.global.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Configuration
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        String fileName = folderName + "/" + file.getOriginalFilename()+"_"+UUID.randomUUID();

        Path tempFile = Path.of(System.getProperty("java.io.tmpdir"), fileName);
        file.transferTo(tempFile);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, tempFile);

        return getPublicUrl(fileName);
    }
    private String getPublicUrl(String fileName){
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
    }
}