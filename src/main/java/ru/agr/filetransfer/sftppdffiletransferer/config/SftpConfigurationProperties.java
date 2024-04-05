package ru.agr.filetransfer.sftppdffiletransferer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpConfigurationProperties {
    private String remoteHost;
    private String username;
    private String password;
    private String knownHosts;
}
