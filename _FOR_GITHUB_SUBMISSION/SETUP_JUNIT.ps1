# ================================================================
# VCFS - JUnit 5 Setup Script
# Downloads and configures proper JUnit 5 modules (not standalone jar)
# ================================================================

$LIB_DIR = "$PSScriptRoot\lib"
$MAVEN_URL = "https://repo1.maven.org/maven2"

# JUnit 5 minimal dependencies (proper modular approach - NOT standalone jar)
$JUNIT_JARS = @(
    @{ name = "junit-jupiter-api-5.9.2.jar"; url = "$MAVEN_URL/org/junit/jupiter/junit-jupiter-api/5.9.2/junit-jupiter-api-5.9.2.jar" },
    @{ name = "junit-jupiter-engine-5.9.2.jar"; url = "$MAVEN_URL/org/junit/jupiter/junit-jupiter-engine/5.9.2/junit-jupiter-engine-5.9.2.jar" },
    @{ name = "junit-platform-commons-1.9.2.jar"; url = "$MAVEN_URL/org/junit/platform/junit-platform-commons/1.9.2/junit-platform-commons-1.9.2.jar" },
    @{ name = "junit-platform-engine-1.9.2.jar"; url = "$MAVEN_URL/org/junit/platform/junit-platform-engine/1.9.2/junit-platform-engine-1.9.2.jar" }
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "JUnit 5 Setup - Proper Modular Approach" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Create lib directory if not exists
if (-not (Test-Path $LIB_DIR)) {
    Write-Host "[INFO] Creating lib directory..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $LIB_DIR | Out-Null
}

Write-Host "[INFO] Downloading JUnit 5 modules (proper dependencies, NOT standalone jar)..." -ForegroundColor Yellow
Write-Host ""

$success = 0
$failed = 0

foreach ($jar in $JUNIT_JARS) {
    $target_path = Join-Path $LIB_DIR $jar.name
    
    # Skip if already downloaded
    if (Test-Path $target_path) {
        Write-Host "[OK]  Already exists: $($jar.name)" -ForegroundColor Green
        $success++
        continue
    }
    
    Write-Host "[DL]  Downloading: $($jar.name)" -ForegroundColor Cyan
    
    try {
        (New-Object Net.WebClient).DownloadFile($jar.url, $target_path)
        Write-Host "[OK]  Downloaded: $($jar.name)" -ForegroundColor Green
        $success++
    }
    catch {
        Write-Host "[ERROR] Failed to download $($jar.name)" -ForegroundColor Red
        Write-Host "       URL: $($jar.url)" -ForegroundColor Red
        Write-Host "       Error: $_" -ForegroundColor Red
        $failed++
    }
}

Write-Host ""
Write-Host "[SUMMARY] Downloaded: $success | Failed: $failed" -ForegroundColor Cyan
Write-Host ""

if ($failed -eq 0) {
    Write-Host "[SUCCESS] All JUnit 5 modules downloaded!" -ForegroundColor Green
    Write-Host "[INFO] Location: $LIB_DIR" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "JARs are ready for compilation. Test files can now be compiled with:" -ForegroundColor Cyan
    Write-Host "  javac -cp lib\* -d build\bin src\test\java\vcfs\core\*Test.java" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host "[WARNING] Some downloads failed. Check your internet connection." -ForegroundColor Yellow
    Write-Host "You can manually download from: https://repo1.maven.org/maven2/org/junit/" -ForegroundColor Yellow
}
