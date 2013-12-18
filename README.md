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

## Notes
- The *mvn-repo* branch is an embedded Maven repository used to distribute **cmis-client** updates as soon as they are ready.  
If a build fails, try to run it again like so: `mvn -U clean your_goals`.
