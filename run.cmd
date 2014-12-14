@echo off
set OPT="-Dsun.nio.cs.map=windows-31j/Shift_JIS,x-windows-iso2022jp/ISO-2022-JP"

java %OPT% -jar ./target/jcc.jar %*
