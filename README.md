NixMash File Upload - Downloads with Bootique Jersey
==========================

This NixMash app demonstrates File Uploading and Downloading with Jersey, specifically with the Bootique Jersey Module.

&nbsp; | &nbsp; 
| --- | --- | 
| *Single File Uploads* | *Multiple File Uploads* | 
| *Multiple File Uploads with the BlueImp jQuery File Upload Plugin* | *File Downloads with .TXT, .JPG, ODT, TAR.GZ, and PDF files* |
| *Secure File Downloads w/ Multiple Roles* ||

## BlueImp jQuery File Upload Plugin Integration with Jersey ##

![BlueImp Jersey Integration](http://nixmash.com/x/pics/github/jersey-blueimp.png)

## Installation ## 

1. BlueImp File Uploading stores file data to a MySQL database, so create a MySQL database and configure the connection in `/resources/bootique.yml.`
2. Run the MySQL Setup Script located in `/install/sql/setup.sql.`
3. You'll notice a `/install/config` directory. This is your *EXTERNAL CONFIGURATION* directory which must be located under your `/home/username` directory. Copy the folder and its contents and enter the location in `/resources/fileuploader.properties.`

Here's a sample configuration: If your username was **billy** and you copied the `/config` folder to `/home/billy/fileuploader` then the properties in `fileuploader.properties` would read as follows, because the properties path indicates a subdirectory path to your user home directory.

```properties
global.properties.file=/fileuploader/global.properties
config.path=/fileuploader
```

4. Update the external `global.properties` file. If, again, your username was **billy** and you copied the `/config` folder to `/home/billy/fileuploader` the properties in your external `global.properties` file would read as shown below, since we are entering *the full filepath* here.

```properties
file.upload.path=/home/billy/fileuploader/files/
thumbnail.upload.path=/home/billy/fileuploader/thumbnails/
downloads.path=/home/billy/fileuploader/downloads/
```


