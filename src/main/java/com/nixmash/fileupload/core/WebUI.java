package com.nixmash.fileupload.core;

import com.github.mustachejava.functions.TranslateBundleFunction;
import com.google.inject.Inject;
import com.nixmash.fileupload.dto.CurrentUser;
import com.nixmash.fileupload.dto.PageInfo;
import com.nixmash.fileupload.enums.ActiveMenu;
import com.nixmash.fileupload.enums.SidebarMenu;
import com.nixmash.fileupload.service.UserService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import static com.nixmash.fileupload.service.UserServiceImpl.CURRENT_USER;


/**
 * Created by daveburke on 7/1/17.
 */
@SuppressWarnings({"SameParameterValue", "ConstantConditions"})
public class WebUI implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(WebUI.class);

    private static final long serialVersionUID = 9155986213118958079L;
    private static final String NULL_FIELD = "*";
    private static final String UPLOADS_MENU= "uploads";
    private static final String DOWNLOADS_MENU= "downloads";
    private static final String POSTS_SIDE_MENU = "posts";
    private static final String TAGS_SIDE_MENU = "tags";
    private static final String SEARCH_SIDE_MENU = "search";
    private static final String ADMIN_SIDE_MENU = "admin";
    private static final String ADMIN_SIDE_POSTS_MENU = "adminPosts";
    private static final String ADMIN_SIDE_NEW_POST_MENU = "adminNewPost";
    private static final String ADMIN_SIDE_USERS_MENU = "adminUsers";
    private static final String ADMIN_SIDE_SITE_MENU = "adminSite";
    private static final String ADMIN_SIDE_UTILITIES_MENU = "adminUtilities";
    private static final String BUNDLE = "messages";

    // region Constructor

    private final WebContext webContext;
    private final UserService userService;

    @Inject
    public WebUI(WebContext webContext, UserService userService) {
        this.webContext = webContext;
        this.userService = userService;
    }

    // endregion

    // region Resource Bundle

    public TranslateBundleFunction getResourceBundle() {
        Locale locale = LocaleUtils.toLocale(webContext.config().currentLocale);
        return new TranslateBundleFunction(BUNDLE, locale);
    }

    // endregion

    // region PageInfo

    @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
    public Optional<PageInfo> getPageInfo(String pagekey) {
        List<PageInfo> pageInfoList = new ArrayList<>();
        try {
            pageInfoList = loadPageInfoFromCvs();
        } catch (IOException e) {
            logger.error("Exception retrieving pageInfo CVS data: " + e.getMessage());
            return Optional.empty();
        }
/*        List<PageInfo> pageInfoList = (List<PageInfo>) janglesCache.get(pageInfoCacheKey());
        if (pageInfoList == null) {
            try {
                pageInfoList = loadPageInfoFromCvs();
                janglesCache.put(pageInfoCacheKey(), (Serializable) pageInfoList);
            } catch (IOException e) {
                return Optional.empty();
            }
        }*/
        Optional<PageInfo> pageInfo = pageInfoList.stream().filter(p -> p.getPage_key().equals(pagekey)).findFirst();
        return pageInfo;
    }

    private List<PageInfo> loadPageInfoFromCvs() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File("/tmp/pageinfo.cvs");
        FileUtils.copyInputStreamToFile(classLoader.getResourceAsStream("pageinfo.cvs"), file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        Locale locale = LocaleUtils.toLocale(webContext.config().currentLocale);
        String pageTitlePrefix = webContext.config().pageTitlePrefix;
        String line;
        List<PageInfo> pageInfoList = new ArrayList<>();
        int iteration = 0;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            if (iteration == 0) {
                iteration++;
                continue;
            }
            String[] fields = line.split(",");

            int page_id = Integer.parseInt(fields[0]);
            String page_key = fields[1];
            String page_title = String.format("%s : %s", pageTitlePrefix, fields[2]);
            String heading = fields[3].equals(NULL_FIELD) ? null : fields[3];
            String subheading = fields[4].equals(NULL_FIELD) ? null : fields[4];
            String menu = fields[5].equals(NULL_FIELD) ? null : fields[5];
            String sidebar_menu = fields[6].equals(NULL_FIELD) ? null : fields[6];

            PageInfo pageInfo = PageInfo.getBuilder(page_id, page_key, page_title)
                    .heading(heading)
                    .subheading(subheading)
                    .inProductionMode(webContext.globals().inProductionMode)
                    .activeMenu(getActiveMenu(menu))
                    .sidebarMenu(getSidebarMenu(sidebar_menu))
                    .resourceBundle(new TranslateBundleFunction(BUNDLE, locale))
                    .build();

            pageInfoList.add(pageInfo);
        }
        br.close();
        return pageInfoList;
    }

    // endregion

    // region Messages

    public String getMessage(String key, Object... params) {
        return webContext.messages().get(key, params);
    }
    // endregion

    // region CacheKeys
