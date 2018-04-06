package com.nixmash.fileupload.db;

import com.google.inject.Inject;
import com.nixmash.fileupload.core.WebConfig;
import com.nixmash.fileupload.dto.PostImage;
import io.bootique.jdbc.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FileDbImpl implements FileDb {

    private static final Logger logger = LoggerFactory.getLogger(FileDbImpl.class);


    private DataSourceFactory dataSource;
    private WebConfig webConfig;

    @Inject
    public FileDbImpl(DataSourceFactory dataSource, WebConfig webConfig) {
        this.dataSource = dataSource;
        this.webConfig = webConfig;
    }

    @Override
    public PostImage addPostImage(PostImage postImage) throws SQLException {
        {
            try (Connection connection = dataSource.forName(webConfig.datasourceName).getConnection()) {

                try (Statement statement = connection.createStatement()) {
                    String sql = "INSERT INTO images (post_id, image_name, thumbnail_filename, filename, content_type, size, thumbnail_size) VALUES (" +
                            postImage.getPostId() + "," +
                            "'" + postImage.getName() + "'," +
                            "'" + postImage.getThumbnailFilename() + "'," +
                            "'" + postImage.getNewFilename() + "'," +
                            "'" + postImage.getContentType() + "'," +
                            postImage.getSize() + "," +
                            postImage.getThumbnailSize() +
                            ")";
                    int result = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                    if (result == 1) {
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            postImage.setId(generatedKeys.getLong(1));
                        }
                    }
                }
            } catch (SQLException e) {
                logger.info("Error creating new PostImage: " + e.getMessage());
            }
            return postImage;
        }
    }


    @Override
    public PostImage getPostImageById(Long image_id)  throws SQLException {
        PostImage postImage = new PostImage();
        try (Connection cn = dataSource.forName(webConfig.datasourceName).getConnection()) {

            try (Statement statement = cn.createStatement()) {
                String getUserSql = "SELECT * FROM images WHERE image_id = " + image_id;
                ResultSet rs = statement.executeQuery(getUserSql);
                while (rs.next()) {
                    populatePostImage(rs, postImage);
                }
                rs.close();
                cn.close();
            }
        } catch (SQLException e) {
            logger.info("Error retrieving postImage: " + e.getMessage());
        }
        return postImage;
    }

    @Override
    public void deletePostImageById(Long image_id)  throws SQLException {
        PostImage postImage = new PostImage();
        try (Connection cn = dataSource.forName(webConfig.datasourceName).getConnection()) {

            try (Statement statement = cn.createStatement()) {
                String sql = "DELETE FROM images WHERE image_id = " + image_id;
                statement.execute(sql);
                statement.close();
                cn.close();
            }
        } catch (SQLException e) {
            logger.info("Error deleting postImage: " + e.getMessage());
        }
    }

    private void populatePostImage(ResultSet rs, PostImage postImage) throws SQLException {
        postImage.setId(rs.getLong("image_id"));
        postImage.setPostId(rs.getLong("post_id"));
        postImage.setName(rs.getString("image_name"));
        postImage.setThumbnailFilename(rs.getString("thumbnail_filename"));
        postImage.setNewFilename(rs.getString("filename"));
        postImage.setContentType(rs.getString("content_type"));
        postImage.setSize(rs.getLong("size"));
        postImage.setThumbnailSize(rs.getLong("thumbnail_size"));
        postImage.setDateCreated(rs.getTimestamp("datetime_created"));

    }
}
