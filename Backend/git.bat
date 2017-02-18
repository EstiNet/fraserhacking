@ECHO OFF
REM GitRTC Windows Git Syncer thing
ECHO Starting git sync routine.
CD C:\GitRTC\test
ECHO Changed directory to repository.
ECHO Starting git sync...
git add .
git commit -m "GitRTC auto commit"
git push
ECHO Done git sync routine
