@echo off

set rootPath=%~dp0
::Э���ļ�·��, ���Ҫ����\������
set protoFile=proto
set SOURCE_FOLDER=%rootPath%%protoFile%\

::C#������·��
set CS_COMPILER_PATH=%rootPath%protobuf-net\ProtoGen\protogen.exe
::C#�ļ�����·��, ���Ҫ����\������
set CS_TARGET_PATH=%SOURCE_FOLDER%cs

::Java������·��
set JAVA_COMPILER_PATH=%rootPath%protobuf-net\protoc.exe
::Java�ļ�����·��, ���Ҫ����\������
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

::ɾ��֮ǰ�������ļ�
del %CS_TARGET_PATH%\*.* /f /s /q
del %JAVA_TARGET_PATH%\*.* /f /s /q
del %JAVA_BAIDU_TARGET_PATH%\*.* /f /s /q

::���������ļ�
for /f "delims=" %%i in ('dir /b "%SOURCE_FOLDER%*.proto"') do (

	echo "from"%%i
    ::���� C# ����
    echo "     create"%CS_TARGET_PATH%\%%~ni.cs
    %CS_COMPILER_PATH% -i:%SOURCE_FOLDER%%%i -o:%CS_TARGET_PATH%\%%~ni.cs
    
    ::���� Java ����
    echo "     create"%JAVA_TARGET_PATH%
    %JAVA_COMPILER_PATH% --java_out=%JAVA_TARGET_PATH% %protoFile%\%%i
    
	echo "     create"%JAVA_BAIDU_TARGET_PATH%
    %JAVA_BAIDU_PATH% %protoFile%\%%i %JAVA_BAIDU_TARGET_PATH%
)

echo Э��������ϡ�

pause