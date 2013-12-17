#!/usr/bin/env bash

deploy() {
    local git_mvn_repo_dir="../mvn-repo"

    # Check
    if [ ! -e $git_mvn_repo_dir ]; then
        echo "Not found: $git_mvn_repo_dir"
        exit 1
    fi

    # Choose
    if grep '<version' pom.xml | head -n 1 | grep -iq snapshot; then
        local group="snapshots"

        # Remove old snapshots
        rm -r $git_mvn_repo_dir/$group
    else
        local group="releases"
    fi

    # Deploy
    mvn -DaltDeploymentRepository=git-$group::default::file:$git_mvn_repo_dir/$group deploy
    
}

######

deploy
