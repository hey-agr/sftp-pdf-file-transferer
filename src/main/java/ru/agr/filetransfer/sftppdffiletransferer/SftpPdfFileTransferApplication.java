package ru.agr.filetransfer.sftppdffiletransferer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
public class SftpPdfFileTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(SftpPdfFileTransferApplication.class, args);
	}

}