/*

    public String adminTagValuesCacheKey() {
        return String.format("AdminTagValuesCacheKey-%s", janglesGlobals.cloudApplicationId);
    }

    public String homePageRecentPostsCacheKey() {
        return String.format("HomeRecentPostsCacheKey-%s", janglesGlobals.cloudApplicationId);
    }

    public String pageInfoCacheKey() {
        return String.format("PageInfoCacheKey-%s", janglesGlobals.cloudApplicationId);
    }

    public String resourceBundleCacheKey() {
        return String.format("ResourceBundleCacheKey-%s", janglesGlobals.cloudApplicationId);
    }
*/

    // endregion

    // region Private Support and Utility Methods

    private ActiveMenu getActiveMenu(String menu) {
        ActiveMenu activeMenu = new ActiveMenu();
        if (menu == null)
            return activeMenu;
        else {
            switch (menu) {
                case UPLOADS_MENU:
                    activeMenu.setUploadsMenu(true);
                    break;
                case DOWNLOADS_MENU:
                    activeMenu.setDownloadsMenu(true);
                    break;
                default:
                    break;
            }
        }
        return activeMenu;
    }

    private SidebarMenu getSidebarMenu(String sidebar_menu) {
        SidebarMenu sidebarMenu = new SidebarMenu();
        if (sidebar_menu == null)
            return sidebarMenu;
        else {
            switch (sidebar_menu) {
                case TAGS_SIDE_MENU:
                    sidebarMenu.setTagsMenu(true);
                    break;
                case POSTS_SIDE_MENU:
                    sidebarMenu.setPostsMenu(true);
                    break;
                case SEARCH_SIDE_MENU:
                    sidebarMenu.setSearchMenu(true);
                    break;
                case ADMIN_SIDE_MENU:
                    sidebarMenu.setAdminMenu(true);
                    break;
                case ADMIN_SIDE_POSTS_MENU:
                    sidebarMenu.setAdminPostsMenu(true);
                    break;
                case ADMIN_SIDE_NEW_POST_MENU:
                    sidebarMenu.setAdminNewPostMenu(true);
                    break;
                case ADMIN_SIDE_USERS_MENU:
                    sidebarMenu.setAdminUsersMenu(true);
                    break;
                case ADMIN_SIDE_SITE_MENU:
                    sidebarMenu.setAdminSiteMenu(true);
                    break;
                case ADMIN_SIDE_UTILITIES_MENU:
                    sidebarMenu.setAdminUtilitiesMenu(true);
                    break;
                default:
                    break;
            }
        }
        return sidebarMenu;
    }

    // endregion

    // region Error Page

    public Map<String, Object> getBasePageInfo(String pageId) {
        Map<String, Object> model = new HashMap<>();
        model.put("pageinfo", getPageInfo(pageId));

        Session session = SecurityUtils.getSubject().getSession();
        if (SecurityUtils.getSubject().getPrincipals() != null) {
            CurrentUser currentUser =  (CurrentUser) session.getAttribute(CURRENT_USER);
            if (currentUser == null) {
                currentUser = userService.getCurrentUser(SecurityUtils.getSubject());
                session.setAttribute(CURRENT_USER, currentUser);
            }
            model.put("user", currentUser);
        }
        return model;
    }

    // endregion

    // region Global Application Settings

    public String getSiteName() {
        return webContext.globals().siteName;
    }

    public String getBaseUrl() {
        return webContext.globals().baseUrl;
    }

    // endregion

    // region Data Conversion and Support Methods
/*

    public List<String> tagsToTagValues(List<Tag> tags) {
        List<String> tagValues = new ArrayList<>();
        for (Tag tag : tags) {
            tagValues.add(tag.getTagValue());
        }
        return tagValues;
    }

    public PostError validatePost(Post post) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        PostError postError = new PostError();
        for (ConstraintViolation<Post> violation : violations) {
            String errorMsg = violation.getMessage();
            logger.info(errorMsg);
            switch (violation.getPropertyPath().toString()) {
                case POST_TITLE:
                    postError.setPostTitleError(errorMsg);
                    break;
                case POST_CONTENT:
                    postError.setPostContentError(errorMsg);
                    break;
                case TAGS:
                    postError.setPostTagsError(errorMsg);
                    break;
                default:
                    break;
            }
        }
        if (violations.size() > 0) {
            postError.setPostIsValid(false);
        }
        return postError;
    }

    public Post mapToPost(MultivaluedMap<String, String> map) {
        Post post = new Post();
        post.setPostId(Long.valueOf(map.getFirst(POST_ID)));
        post.setPostTitle(map.getFirst(POST_TITLE));
        post.setPostContent(map.getFirst(POST_CONTENT));
        if (map.get(TAGS) != null) {
            post.setTags(map.get(TAGS)
                    .stream()
                    .map(Tag::new)
                    .collect(Collectors.toSet()));
        }
        if (!map.get(POST_LINK).get(0).equals(Strings.EMPTY)) {
            String link = map.getFirst(POST_LINK);
            post.setPostLink(link);
            post.setPostSource(PostUtils.createPostSource(link));
            post.setPostType(PostType.LINK);
        } else {
            post.setPostLink(null);
            post.setPostSource(POST_SOURCE_NA);
            post.setPostType(PostType.POST);
        }
        post.setCategoryId(Long.valueOf(map.getFirst(CATEGORY_ID)));
        post.setPostName(createSlug(post.getPostTitle()));
        post.setIsPublished(map.getFirst(PUBLISH_STATUS).equals("publish"));
        post.setTagList( new ArrayList<Tag>(post.getTags()).stream()
                .map(Tag::getTagValue).collect(Collectors.joining(",")));
        post.setTwitterCardType(TwitterCardType.valueOf(map.getFirst("twitterCardType")));
        return post;
    }

    public String createSlug(String title) {
        Slugify slugify = new Slugify();
        return slugify.slugify(title);
    }
*/

    // endregion

    // region Session

    public void setSessionAttribute(String name, Object value) {
        Session session = SecurityUtils.getSubject().getSession();
        if (value != null) {
            session.setAttribute(name, value);
        }
        else {
            session = SecurityUtils.getSubject().getSession(false);
            if (session != null) {
                session.removeAttribute(name);
            }
        }
    }

    public Object getSessionAttribute(String name) {
        Session session = SecurityUtils.getSubject().getSession(false);
        return (session != null ? session.getAttribute(name) : null);
    }

    // endregion

    // region File Handling

    public void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            OutputStream out;
            int read;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // endregion
}
