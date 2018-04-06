package com.nixmash.fileupload.service;

import com.google.inject.Inject;
import com.nixmash.fileupload.db.FileDb;
import com.nixmash.fileupload.dto.PostImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    // region Constructor

    private FileDb fileDb;

    @Inject
    public FileServiceImpl(FileDb fileDb) {
        this.fileDb = fileDb;
    }

    // endregion


    @Override
    public PostImage addPostImage(PostImage postImage) {
        try {
            return fileDb.addPostImage(postImage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PostImage getPostImage(Long image_id) {
        try {
            return fileDb.getPostImageById(image_id);
        } catch (SQLException e) {
            logger.error("Exception retrieving PostImage By Id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deletePostImage(Long image_id) {
        try {
            fileDb.deletePostImageById(image_id);
        } catch (SQLException e) {
            logger.error("Exception deleting PostImage By Id: " + e.getMessage());
        }
    }
}
