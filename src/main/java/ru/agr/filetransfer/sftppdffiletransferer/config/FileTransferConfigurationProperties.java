package ru.agr.filetransfer.sftppdffiletransferer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file-transfer")
public class FileTransferConfigurationProperties {
    private String sourceFolder;
    private String destinationFolder;
    private Boolean destinationChangeFilename;
    private String destinationFilename;
    private String destinationFileFormat;
    private Boolean destinationFilenameDatePostfix;
}
