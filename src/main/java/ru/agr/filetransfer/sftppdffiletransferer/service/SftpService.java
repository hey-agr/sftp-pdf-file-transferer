package ru.agr.filetransfer.sftppdffiletransferer.service;

import lombok.NonNull;
import ru.agr.filetransfer.sftppdffiletransferer.service.exception.SFTPException;

public interface SftpService<T> {
    T connect() throws SFTPException;
    void disconnect(@NonNull T connection) throws SFTPException;
    void uploadFile(@NonNull T connection, @NonNull String sourcePath, @NonNull String destinationPath) throws SFTPException;
}
