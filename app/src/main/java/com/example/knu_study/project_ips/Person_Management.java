package com.example.knu_study.project_ips;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Kitty_700 on 2017-11-24.
 */

public class Person_Management {

    static final int no_error = 0;
    static final int invalid_character_included_in_string = -1;
    static final int null_string = -2;
    static final int already_exist_name = -3;

    static final int isInvalidIDInput = 400;
    static final int isNullorEmptyID = 200;
    static final int isSameNamedAccountIDExists = 100;
    static final int isInvalidPWInput = 20;
    static final int isNullorEmptyPW = 10;
    static final int isInvalidMemoInput = 1;

    static boolean isInvalidStringInput(String str) {
        return isInvalidStringInput(str, "");
    }

    static boolean isInvalidStringInput(String str1, String str2) {
        return isInvalidStringInput(str1, str2, "");
    }

    static boolean isInvalidStringInput(String str1, String str2, String str3) {   //오버로딩 갯수는 한꺼번에 최대 3개 입력받는 Account의 속성을 따른다.
        //프로그램 짜다보니 별 쓸모 없어졌지만 그냥 남겨둠
        if (str1.contains("" + File_IO_Protocol.OP_START_INPUT) ||
                str2.contains("" + File_IO_Protocol.OP_START_INPUT) ||
                str3.contains("" + File_IO_Protocol.OP_START_INPUT) ||
                str1.contains("" + File_IO_Protocol.OP_FINISH_INPUT) ||
                str2.contains("" + File_IO_Protocol.OP_FINISH_INPUT) ||
                str3.contains("" + File_IO_Protocol.OP_FINISH_INPUT) ||
                str1.contains("" + File_IO_Protocol.OP_FINISH_INPUT_NULL) ||
                str2.contains("" + File_IO_Protocol.OP_FINISH_INPUT_NULL) ||
                str3.contains("" + File_IO_Protocol.OP_FINISH_INPUT_NULL))
            return true;
        return false;
    }

    static boolean isNullorEmptyString(String str) {
        return isNullorEmptyString(str, "asdasd");
    } //empty가 아니면 뭐든 str2로 가능.

    static boolean isNullorEmptyString(String str1, String str2) {   //메모는 입력않더라도 ID와 PW는 입력받아야하기 때문에 2개까지 오버로딩한다.
        if (str1.equals("") || str2.equals(""))
            return true;
        else
            return false;
    }

    static void fastToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    static void checkAccountFailure(SecondActivity secondActivity, int error_code) {
        String error_in_string = "";
        boolean need_new_line = false;
        if (error_code - isInvalidIDInput >= 0) {
            error_in_string = error_in_string + "부적합한 문자가 ID에 포함됨.";
            error_code -= isInvalidIDInput;
            need_new_line = true;
        }
        if (error_code - isNullorEmptyID >= 0) {
            if (need_new_line)
                error_in_string = error_in_string + "\n";
            error_in_string = error_in_string + "ID가 비어있음.";
            error_code -= isNullorEmptyID;
            need_new_line = true;
        }
        if (error_code - isSameNamedAccountIDExists >= 0) {
            if (need_new_line)
                error_in_string = error_in_string + "\n";
            error_in_string = error_in_string + "동일한 ID가 이미 있음.";
            error_code -= isSameNamedAccountIDExists;
            need_new_line = true;
        }
        if (error_code - isInvalidPWInput >= 0) {
            if (need_new_line)
                error_in_string = error_in_string + "\n";
            error_in_string = error_in_string + "부적합한 문자가 비밀번호에 포함됨.";
            error_code -= isInvalidPWInput;
            need_new_line = true;
        }
        if (error_code - isNullorEmptyPW >= 0) {
            if (need_new_line)
                error_in_string = error_in_string + "\n";
            error_in_string = error_in_string + "비밀번호가 비어있음.";
            error_code -= isNullorEmptyPW;
            need_new_line = true;
        }
        if (error_code - isInvalidMemoInput >= 0) {
            if (need_new_line)
                error_in_string = error_in_string + "\n";
            error_in_string = error_in_string + "부적합한 문자가 메모에 포함됨.";
        }
        fastToast(secondActivity, error_in_string);
    }
}
