package com.example.knu_study.project_ips;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Import extends File_IO_Protocol {
    //생성자 인자로 정상적으로 로드된 FileInputStream 인스턴스와 결과를 출력할 텍스트뷰 인스턴스를 받는다.

    FileInputStream import_stream;
    File file;
    Context context;
    char now_what_i_doing/*?*/ = (char) -1; //아무 상태도 아니니 -1로 일단 초기화해둔다.

    byte file_content[];

    ArrayList<Site> site_set_for_kitty;

    Site temp_site;
    Account temp_account;

    Person kitty;

    Import(FileInputStream file_stream, File file, Context context) {
        this.file_content = new byte[(int) file.length()];
        this.import_stream = file_stream;
        this.file = file;
        this.site_set_for_kitty = new ArrayList<>();
        this.context = context;
        kitty = new Person();
        try {
            file_stream.read(file_content);
        } catch (Exception e) {
            Person_Management.fastToast(context, "Import Class has problem.");
        }
        assemblyBytes();
        kitty.takeSites(site_set_for_kitty);
    }

    void assemblyBytes() {   //Byte 스트림을 조립해서 Person 인스턴스를 완성해나간다.
        boolean is_start = false;
        boolean is_separate_on = false;
        byte temp_byte[] = new byte[STRING_LIMIT];   //UI와 타 플랫폼과의 호환성을 위해 바이트 길이는 200으로 제한된다.
        int index = 0;
        for (byte each_byte : file_content) {
            if (do_decrypt)
                each_byte = decryptByte(each_byte); //암호화의 단위가 개별 바이트므로 복호화 역시 각각의 바이트 별로 해준다.
            if (is_separate_on) //파일 내용 중에 # 이 있다면 is_separete_on이 true가 된다.
            {
                switch (each_byte) {
                    case START_LOADING:
                        is_start = true;
                        now_what_i_doing = START_LOADING;
                        break;
                    case INPUT_NEW_SITE_NAME:
                        now_what_i_doing = INPUT_NEW_SITE_NAME;
                        break;
                    case INPUT_NEW_ACC_ID:
                        now_what_i_doing = INPUT_NEW_ACC_ID;
                        break;
                    case INPUT_NEW_ACC_PW:
                        now_what_i_doing = INPUT_NEW_ACC_PW;
                        break;
                    case INPUT_NEW_ACC_UPDATED:
                        now_what_i_doing = INPUT_NEW_ACC_UPDATED;
                        break;
                    case INPUT_NEW_ACC_MEMO:
                        now_what_i_doing = INPUT_NEW_ACC_MEMO;
                        break;
                    case INPUT_NEW_SITE_END:
                        now_what_i_doing = INPUT_NEW_SITE_END;
                        break;
                    case FINISH_LOADING:
                        now_what_i_doing = FINISH_LOADING;
                        is_start = false; //종료를 확실하게 해줘야 쓸모없이 다른 바이트 읽어서 해석하는 과정을 굳이 거치지 않는다.
                        kitty.giveLife();  //제대로 로드를 마칠 시점에 무언가 하나라도 가지고 있다면 살아있다고 할 만한 상태라고 판단한다.
                        break;
                    default:
                        Person_Management.fastToast(context, "unexpected operational character is inserted in assembling.");
                }
                is_separate_on = false;
            } else {
                switch (each_byte) {
                    case OP_START_INPUT: //(시스템 조작 문자 #, $, % 등)
                        is_separate_on = true;
                        break;
                    default:
                        if (is_start == false) //is_start 가 false 면 굳이 버퍼에 아무것도 넣을 필요가 없다.
                            continue;
                        switch (each_byte) {
                            case OP_FINISH_INPUT:   //OP_FINISH_INPUT 종류는 Person의 적재적소에 문자열을 붙이는 트리거 역할을 한다.
                                try {
                                    attachString(new String(temp_byte, "KSC5601")); //버그 고칠 때 for문 가까이에 검사하는 코드를 넣어 제대로 디버깅을 못 했었음.
                                } catch (Exception e) {
                                    Person_Management.fastToast(context, "bytes assembling has error.");
                                }
                                break;
                            case OP_FINISH_INPUT_NULL:
                                attachString(empty_string);
                                break;
                            default:
                                if (index < STRING_LIMIT)    //윈도우버전 호환을 위해 STRING_LIMIT 를 넘어서면 더이상 불러들이지 않는다.
                                    temp_byte[index++] = each_byte;
                                continue;
                        }
                        //한번 버퍼를 attachString()으로 보냈으면 버퍼 관련 정보를 전부 초기화시킨다.
                        index = 0;
                        temp_byte = new byte[STRING_LIMIT];
                }
            }
        }
        temp_site = null;
        temp_account = null;
    }

    void attachString(String each_string) {   //assemblyBytes() 로부터 호출되어 Person 인스턴스의 적재적소에 문자열을 붙이는 함수.
        each_string = each_string.trim();
        switch (now_what_i_doing/*?*/) {
            case INPUT_NEW_SITE_NAME:
                temp_site = new Site(each_string, kitty);
                site_set_for_kitty.add(temp_site);
                break;
            case INPUT_NEW_ACC_ID:
                temp_account = new Account(each_string, temp_site);
                temp_site.account_set.add(temp_account);
                break;
            case INPUT_NEW_ACC_PW:
                temp_account.setPW(each_string);
                break;
            case INPUT_NEW_ACC_UPDATED:
                temp_account.setUpdateddate(each_string);
                break;
            case INPUT_NEW_ACC_MEMO:
                temp_account.setMemo(each_string);
                temp_account = null;
                break;
            case INPUT_NEW_SITE_END:
                temp_site = null;
                break;
            case START_LOADING:
            case FINISH_LOADING:
                break;
            default:
                Person_Management.fastToast(context, "attaching strings is error.");
        }
    }

    byte decryptByte(byte each_byte) {
        return (byte) ((~each_byte) ^ crypt_key);
    }

    Person getPerson() {
        return kitty;
    }

}