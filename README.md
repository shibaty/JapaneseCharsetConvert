CharsetConvert
==============

Japanese charset convert

Javaの起動オプションに以下を付与することで、あまり難しいことは考えずに、ShiftJISとJISを扱えた

```
-Dsun.nio.cs.map=windows-31j/Shift_JIS,x-windows-iso2022jp/ISO-2022-JP
```

Shift_JISについては、windows-31j(MS932)にマッピングすることで依存文字に対応する。  
JIS(ISO-2022-JP)については、x-windows-iso2022jpにマッピングすることでMS932相当の依存文字に対応する。  
コード中では、Shift_JIS、ISO-2022-JPで扱えばよい。  
JavaMailのISO-2022-JPに関してもこれで丸数字等の対応ができる。  
いわゆる波ダッシュ問題も自前のコーディングは不要。  
