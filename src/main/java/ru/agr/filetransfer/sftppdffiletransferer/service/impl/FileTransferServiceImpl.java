package ru.agr.filetransfer.sftppdffiletransferer.service.impl;

import com.jcraft.jsch.ChannelSftp;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.agr.filetransfer.sftppdffiletransferer.config.FileTransferConfigurationProperties;
import ru.agr.filetransfer.sftppdffiletransferer.service.FileTransferService;
import ru.agr.filetransfer.sftppdffiletransferer.service.SftpService;
import ru.agr.filetransfer.sftppdffiletransferer.service.exception.FileTransferException;
import ru.agr.filetransfer.sftppdffiletransferer.service.exception.SFTPException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileTransferServiceImpl implements FileTransferService {
    private final SftpService<ChannelSftp> sftpService;
    private final FileTransferConfigurationProperties properties;

    @Override
    @SneakyThrows
    @Scheduled(cron = "${file-transfer.cron-expression}")
    public void startTransfer() {
        if (!validateProperties()) {
            log.info("Couldn't start file transfer at {} :( please correct properties and restart application.", LocalDateTime.now());
            return;
        }

        log.info("Starting file transfer: {}", LocalDateTime.now());
        try {
            var connection = sftpService.connect();
            log.info("Getting list of files in the source directory: {}", properties.getSourceFolder());
            try (Stream<Path> stream = Files.list(Paths.get(properties.getSourceFolder()))) {
                var fileNamesMap = stream.filter(file -> !Files.isDirectory(file))
                        .collect(Collectors.toMap(path -> path.getFileName().toString(), Function.identity()));
                log.info("Found files in the source directory: {}", fileNamesMap);
                for (Map.Entry<String, Path> entry : fileNamesMap.entrySet()) {
                    var destinationFilename = new File(new File(properties.getDestinationFolder()), entry.getKey());
                    Path path = entry.getValue();
                    if (properties.getDestinationChangeFilename() == Boolean.TRUE) {
                        destinationFilename = new File(new File(properties.getDestinationFolder()), properties.getDestinationFilename() + properties.getDestinationFileFormat());
                    }
                    sftpService.uploadFile(connection, path.toString(), destinationFilename.getPath());
                }
            } catch (IOException e) {
                throw new FileTransferException(e);
            }
            sftpService.disconnect(connection);
            log.info("Successfully :) finished file transfer: {}", LocalDateTime.now());
        } catch (SFTPException | FileTransferException e) {
            log.error("File transfer finished with ERROR :( message: {}", e.getMessage(), e);
            throw e;
        }
    }

    private boolean validateProperties() {
        return true;
    }
}
