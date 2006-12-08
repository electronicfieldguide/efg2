




/** 
*Usage
*
*${VersionCheckNew} "version 1" "version 2" "$[output variable]"
*
*
*Example
*${VersionCheckNew} "1.0.1" "1.1.0" "$R0"
*MessageBox MB_OK "Return value: $R0" ;outputs 2 
 
*Output values
*   Output  Meaning
*   0       Versions are equal
*   1       Version 1 is newer
*   2       Version 2 is newer
*/
!macro VersionCheckNew Ver1 Ver2 OutVar
 Push "${Ver1}"
 Push "${Ver2}"
 Call VersionCheckNew
 Pop "${OutVar}"
!macroend
!define VersionCheckNew "!insertmacro VersionCheckNew"
 
Function VersionCheckNew
 Exch $R0 ; second version number
 Exch
 Exch $R1 ; first version number
 Push $R2
 Push $R3
 Push $R4
 Push $R5 ; second version part
 Push $R6 ; first version part
 
  StrCpy $R1 $R1.
  StrCpy $R0 $R0.
 
 Next: StrCmp $R0$R1 "" 0 +3
  StrCpy $R0 0
  Goto Done
 
  StrCmp $R0 "" 0 +2
   StrCpy $R0 0.
  StrCmp $R1 "" 0 +2
   StrCpy $R1 0.
 
 StrCpy $R2 0
  IntOp $R2 $R2 + 1
  StrCpy $R4 $R1 1 $R2
  StrCmp $R4 . 0 -2
    StrCpy $R6 $R1 $R2
    IntOp $R2 $R2 + 1
    StrCpy $R1 $R1 "" $R2
 
 StrCpy $R2 0
  IntOp $R2 $R2 + 1
  StrCpy $R4 $R0 1 $R2
  StrCmp $R4 . 0 -2
    StrCpy $R5 $R0 $R2
    IntOp $R2 $R2 + 1
    StrCpy $R0 $R0 "" $R2
 
 IntCmp $R5 0 Compare
 IntCmp $R6 0 Compare
 
 StrCpy $R3 0
  StrCpy $R4 $R6 1 $R3
  IntOp $R3 $R3 + 1
  StrCmp $R4 0 -2
 
 StrCpy $R2 0
  StrCpy $R4 $R5 1 $R2
  IntOp $R2 $R2 + 1
  StrCmp $R4 0 -2
 
 IntCmp $R3 $R2 0 +2 +4
 Compare: IntCmp 1$R5 1$R6 Next 0 +3
 
  StrCpy $R0 1
  Goto Done
  StrCpy $R0 2
 
 Done:
 Pop $R6
 Pop $R5
 Pop $R4
 Pop $R3
 Pop $R2
 Pop $R1
 Exch $R0 ; output
FunctionEnd




 
!ifndef IsNT_KiCHiK
!define IsNT_KiCHiK
 
###########################################
#            Utility Functions            #
###########################################
 
; IsNT
; no input
; output, top of the stack = 1 if NT or 0 if not
;
; Usage:
;   Call IsNT
;   Pop $R0
;  ($R0 at this point is 1 or 0)
 
!macro IsNT un
Function ${un}IsNT
  Push $0
  ReadRegStr $0 HKLM "SOFTWARE\Microsoft\Windows NT\CurrentVersion" CurrentVersion
  StrCmp $0 "" 0 IsNT_yes
  ; we are not NT.
  Pop $0
  Push 0
  Return
 
  IsNT_yes:
    ; NT!!!
    Pop $0
    Push 1
FunctionEnd
!macroend
!insertmacro IsNT ""
!insertmacro IsNT "un."
 
!endif ; IsNT_KiCHiK
 
; StrStr
; input, top of stack = string to search for
;        top of stack-1 = string to search in
; output, top of stack (replaces with the portion of the string remaining)
; modifies no other variables.
;
; Usage:
;   Push "this is a long ass string"
;   Push "ass"
;   Call StrStr
;   Pop $R0
;  ($R0 at this point is "ass string")
 
!macro StrStr un
Function ${un}StrStr
Exch $R1 ; st=haystack,old$R1, $R1=needle
  Exch    ; st=old$R1,haystack
  Exch $R2 ; st=old$R1,old$R2, $R2=haystack
  Push $R3
  Push $R4
  Push $R5
  StrLen $R3 $R1
  StrCpy $R4 0
  ; $R1=needle
  ; $R2=haystack
  ; $R3=len(needle)
  ; $R4=cnt
  ; $R5=tmp
  loop:
    StrCpy $R5 $R2 $R3 $R4
    StrCmp $R5 $R1 done
    StrCmp $R5 "" done
    IntOp $R4 $R4 + 1
    Goto loop
done:
  StrCpy $R1 $R2 "" $R4
  Pop $R5
  Pop $R4
  Pop $R3
  Pop $R2
  Exch $R1
