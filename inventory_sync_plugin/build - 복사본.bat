@echo off
chcp 65001 > nul
setlocal enabledelayedexpansion

echo ==========================================
echo InventorySync 플러그인 자동 빌드 스크립트
echo ==========================================
echo.

REM Java 확인
echo [1/4] Java 확인 중...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java가 설치되지 않았습니다!
    echo 다운로드: https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)
echo ✅ Java 설치됨
echo.

REM Maven 확인 및 설치
echo [2/4] Maven 확인 중...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ⚠️  Maven이 설치되지 않았습니다. 설치 중...
    echo.
    
    REM Chocolatey 확인
    choco --version >nul 2>&1
    if %errorlevel% equ 0 (
        echo Chocolatey로 Maven 설치 중...
        choco install maven -y
        call refreshenv
    ) else (
        echo ❌ Chocolatey가 필요합니다!
        echo 다음을 관리자 권한으로 실행하세요:
        echo powershell -Command "iex ((New-Object System.Net.ServicePointManager).SecurityProtocol = 3072); iex (New-Object Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1')"
        echo.
        pause
        exit /b 1
    )
)
echo ✅ Maven 설치됨
echo.

REM 빌드 시작
echo [3/4] 플러그인 빌드 중...
echo (이 과정은 1~5분 걸릴 수 있습니다)
echo.

cd /d "%~dp0"
mvn clean package

if %errorlevel% neq 0 (
    echo.
    echo ❌ 빌드 실패!
    echo 에러 메시지를 확인하세요.
    echo.
    pause
    exit /b 1
)

echo.
echo [4/4] 빌드 완료!
echo.

REM JAR 파일 확인
if exist "target\InventorySync-1.0.0.jar" (
    echo ✅ JAR 파일 생성 완료!
    echo.
    echo 파일 위치:
    echo %cd%\target\InventorySync-1.0.0.jar
    echo.
    echo 다음 단계:
    echo 1. 페더클라이언트 서버 폴더 열기
    echo 2. plugins 폴더에 위의 JAR 파일 복붙
    echo 3. 서버 시작
    echo.
) else (
    echo ❌ JAR 파일을 찾을 수 없습니다!
    echo.
)

pause
