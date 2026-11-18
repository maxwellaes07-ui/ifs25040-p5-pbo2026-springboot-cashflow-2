package org.delcom.ifs25040springbootcashflow.adapter.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.UUID;

@Component
public class FileStorage {
    private final Path root;
    public FileStorage(@Value("${app.upload-dir:uploads}") String directory) {
        root=Path.of(directory).toAbsolutePath().normalize();
        try { Files.createDirectories(root); } catch(Exception e) { throw new IllegalStateException(e); }
    }
    public String save(MultipartFile file) {
        if(file==null||file.isEmpty()) return null;
        String name=UUID.randomUUID()+"-"+Path.of(file.getOriginalFilename()==null?"upload":file.getOriginalFilename()).getFileName();
        try { Files.copy(file.getInputStream(),root.resolve(name),StandardCopyOption.REPLACE_EXISTING); return "/uploads/"+name; }
        catch(Exception e) { throw new IllegalStateException("Gagal menyimpan file",e); }
    }
}
