package com.nixmash.fileupload.service;

import com.google.inject.ImplementedBy;
import com.nixmash.fileupload.dto.PostImage;

@ImplementedBy(FileServiceImpl.class)
public interface FileService {
    PostImage addPostImage(PostImage postImage);

    PostImage getPostImage(Long image_id);
}
