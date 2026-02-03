package com.fcs.ftpservice.controller;

import com.fcs.ftpservice.service.FtpService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ftp")
@CrossOrigin(origins = "*")
public class FtpController {

    private final FtpService ftpService;

    public FtpController(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @GetMapping("/directories")
    public List<String> listDirectories() throws Exception {
        return ftpService.listDirectories();
    }

    @PostMapping("/download")
    public String download(@RequestBody List<String> dirs) throws Exception {
        for (String dir : dirs) {
            ftpService.downloadDirectory(dir);
        }
        return "Download completed";
    }
}
