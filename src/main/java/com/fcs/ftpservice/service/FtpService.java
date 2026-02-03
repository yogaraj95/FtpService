package com.fcs.ftpservice.service;

import com.fcs.ftpservice.config.FtpProperties;
import org.apache.commons.net.ftp.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FtpService {

    private final FtpProperties props;

    public FtpService(FtpProperties props) {
        this.props = props;
    }

    private FTPClient connect() throws Exception {
        FTPClient ftp = new FTPClient();
        ftp.connect(props.getHost(), props.getPort());
        ftp.login(props.getUsername(), props.getPassword());
        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        return ftp;
    }

    public List<String> listDirectories() throws Exception {
        FTPClient ftp = connect();
        ftp.changeWorkingDirectory(props.getBasePath());

        List<String> dirs = new ArrayList<>();
        for (FTPFile f : ftp.listFiles()) {
            if (f.isDirectory()) {
                dirs.add(f.getName());
            }
        }

        ftp.logout();
        ftp.disconnect();
        return dirs;
    }

    public void downloadDirectory(String dirName) throws Exception {
        FTPClient ftp = connect();

        File localDir = new File(props.getLocalTempDir(), dirName);
        localDir.mkdirs();

        ftp.changeWorkingDirectory(props.getBasePath() + "/" + dirName);

        for (FTPFile f : ftp.listFiles()) {
            if (f.isFile()) {
                File file = new File(localDir, f.getName());
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ftp.retrieveFile(f.getName(), fos);
                }
            }
        }

        ftp.logout();
        ftp.disconnect();
    }
}
