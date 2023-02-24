package com.example.board.web.util;

import com.example.board.web.model.AttachFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.*;

@Component
public class FileStore {

    /**
     * 첨부 파일을 스토리지에 업로드
     * @param id 게시글 id
     * @param multipartFiles 파일 폼 데이터
     * @return 업로드된 파일 정보 리스트
     */
    public List<AttachFile> uploadFiles(int id, List<MultipartFile> multipartFiles) {
        List<AttachFile> files = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles){
            if(!multipartFile.isEmpty() && multipartFile.getSize() > 0){
                String origFileName = multipartFile.getOriginalFilename();
                String extension = extracted(origFileName);
                String storeFileName = createStoreFileName(extension);
                String storeDir = getStoragePath() + File.separator;

                File saveFile = new File(storeDir, storeFileName);
                try {
                    multipartFile.transferTo(saveFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                AttachFile attachFile = AttachFile.builder()
                        .postId(id)
                        .origName(origFileName)
                        .storeName(storeFileName)
                        .ext(extension)
                        .storeDir(storeDir)
                        .build();
                files.add(attachFile);
            }
        }
        return files;
    }


    /**
     * 스토리지에서 파일을 읽어 response body에 출력
     * @param attachFile 다운로드할 파일 정보
     * @param response HTTP 응답 정보
     */
    public void downloadFile(AttachFile attachFile, HttpServletResponse response){
        File file = new File(attachFile.getStoreDir(), attachFile.getStoreName());

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String origName = attachFile.getOrigName();
        try {
            origName = new String(origName.getBytes("utf-8"), "8859_1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Content-disposition", "attachment; fileName=" + origName);

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            byte[] buffer = new byte[1024 * 8];
            while(true){
                int count = fis.read(buffer);
                if(count == -1){
                    break;
                }
                os.write(buffer, 0, count);
            }
            os.flush();

            os.close();
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 프로퍼티 파일 읽어 스토리지 경로 반환
     * @return 스토리지 경로
     */
    private String getStoragePath() {
        URL resource = FileStore.class.getClassLoader().getResource("application.properties");
        Properties properties = new Properties();
        try {
            properties.load(resource.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("file.storage");
    }

    /**
     * 스토리지 파일 삭제
     * @param attachFiles 파일 정보 리스트
     */
    public void deleteFiles(List<AttachFile> attachFiles){
        for(AttachFile attachFile : attachFiles){
            File file = new File(attachFile.getStoreDir() + attachFile.getStoreName());
            if(file.exists()){
                file.delete();
            }
        }
    }

    /**
     * 스토리지에 저장될 이름 생성
     * @param ext 확장자
     * @return 파일 이름
     */
    private String createStoreFileName(String ext){
        return UUID.randomUUID() + "." + ext;
    }

    /**
     * 파일 이름에서 확장자 추출
     * @param fileName 파일 이름
     * @return 확장자
     */
    private String extracted(String fileName){
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos+1);
    }
}
