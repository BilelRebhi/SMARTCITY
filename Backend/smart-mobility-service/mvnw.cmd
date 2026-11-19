<# : batch portion
@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.4
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=
@SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$scriptDir='%~dp0'; $script='%__MVNW_ARG0_NAME__%'; icm -ScriptBlock ([Scriptblock]::Create((Get-Content -Raw '%~f0'))) -NoNewScope}"`) DO @(
  IF "%%A"=="MVN_CMD" (set __MVNW_CMD__=%%B) ELSE IF "%%B"=="" (echo %%A) ELSE (echo %%A=%%B)
)
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE%
@SET __MVNW_ARG0_NAME__=
@IF NOT "%__MVNW_CMD__%"=="" ("%__MVNW_CMD__%" %*)
@echo Cannot start maven from wrapper >&2 && exit /b 1
@GOTO :EOF
: end batch / begin powershell #>

$ErrorActionPreference = "Stop"
$distributionUrl = (Get-Content -Raw "$scriptDir/.mvn/wrapper/maven-wrapper.properties" | ConvertFrom-StringData).distributionUrl
$MVN_CMD = $script -replace '^mvnw','mvn'
$distributionUrlName = $distributionUrl -replace '^.*//',''
$distributionUrlNameMain = $distributionUrlName -replace '\.[^.]*$','' -replace '-bin$',''
$MAVEN_M2_PATH = if ($env:MAVEN_USER_HOME) { $env:MAVEN_USER_HOME } else { "$HOME/.m2" }
$MAVEN_HOME_NAME = ([System.Security.Cryptography.SHA256]::Create().ComputeHash([byte[]][char[]]$distributionUrl) | ForEach-Object {$_.ToString("x2")}) -join ''
$MAVEN_HOME = "$MAVEN_M2_PATH/wrapper/dists/$distributionUrlNameMain/$MAVEN_HOME_NAME"
if (Test-Path -Path "$MAVEN_HOME" -PathType Container) {
  Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
  exit 0
}
New-Item -Itemtype Directory -Path (Split-Path $MAVEN_HOME) -Force | Out-Null
$TMP = New-TemporaryFile; $TMP_DIR = New-Item -Itemtype Directory -Path "$TMP.dir"; $TMP.Delete()
trap { if ($TMP_DIR.Exists) { Remove-Item $TMP_DIR -Recurse -Force } }
$webclient = New-Object System.Net.WebClient
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
$webclient.DownloadFile($distributionUrl, "$TMP_DIR/$distributionUrlName")
Expand-Archive "$TMP_DIR/$distributionUrlName" -DestinationPath "$TMP_DIR"
Rename-Item -Path "$TMP_DIR/$distributionUrlNameMain" -NewName $MAVEN_HOME_NAME
Move-Item -Path "$TMP_DIR/$MAVEN_HOME_NAME" -Destination (Split-Path $MAVEN_HOME)
Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
