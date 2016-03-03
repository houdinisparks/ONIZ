# ONIZ - Oh No Its Zombies - LibGDX Multiplayer Game

### GIT WORK FLOW

##### Setup

1. On Github, fork the orignal repository to create your own copy.
2. Go to your forked repository page and clone it to your local environment (e.g. Desktop).
```sh
$ git clone https://github.com/esmondchuah/ONIZ.git
```
3. Go into your local clone of your forked repository.
```sh
$ cd ONIZ
```
4. Run the command below and you should see that you have exactly one remote called *origin*. ("Remotes" are like nicknames for the URLs of repositories - *origin* is one, for example.) 
```sh
$ git remote
```
5. Add the original Github repository as a new remote and call it *upstream*. (Then try "git remote" again, you should now see two remotes listed.)
```sh
$ git remote add upstream https://github.com/robin-lee/ONIZ.git
```

##### Commit and update your own Github Repo
1. Add your updated files to the staging area.
```sh
$ git add GameScreen.java
```
Alternatively, you could just "add all" if you have multiple updated files.
```sh
$ git add -A
```
2. Commit your updated files.
```sh
$ git commit -m "Add your commit message here"
```
3. Push it to your own forked repository on GitHub.
```sh
git push origin master
```

##### When there's a new update on the original Github Repo
1. Stage and commit your own updated code first.
```sh
$ git add -A
$ git commit -m "Commit your local updates first"
```
2. Fetch all the branches from that upstream repository into remote-tracking branches.
```sh
$ git fetch upstream
```
3. Make sure that you're on your master branch.
```sh
$ git checkout master
```
4. Rewrite your master branch so that any commits of yours that aren't already in upstream/master are replayed on top of that other branch.
```sh
$ git rebase upstream/master
```
5. Push it to your own forked repository on GitHub.
```sh
git push origin master
```

##### Submit a pull request
1. **(IMPORTANT)** Make sure you have the lastest updates from the original Github repository in your forked Github repository (by doing the above).
2. Click on the "Pull request" button (right under the "Download ZIP" button).
3. You should see: "base fork: *robin-lee/ONIZ*, base: *master*; head fork: *esmondchuah/ONIZ*, compare: *master*".
4. Confirm all the code changes you are making and click on the "Create pull request" button.