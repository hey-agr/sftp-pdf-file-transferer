package ru.agr.filetransfer.sftppdffiletransferer.service.impl;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.agr.filetransfer.sftppdffiletransferer.config.SftpConfigurationProperties;
import ru.agr.filetransfer.sftppdffiletransferer.service.SftpService;
import ru.agr.filetransfer.sftppdffiletransferer.service.exception.SFTPException;

import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class SftpServiceImpl implements SftpService<ChannelSftp> {
    private static final String CHANNEL_TYPE = "sftp";

    private final SftpConfigurationProperties properties;

    @Override
    public ChannelSftp connect() throws SFTPException {
        try {
            log.info("Connecting to SFTP server: {} ...", properties.getRemoteHost());
            JSch jsch = new JSch();
            Session jschSession = jsch.getSession(properties.getUsername(), properties.getRemoteHost());
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);
            jschSession.setPassword(properties.getPassword());
            jschSession.connect();
            var channel = (ChannelSftp) jschSession.openChannel(CHANNEL_TYPE);
            channel.connect();
            log.info("Successfully connected to SFTP Server: {}", properties.getRemoteHost());
            return channel;
        } catch (JSchException e) {
            log.error("Error while connection to SFTP Server :( {}", properties.getRemoteHost(), e);
            throw new SFTPException(e);
        }
    }

    @Override
    public void disconnect(@NonNull ChannelSftp connection) {
        log.info("Disconnecting from SFTP server: {} ...", properties.getRemoteHost());
        connection.exit();
        log.info("Successfully disconnected from SFTP server: {}", properties.getRemoteHost());
    }

    @Override
    public void uploadFile(@NonNull ChannelSftp connection,
                           @NonNull String sourcePath,
                           @NonNull String destinationPath) throws SFTPException {
        try {
            log.info("Uploading file from {} to {} ...", sourcePath, destinationPath);
            connection.put(sourcePath, destinationPath);
            log.info("Successfully uploaded file from {} to {} !", sourcePath, destinationPath);
        } catch (com.jcraft.jsch.SftpException e) {
            throw new SFTPException(e);
        }
    }
}
