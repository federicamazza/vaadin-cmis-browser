# Project Structure
This is a Maven project with two modules:
- **cmis-client**: implements a simple CMIS client that builds upon [Apache Chemistry OpenCMIS](http://chemistry.apache.org/java/opencmis.html) to provide a far less granular interface to a CMIS repository
- **vaadin-ui**: implements the web interface of the application with [vaadin](https://vaadin.com/)

# Development guide

## Setup
1. Fork this repo

1. Clone your fork
```sh
$ git clone https://github.com/username/vaadin-cmis-browser.git --single-branch
```

1. Create your personal branch for development
```sh
$ cd vaadin-cmis-browser
$ git checkout -b username
```

1. Configure remotes
```sh
$ git remote add upstream https://github.com/atave/vaadin-cmis-browser.git
```

1. Check if Maven can fetch all dependencies by running the web application
```sh
$ cd vaadin-ui
$ mvn jetty:run
```

1. If jetty starts, check out in your browser the following addresses: [1](http://localhost:8080) and [2](http://localhost:8080/opencmis-inmemory/)


## Workflow
1. Write your code

1. Test it

1. Commit your changes to your personal branch or to a topic branch

1. When you feel like it, [open a Pull Request](https://help.github.com/articles/using-pull-requests)

1. Wait for me to merge those changes into *master*

1. When you can, merge those changes back
```sh
$ git checkout master
$ git fetch upstream/master
$ git merge upstream/master
```

## CmisClient API Tutorial

### Instantiation
```java
CmisClient client;
try {
    client = new OpenCmisInMemoryClient(user, password);
} catch(CmisBaseException e) {
    // Wrong username and password
}
```
This snippet instantiates a CmisClient implementation for the bundled OpenCMIS InMemory Server.  
Upon creation, the client automatically connects to the first available repository on the CMIS Server.  
Since both the bundled OpenCMIS InMemory Server and Alfresco Community 4.2 provide only a single repository, there's no need to manually call methods like `#getRepositories()` and `#connect(String)`.

### Navigation
```java
// Get the current folder
FolderView currentFolder = client.getCurrentFolder();

// Get the current path
String currentPath = currentFolder.getPath();

// List folders in the current directory
<...> = currentFolder.getFolders();

// Change the current directory
client.navigateTo("subdirectory");

// Back to the parent
client.navigateTo("..");

// A deep dive
client.navigateTo("/path/to/deep/folder");
```

### Folder creation and deletion
```java
FolderView createdFolder;

// Create folder in the current directory
createdFolder = client.createFolder("mysubdir");

// Create folder somewhere else
createdFolder = client.createFolder("/path/to/parent", "subfolderName");

// Time to delete stuff
client.deleteFolder(createdFolder);

// Also
client.deleteFolder("/path/to/some/other/folder");
```

### Document upload (vaadin example)
```java
DocumentUploader receiver = new DocumentUploader(client, "/path/to/parent") {
    @Override
    protected onCmisUploadReceived(DocumentView uploadedDocument) {
       // do something (if anything)
    }
};
    
Upload upload = new Upload("Upload Document", receiver);

// ...

// In an event listener of some sort, before the upload starts, set additional info
receiver.setFileName("readme.txt");
receiver.setCheckInComment("awesome stuff!");
receiver.setVersioningState(VersioningState.MAJOR);

receiver.setProperty(PropertyIds.DESCRIPTION, "a great document description");
receiver.setProperty("my:property1", "value1");
receiver.setProperty("my:property2", "value2");
```

### Document download (vaadin example)
```java
// Get the document
DocumentView document = client.getDocument("/path/to/document");

// Make sure it's the correct version
String requestedVersion = <...>
document = document.getObjectOfVersion(requestedVersion);

// Create a stream resource
StreamResource.StreamSource source = new DocumentDownloader(document);
StreamResource resource = new StreamResource(source, document.getName());

// Let the user download the document
Link downloadLink = new Link("Download", resource);
```

### Document deletion
```java
// Delete all versions of a document
client.deleteDocument("/path/of/document/to/nuke");

// Also
client.deleteDocument(document);

// Delete a single version of a document
String versionToDelete = <...>
client.deleteDocument("/path/to/document", versionToDelete);

// Also
client.deleteDocument(document, versionToDelete);
```

### Document search
*Work in progress*


## Notes
- The *mvn-repo* branch is an embedded Maven repository used to distribute **cmis-client** updates as soon as they are ready.  
If a build fails, try to run it again like so: `mvn -U clean your_goals`.
