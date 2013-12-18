# Project Structure
This is a Maven project with two modules:
- **cmis-client**: implements a simple CMIS client that builds upon [Apache Chemistry OpenCMIS](http://chemistry.apache.org/java/opencmis.html) to provide a far less granular interface to a CMIS repository
- **vaadin-ui**: implements the web interface of the application with [vaadin](https://vaadin.com/).

# Development guide

## Setup
1. Fork this repo

1. Clone your fork
```sh
$ git clone https://github.com/&lt;username&gt;/vaadin-cmis-browser.git --single-branch
```

1. Create your personal branch for development
```sh
$ cd vaadin-cmis-browser
$ git checkout -b <b>username</b>
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

1. If jetty starts you can press [CTRL + C] to stop it.
   Otherwise, something went wrong.

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

## Notes
- the *mvn-repo* branch is an embedded Maven repository used to distribute **cmis-client** updates as soon as they are ready in order to not having to manually run `mvn install` every time that module is updated: ignore it
