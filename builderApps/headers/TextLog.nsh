# TextLog.nsh v1.1 - 2005-12-26
# Written by Mike Schinkel [http://www.mikeschinkel.com/blog/]
 
Var /GLOBAL __TextLog_FileHandle
Var /GLOBAL __TextLog_FileName
Var /GLOBAL __TextLog_State
 
!define LogMsg '!insertmacro LogMsgCall'
!macro LogMsgCall _text
        Call LogSetOn
    Push "${_text}"
    Call LogText
    Call LogSetOff
!macroend
 
 
!define LogText '!insertmacro LogTextCall'
!macro LogTextCall _text
    Push "${_text}"
    Call LogText
!macroend
Function GetTime
    !define GetTime `!insertmacro GetTimeCall`
 
    !macro GetTimeCall _FILE _OPTION _R1 _R2 _R3 _R4 _R5 _R6 _R7
        Push `${_FILE}`
        Push `${_OPTION}`
        Call GetTime
        Pop ${_R1}
        Pop ${_R2}
        Pop ${_R3}
        Pop ${_R4}
        Pop ${_R5}
        Pop ${_R6}
        Pop ${_R7}
    !macroend
 
    Exch $1
    Exch
    Exch $0
    Exch
    Push $2
    Push $3
    Push $4
    Push $5
    Push $6
    Push $7
    ClearErrors
 
    StrCmp $1 'L' gettime
    StrCmp $1 'A' getfile
    StrCmp $1 'C' getfile
    StrCmp $1 'M' getfile
    StrCmp $1 'LS' gettime
    StrCmp $1 'AS' getfile
    StrCmp $1 'CS' getfile
    StrCmp $1 'MS' getfile
    goto error
 
    getfile:
    IfFileExists $0 0 error
    System::Call /NOUNLOAD '*(i,l,l,l,i,i,i,i,&t260,&t14) i .r6'
    System::Call /NOUNLOAD 'kernel32::FindFirstFileA(t,i)i(r0,r6) .r2'
    System::Call /NOUNLOAD 'kernel32::FindClose(i)i(r2)'
 
    gettime:
    System::Call /NOUNLOAD '*(&i2,&i2,&i2,&i2,&i2,&i2,&i2,&i2) i .r7'
    StrCmp $1 'L' 0 systemtime
    System::Call /NOUNLOAD 'kernel32::GetLocalTime(i)i(r7)'
    goto convert
    systemtime:
    StrCmp $1 'LS' 0 filetime
    System::Call /NOUNLOAD 'kernel32::GetSystemTime(i)i(r7)'
    goto convert
 
    filetime:
    System::Call /NOUNLOAD '*$6(i,l,l,l,i,i,i,i,&t260,&t14)i(,.r4,.r3,.r2)'
    System::Free /NOUNLOAD $6
    StrCmp $1 'A' 0 +3
    StrCpy $2 $3
    goto tolocal
    StrCmp $1 'C' 0 +3
    StrCpy $2 $4
    goto tolocal
    StrCmp $1 'M' tolocal
 
    StrCmp $1 'AS' tosystem
    StrCmp $1 'CS' 0 +3
    StrCpy $3 $4
    goto tosystem
    StrCmp $1 'MS' 0 +3
    StrCpy $3 $2
    goto tosystem
 
    tolocal:
    System::Call /NOUNLOAD 'kernel32::FileTimeToLocalFileTime(*l,*l)i(r2,.r3)'
    tosystem:
    System::Call /NOUNLOAD 'kernel32::FileTimeToSystemTime(*l,i)i(r3,r7)'
 
    convert:
    System::Call /NOUNLOAD '*$7(&i2,&i2,&i2,&i2,&i2,&i2,&i2,&i2)i(.r5,.r6,.r4,.r0,.r3,.r2,.r1,)'
    System::Free $7
 
    IntCmp $0 9 0 0 +2
    StrCpy $0 '0$0'
    IntCmp $1 9 0 0 +2
    StrCpy $1 '0$1'
    IntCmp $2 9 0 0 +2
    StrCpy $2 '0$2'
    IntCmp $6 9 0 0 +2
    StrCpy $6 '0$6'
 
    StrCmp $4 0 0 +3
    StrCpy $4 Sunday
    goto end
    StrCmp $4 1 0 +3
    StrCpy $4 Monday
    goto end
    StrCmp $4 2 0 +3
    StrCpy $4 Tuesday
    goto end
    StrCmp $4 3 0 +3
    StrCpy $4 Wednesday
    goto end
    StrCmp $4 4 0 +3
    StrCpy $4 Thursday
    goto end
    StrCmp $4 5 0 +3
    StrCpy $4 Friday
    goto end
    StrCmp $4 6 0 error
    StrCpy $4 Saturday
    goto end
 
    error:
    SetErrors
    StrCpy $0 ''
    StrCpy $1 ''
    StrCpy $2 ''
    StrCpy $3 ''
    StrCpy $4 ''
    StrCpy $5 ''
    StrCpy $6 ''
 
    end:
    Pop $7
    Exch $6
    Exch
    Exch $5
    Exch 2
    Exch $4
    Exch 3
    Exch $3
    Exch 4
    Exch $2
    Exch 5
    Exch $1
    Exch 6
    Exch $0
FunctionEnd 
Function LogText
    Exch $0     ; pABC -> 0ABC
    FileWrite $__TextLog_FileHandle "$0$\r$\n"
    Pop $0      ; 0ABC -> ABC
FunctionEnd
 
!define LogSetFileName '!insertmacro LogSetFileNameCall'
!macro LogSetFileNameCall _filename
    Push "${_filename}"
    Call LogSetFileName
!macroend
 
Function LogSetFileName
    Exch $0     ; pABC -> 0ABC
    StrCpy $__TextLog_FileName "$0"
    StrCmp $__TextLog_State "open" +1 +3
    Call LogSetOff
    Call LogSetOn
    Pop $0      ; 0ABC -> ABC
FunctionEnd
 
!define LogSetOn '!insertmacro LogSetOnCall'
!macro LogSetOnCall
    Call LogSetOn
!macroend
 
Function LogSetOn
    StrCmp $__TextLog_FileName "" +1 AlreadySet
    StrCpy $__TextLog_FileName "$INSTDIR\install.log"
AlreadySet:
    StrCmp $__TextLog_State "open" +2
    FileOpen $__TextLog_FileHandle  "$__TextLog_FileName"  a
        FileSeek $__TextLog_FileHandle 0 END
    StrCpy $__TextLog_State "open"
FunctionEnd
 
!define LogSetOff '!insertmacro LogSetOffCall'
!macro LogSetOffCall
    Call LogSetOff
!macroend
  Function writeTime
    ${LogText} ""
    ${LogText} "-------------------------------------"
    ${GetTime} "" "L" $0 $1 $2 $3 $4 $5 $6

    ; $0="01"      day

    ; $1="04"      month

    ; $2="2005"    year

    ; $3="Friday"  day of week name

    ; $4="16"      hour

    ; $5="05"      minute

    ; $6="50"      seconds


    ${LogText} "'Date=$0/$1/$2 ($3)$\nTime=$4:$5:$6'"    
     ${LogText} "Log file for installer EFG2Installer ${ProductVersion}"
  ${LogText} "-------------------------------------"
   ${LogText} ""
   
 FunctionEnd
Function LogSetOff
    StrCmp $__TextLog_State "open" +1 +2
    FileClose $__TextLog_FileHandle
    StrCpy $__TextLog_State ""
FunctionEnd