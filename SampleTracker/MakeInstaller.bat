echo off
echo This will take some time. Wait for the program to finish
echo
echo on
jpackage --type msi --app-version "1.2.8" --input ".\target\artifacts\SampleTracker_jar" --dest . --main-jar ".\SampleTracker.jar" --main-class org.openjfx.Launcher --win-shortcut --win-menu --icon ".\src\main\resources\TrackerIcon.ico" --name "Tracker" --jlink-options --bind-services

pause