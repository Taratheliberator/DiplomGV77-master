package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.example.exception.InputDataException;
import org.example.exception.UnauthorizedException;
import org.example.repository.AuthenticationRepository;
import org.example.repository.StorageFileRepository;
import org.example.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.example.TestData.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StorageFileServiceTest {

    @InjectMocks
    private StorageFileService storageFileService;

    @Mock
    private StorageFileRepository storageFileRepository;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(authenticationRepository.getUsernameByToken(BEARER_TOKEN_SPLIT)).thenReturn(USERNAME_1);
        Mockito.when(userRepository.findByUsername(USERNAME_1)).thenReturn(USER_1);
    }

    @Test
    void uploadFile() {
        assertTrue(storageFileService.uploadFile(BEARER_TOKEN, FILENAME_1, MULTIPART_FILE));
    }

    @Test
    void uploadFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> storageFileService.uploadFile(TOKEN_1, FILENAME_1, MULTIPART_FILE));
    }

    @Test
    void deleteFile() {
        storageFileService.deleteFile(BEARER_TOKEN, FILENAME_1);
        Mockito.verify(storageFileRepository, Mockito.times(1)).deleteByUserAndFilename(USER_1, FILENAME_1);
    }

    @Test
    void deleteFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> storageFileService.deleteFile(TOKEN_1, FILENAME_1));
    }

    @Test
    void deleteFileInputDataException() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertThrows(InputDataException.class, () -> storageFileService.deleteFile(BEARER_TOKEN, FILENAME_1));
    }

    @Test
    void downloadFile() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertEquals(FILE_CONTENT_1, storageFileService.downloadFile(BEARER_TOKEN, FILENAME_1));
    }

    @Test
    void downloadFileUnauthorized() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertThrows(UnauthorizedException.class, () -> storageFileService.downloadFile(TOKEN_1, FILENAME_1));
    }

    @Test
    void downloadFileInputDataException() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertThrows(InputDataException.class, () -> storageFileService.downloadFile(BEARER_TOKEN, FILENAME_2));
    }

    @Test
    void editFileName() {
        storageFileService.editFileName(BEARER_TOKEN, FILENAME_1, EDIT_FILE_NAME_RQ);
        Mockito.verify(storageFileRepository, Mockito.times(1)).editFileNameByUser(USER_1, FILENAME_1, NEW_FILENAME);
    }

    @Test
    void editFileNameUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> storageFileService.editFileName(TOKEN_1, FILENAME_1, EDIT_FILE_NAME_RQ));
    }

    @Test
    void editFileNameInputDataException() {
        Mockito.when(storageFileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(STORAGE_FILE_1);
        assertThrows(InputDataException.class, () -> storageFileService.deleteFile(BEARER_TOKEN, FILENAME_1));
    }

    @Test
    void getAllFiles() {
        Mockito.when(storageFileRepository.findAllByUser(USER_1)).thenReturn(STORAGE_FILE_LIST);
        assertEquals(FILE_RS_LIST, storageFileService.getAllFiles(BEARER_TOKEN, LIMIT));
    }

    @Test
    void getAllFilesUnauthorized() {
        Mockito.when(storageFileRepository.findAllByUser(USER_1)).thenReturn(STORAGE_FILE_LIST);
        assertThrows(UnauthorizedException.class, () -> storageFileService.getAllFiles(TOKEN_1, LIMIT));
    }
}