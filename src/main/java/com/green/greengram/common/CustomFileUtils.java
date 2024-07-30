package com.green.greengram.common;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
@Getter
@ToString
public class CustomFileUtils {
    public final String uploadPath;

    public CustomFileUtils(@Value("${file.directory}") String uploadPath) {
        this.uploadPath = uploadPath;
    }
    public String makeFolders(String path){
        File folder = new File(uploadPath, path);
        folder.mkdirs();
        return folder.getAbsolutePath();
    }
    public String getExt(String fileName){
        int idx = fileName.lastIndexOf(".");
        return fileName.substring(idx);
    }
    public String makeRandomFileName(){
        return UUID.randomUUID().toString();
    }
    public String makeRandomFileName(String fileName){
        return makeRandomFileName() + getExt(fileName);
    }
    public String makeRandomFileName(MultipartFile mf){
        return mf == null || mf.isEmpty() ? null : makeRandomFileName(mf.getOriginalFilename());
    }
    public void transferTo(MultipartFile mf, String target) throws Exception{
        File saveFile = new File(uploadPath, target);
        mf.transferTo(saveFile);
    }
    //폴더 삭제
    public void deleteFolder(String absoluteFolderPath){
        File folder = new File(absoluteFolderPath); //D:\download\greengram_ver2\\user\?
        if(folder.exists() && folder.isDirectory()){ //폴더인지아닌지
            File[] files = folder.listFiles(); //해당하는 폴더에 해당하는 폴더 파일을 배열형태로 리턴

            for(File f : files){
                if(f.isDirectory()){
                    deleteFolder(f.getAbsolutePath()); //재귀호출
                } else {
                    f.delete();
                }
            }
            folder.delete();
        }
    }
}
