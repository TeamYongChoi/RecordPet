package teamyc.recordpet.global.s3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3Service.uploadFile(file, "uploads");
            return ResponseEntity.ok("정상적으로 파일이 업로드 되었습니다. "+ fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("업로드에 실패했습니다." + e.getMessage());
        }
    }
}
