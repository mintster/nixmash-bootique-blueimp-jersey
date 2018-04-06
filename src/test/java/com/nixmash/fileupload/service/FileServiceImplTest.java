package com.nixmash.fileupload.service;

import com.nixmash.fileupload.core.TestDbBase;
import com.nixmash.fileupload.dto.PostImage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class FileServiceImplTest extends TestDbBase {

    @Test
    public void addPostImage() {
        PostImage postImage = createPostImage();
        assertNull(postImage.getId());

        postImage = fileService.addPostImage(postImage);
        assertTrue(postImage.getId() > 0);
    }

    @Test
    public void getPostImage() {
        PostImage created = fileService.addPostImage(createPostImage());
        Long id = created.getId();

        PostImage retrieved = fileService.getPostImage(id);
        assertEquals(retrieved.getName(), created.getName());
    }

    private PostImage createPostImage() {
        PostImage postImage = new PostImage();
        postImage.setPostId(-1L);
        postImage.setName("test.png");
        postImage.setNewFilename("test.png");
        postImage.setThumbnailFilename("test.png");
        postImage.setSize(100L);
        postImage.setThumbnailSize(50L);
        postImage.setContentType("image/png");
        return postImage;
    }


}