FunctionEnd
!macroend
!insertmacro StrStr ""
!insertmacro StrStr "un."


/*
*http://nsis.sourceforge.net/GetTime
* get the current time borrowed from NSIS pages
*/
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

/**
* Shows log file to user
*/
Function ShowLogs
 ExecShell "" "$TEMP\${UPDATE_DIR}\log.txt"
FunctionEnd
/*
*Simply write text to file
* Borrowed from: http://nsis.sourceforge.net/Simple_write_text_to_file
*/
Function WriteToFile
 Exch $0 ;file to write to
 Exch
 Exch $1 ;text to write
 
  FileOpen $0 $0 a #open file
   FileSeek $0 0 END #go to end
   FileWrite $0 $1 #write to file
  FileClose $0
 
 Pop $1
 Pop $0
FunctionEnd
 /*
*Simple write text to file macro
* Borrowed from: http://nsis.sourceforge.net/Simple_write_text_to_file
*/
!macro WriteToFile String File
 Push "${String}"
 Push "${File}"
  Call WriteToFile
!macroend
!define WriteToFile "!insertmacro WriteToFile"
; -- written by Alexis de Valence --
; GetONEParameter
 
; Usage:
;   Push 3                 ; to get the 3rd parameter of the command line
;   Call GetONEParameter
;   Pop $R0                ; saves the result in $R0
; returns an empty string if not found
Function GetONEParameter
   Exch $R0
   Push $R1
   Push $R2
   Push $R3
   Push $R4
   Push $R5
   Push $R6
 
; init variables
   IntOp $R5 $R0 + 1
   StrCpy $R2 0
   StrCpy $R4 1
   StrCpy $R6 0
 
   loop3: ; looking for a char that's not a space
     IntOp $R2 $R2 + 1
     StrCpy $R0 $CMDLINE 1 $R2
     StrCmp $R0 " " loop3
     StrCpy $R3 $R2   ; found the beginning of the current parameter
 
 
   loop:          ; scanning for the end of the current parameter
 
     StrCpy $R0 $CMDLINE 1 $R2
     StrCmp $R0 " " loop2
     StrCmp $R0 "" last
     IntOp $R2 $R2 + 1
     Goto loop
 
   last: ; there will be no other parameter to extract
   StrCpy $R6 1
 
   loop2: ; found the end of the current parameter
 
   IntCmp $R4 $R5 0 NextParam end
   StrCpy $R6 1 ; to quit after this process
 
   IntOp $R1 $R2 - $R3 ;number of letter of current parameter
   StrCpy $R0 $CMDLINE $R1 $R3        ; stores the result in R0
  
   NextParam:
   IntCmp $R6 1 end ; leave if found or if not enough parameters
 
   ; process the next parameter
   IntOp $R4 $R4 + 1
 
   Goto loop3
 
   end:
   Pop $R6  ; restore R0 - R6 to their initial value
   Pop $R5
   Pop $R4
   Pop $R3
   Pop $R2
   Pop $R1
 
   Exch $R0    ;Puts the result on the stack
 
 FunctionEnd
 

/**
* Borrowed from NSIS web site
  Indicates the position of a substring within a given String
   $R0 = StartPoint (input)
   $R1 = SubString (input)
   $R2 = String (input)
   $R3 = SubStringLen (temp)
   $R4 = StrLen (temp)
   $R5 = StartCharPos (temp)
   $R6 = TempStr (temp)
*/
 
!macro StrLoc un
Function ${un}StrLoc

  ;Get input from user
  Exch $R0
  Exch
  Exch $R1
  Exch 2
  Exch $R2
  Push $R3
  Push $R4
  Push $R5
  Push $R6
 
  ;Get "String" and "SubString" length
  StrLen $R3 $R1
  StrLen $R4 $R2
  ;Start "StartCharPos" counter
  StrCpy $R5 0
 
  ;Loop until "SubString" is found or "String" reaches its end
  ${Do}
    ;Remove everything before and after the searched part ("TempStr")
    StrCpy $R6 $R2 $R3 $R5
 
    ;Compare "TempStr" with "SubString"
    ${If} $R6 == $R1
      ${If} $R0 == `<`
        IntOp $R6 $R3 + $R5
        IntOp $R0 $R4 - $R6
      ${Else}
        StrCpy $R0 $R5
      ${EndIf}
      ${ExitDo}
    ${EndIf}
    ;If not "SubString", this could be "String"'s end
    ${If} $R5 >= $R4
      StrCpy $R0 ``
      ${ExitDo}
    ${EndIf}
    ;If not, continue the loop
    IntOp $R5 $R5 + 1
  ${Loop}
 
  ;Return output to user
  Pop $R6
  Pop $R5
  Pop $R4
  Pop $R3
  Pop $R2
  Exch
  Pop $R1
  Exch $R0
FunctionEnd
!macroend
!insertmacro StrLoc ""
!insertmacro StrLoc "un."
  