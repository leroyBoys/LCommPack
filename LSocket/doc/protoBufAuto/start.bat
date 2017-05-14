@echo off

set rootPath=%~dp0
::协议文件路径, 最后不要跟“\”符号
set protoFile=proto
set SOURCE_FOLDER=%rootPath%%protoFile%\

::C#编译器路径
set CS_COMPILER_PATH=%rootPath%protobuf-net\ProtoGen\protogen.exe
::C#文件生成路径, 最后不要跟“\”符号
set CS_TARGET_PATH=%SOURCE_FOLDER%cs

::Java编译器路径
set JAVA_COMPILER_PATH=%rootPath%protobuf-net\protoc.exe
::Java文件生成路径, 最后不要跟“\”符号
set JAVA_TARGET_PATH=%SOURCE_FOLDER%java

set JAVA_BAIDU_PATH=%rootPath%protobuf-net\protocbaidu.exe
set JAVA_BAIDU_TARGET_PATH=%SOURCE_FOLDER%annotation\

IF NOT EXIST %SOURCE_FOLDER% (
	echo create %SOURCE_FOLDER%         
	md %SOURCE_FOLDER%  
)

IF NOT EXIST %CS_TARGET_PATH% (
	echo create %CS_TARGET_PATH%         
	md %CS_TARGET_PATH%  
)

IF NOT EXIST %JAVA_TARGET_PATH% (
	echo create %JAVA_TARGET_PATH%         
	md %JAVA_TARGET_PATH%  
)

IF NOT EXIST %JAVA_BAIDU_TARGET_PATH% (
	echo create %JAVA_BAIDU_TARGET_PATH%         
	md %JAVA_BAIDU_TARGET_PATH%  
)

::删除之前创建的文件
del %CS_TARGET_PATH%\*.* /f /s /q
del %JAVA_TARGET_PATH%\*.* /f /s /q
del %JAVA_BAIDU_TARGET_PATH%\*.* /f /s /q

::遍历所有文件
for /f "delims=" %%i in ('dir /b "%SOURCE_FOLDER%*.proto"') do (

	echo "from"%%i
    ::生成 C# 代码
    echo "     create"%CS_TARGET_PATH%\%%~ni.cs
    %CS_COMPILER_PATH% -i:%SOURCE_FOLDER%%%i -o:%CS_TARGET_PATH%\%%~ni.cs
    
    ::生成 Java 代码
    echo "     create"%JAVA_TARGET_PATH%
    %JAVA_COMPILER_PATH% --java_out=%JAVA_TARGET_PATH% %protoFile%\%%i
    
	echo "     create"%JAVA_BAIDU_TARGET_PATH%
    %JAVA_BAIDU_PATH% %protoFile%\%%i %JAVA_BAIDU_TARGET_PATH%
)

echo 协议生成完毕。

pause