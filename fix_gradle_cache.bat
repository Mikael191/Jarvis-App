@echo off
echo ========================================================
echo      JARVIS ANDROID - FIX CORRUPTED CACHE (Windows)
echo ========================================================
echo.
echo This script will attempt to clean the corrupted Gradle cache.
echo WARNING: This will force Gradle to redownload dependencies next time.
echo.
pause

echo.
echo 1. Stopping Gradle Daemons...
call gradlew --stop

echo.
echo 2. Deleting Corrupted Caches...
echo    Target: %USERPROFILE%\.gradle\caches
if exist "%USERPROFILE%\.gradle\caches" (
    rmdir /s /q "%USERPROFILE%\.gradle\caches"
    echo    Cache deleted.
) else (
    echo    Cache folder not found (already clean or different path).
)

echo.
echo 3. Cleaning Project Build...
if exist "gradlew.bat" (
    call gradlew clean
)

echo.
echo ========================================================
echo                 DONE!
echo ========================================================
echo Try running the project again in Android Studio.
echo.
pause
