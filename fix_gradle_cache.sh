#!/bin/bash
echo "========================================================"
echo "      JARVIS ANDROID - FIX CORRUPTED CACHE (Linux/Mac)"
echo "========================================================"
echo ""
echo "This script will attempt to clean the corrupted Gradle cache."
echo "WARNING: This will force Gradle to redownload dependencies next time."
echo ""
read -p "Press [Enter] to continue..."

echo ""
echo "1. Stopping Gradle Daemons..."
./gradlew --stop

echo ""
echo "2. Deleting Corrupted Caches..."
echo "   Target: ~/.gradle/caches"
rm -rf ~/.gradle/caches
echo "   Cache deleted."

echo ""
echo "3. Cleaning Project Build..."
./gradlew clean

echo ""
echo "========================================================"
echo "                 DONE!"
echo "========================================================"
echo "Try running the project again in Android Studio."
echo ""
