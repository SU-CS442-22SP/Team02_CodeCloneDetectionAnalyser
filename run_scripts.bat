@echo off
:: This script will run CodeCloneDetectionAnalyser

:: Set the base path and dependencies
set PROJECT_PATH=C:\Users\mertk\Desktop\CS442\Team02_CodeCloneDetectionAnalyser\
set ANTLR4_PATH=C:\Users\mertk\antlr-4.7-complete.jar

:: Set the tool paths
set TOOL_1_NAME=duplicate-code-detection-tool\
set TOOL_2_NAME=SimpleCC\
set TOOL_3_NAME=myCC\

:: Set the dataset file name from the command line arguments
set DATA_SET_NAME=%~1

:: If no valid dataset file is provided, ask for one
:datasetloop
if not defined DATA_SET_NAME (
    set /p "DATA_SET_NAME=Please provide a dataset folder name: "
    goto :datasetloop
) else if not exist "%PROJECT_PATH%%DATA_SET_NAME%" (
    echo Dataset file "%PROJECT_PATH%%DATA_SET_NAME%" does not exist.
    set DATA_SET_NAME=
    goto :datasetloop
)

:: Prompt the user for input
echo.
echo * Press 1 for Duplicate Code Detection - Type1 CC detection
echo * Press 2 for SimpleCC - Type2 CC detection
echo * Press 3 for myCC - Type2 CC detection
echo * Press 4 to exit
echo * Press any other key for all of them 
echo.

set /p "choice=Choice: "

:: Check the user's input
if "%choice%"=="1" (
    :: Run duplicate code detector
    python -W ignore "%PROJECT_PATH%%TOOL_1_NAME%duplicate_code_detection.py" -d "%PROJECT_PATH%%DATA_SET_NAME%"
) else if "%choice%"=="2" (
    :: Run SimpleCC
    java -cp "%ANTLR4_PATH%;%PROJECT_PATH%%TOOL_2_NAME%simplecc.jar" jp.naist.se.simplecc.CloneDetectionMain "%PROJECT_PATH%%TOOL_2_NAME%src"
) else if "%choice%"=="3" (
    :: Compile and run myCC
    exit
) else if "%choice%"=="4" (
    :: Exit the program
    exit
) else (
    :: Run duplicate code detector && SimpleCC
    python -W ignore "%PROJECT_PATH%%TOOL_1_NAME%duplicate_code_detection.py" -d "%PROJECT_PATH%%DATA_SET_NAME%"
    java -cp "%ANTLR4_PATH%;%PROJECT_PATH%%TOOL_2_NAME%simplecc.jar" jp.naist.se.simplecc.CloneDetectionMain "%PROJECT_PATH%%TOOL_2_NAME%src"
)
