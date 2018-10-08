package com.example.knu_study.project_ips;

/**
 * Created by KNU-STUDY on 2017-11-23.
 */

public class File_IO_Protocol {
    static final char OP_START_INPUT = '#';
    static final char OP_FINISH_INPUT = '$';
    static final char OP_FINISH_INPUT_NULL = '%';
    static final char START_LOADING = '0';
    static final char INPUT_NEW_SITE_NAME = '1';
    static final char INPUT_NEW_ACC_ID = '2';
    static final char INPUT_NEW_ACC_PW = '3';
    static final char INPUT_NEW_ACC_UPDATED = '4';
    static final char INPUT_NEW_ACC_MEMO = '5';
    static final char INPUT_NEW_SITE_END = '6';
    static final char FINISH_LOADING = '9';
    static final byte crypt_key = (byte)OP_START_INPUT;
    static final String empty_string = "\t-\t";
    static final int STRING_LIMIT = 200;
    /*********************************/
    static boolean do_decrypt = true;
    static boolean do_encrypt = true;
    /*********************************/
}
