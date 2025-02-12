package vn.project.group_lottery.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Service
public class UploadImgService {
    private final ServletContext servletContext;

    public UploadImgService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String handleSaveUploadImg(MultipartFile file, String targetFolder) {
        String rootPath = this.servletContext.getRealPath("/resources/common/");
        String finalName = "";

        if (file == null || file.isEmpty()) {
            return "default_avatar.jpg";
        }

        // Kiểm tra loại file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Chỉ được phép tải lên file ảnh!");
        }

        // Kiểm tra kích thước file (tối đa 5MB)
        long maxFileSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("Kích thước file không được vượt quá 5MB!");
        }

        try {
            byte[] bytes = file.getBytes();

            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            finalName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);

            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage(), e);
        }

        return finalName;
    }
}
