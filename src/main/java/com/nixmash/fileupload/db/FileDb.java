package com.nixmash.fileupload.db;

import com.google.inject.ImplementedBy;
import com.nixmash.fileupload.dto.PostImage;

import java.sql.SQLException;

@ImplementedBy(FileDbImpl.class)
public interface FileDb {
    PostImage addPostImage(PostImage postImage) throws SQLException;

    PostImage getPostImageById(Long image_id) throws SQLException;
}
