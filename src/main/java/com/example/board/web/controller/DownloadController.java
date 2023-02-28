package com.example.board.web.controller;

import com.example.board.web.model.*;
import com.example.board.web.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/free/")
public class DownloadController {

    private final BoardService boardService;

    @PostMapping("/download.do")
    public void downloadFile(@RequestParam int fileId, HttpServletResponse response){
        AttachFile attachFile = boardService.findFileById(fileId);
        
        if(attachFile != null){
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



}
