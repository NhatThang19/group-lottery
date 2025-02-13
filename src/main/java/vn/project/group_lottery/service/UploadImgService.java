package vn.project.group_lottery.service;

import java.io.*;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadImgService {

    public String handleSaveUploadImg(MultipartFile file, String targetFolder) {
        String rootPath = System.getProperty("user.dir") + "/src/main/resources/static/common/";
        String finalName = "default_avatar.jpg";

        if (file == null || file.isEmpty()) {
            return finalName;
        }

        try {
            byte[] bytes = file.getBytes();
            File dir = new File(rootPath + File.separator + targetFolder);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!dir.canWrite()) {
                throw new RuntimeException("Không thể ghi vào thư mục: " + dir.getAbsolutePath());
            }

            String cleanFileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
            finalName = UUID.randomUUID().toString() + "-" + cleanFileName;
            File serverFile = new File(dir, finalName);

            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage(), e);
        }

        return finalName;
    }
}
