package com.example.knu_study.project_ips;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@SuppressWarnings("serial")
public class Site extends Person_Management {
    //일반 자료형을 intent로 전달하는 것이 아니라 사용자 정의 클래스를 전달하기 위해서는 Serializable 인터페이스를 implements해야한다.
    Person parent;
    String site_name;

    ArrayList<Account> account_set;
    Iterator<Account> accounts_search;
    // 이 자리에 있던 Iterator<Account> accounts_search;을 각 함수에서 선언하도록 바꾸니까
    // Caused by: java.io.NotSerializableException: java.util.ArrayList$Itr 에러는 뜨지 않았다.
    ArrayList<String> accounts_ID_set;

    Account temp_account;

    Site(String siteName, Person parent) {
        this.site_name = siteName;
        this.account_set = new ArrayList<>();
        this.parent = parent;
    }

    /************************************GUI에 의해 수행될 부분************************************/
    //add 는 parent 에서 담당
    int setSiteName(String exception_site_name,String new_name) {
        if (isInvalidStringInput(new_name))
            return invalid_character_included_in_string;
        else if (isNullorEmptyString(new_name))
            return null_string;
        else if (parent.isSameNamedSiteExists(new_name) && !new_name.equals(exception_site_name))
            //사이트 이름을 Change할 때 기존 사이트 이름이 검사당하면 반드시 사이트 이름이 중복되는 문제가 발생하므로 예외처리.
            return already_exist_name;
        else {
            this.site_name = new_name;
            return no_error;
            /**RELOADING**/
        }
    }

    int addAccount(String new_ID, String new_PW, String new_Memo) {
        int error_code = 0;

        if (isInvalidStringInput(new_ID))
            error_code += 400;
        if (isNullorEmptyString(new_ID))
            error_code += 200;
        if (isSameNamedAccountIDExists(new_ID))
            error_code += 100;
        //ID 유효성 검증 끝
        if (isInvalidStringInput(new_PW))
            error_code += 20;
        if (isNullorEmptyString(new_PW))
            error_code += 10;
        //PW 유효성 검증 끝
        if (isInvalidStringInput(new_Memo))
            error_code += 1;
        //Memo 유효성 검증 끝

        if (error_code != 0)
            return error_code;
        /*
            모든 에러 조건을 만족시켰을 때 error_code 값은 최대 값인 731
                (사실 하나의 문자열이 모든 에러 조건을 만족할 수가 없기 때문에 731이 나올 수는 없다.)
                -> 가장 큰 수부터 하나씩 뺐을 때 0보다 크거나 같으면 해당 에러인 것.
                   error_code 731-400 == 331 이므로 400에 해당하는 에러 존재
                   error_code 20 - 20 == 0 이므로 20에 해당하는 에러 존재
                   error_code 1 - 400 == -399 이므로 400에 해당하는 에러는 발생하지 않음.
                   error_code 1 - 20  == -19 이므로 20에 해당하는 에러는 발생하지 않음.
                   error_code 1 - 1   == 0 이므로 1에 해당하는 에러 존재.
            모든 에러 조건을 만족시키지 못했을 때 error_code 값은 000이고 다음과 같은 변경코드를 따른다.
        */

        account_set.add(new Account(
                new_ID,
                new_PW,
                new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss").format(new Date()).toString() + " (in mobile)",
                new_Memo,
                this));
        return no_error;
        /**RELOADING**/
    }

    int deleteAccount(int index) {
        account_set.remove(index);
        return no_error;
        /**RELOADING**/
    }

    /************************************GUI에 의해 수행될 부분************************************/

    ArrayList<String> getAccountsIDSet() {
        accounts_ID_set = new ArrayList<>();
        accounts_search = account_set.iterator();
        while (accounts_search.hasNext()) {
            temp_account = accounts_search.next();
            accounts_ID_set.add(temp_account.ID);
        }
        return accounts_ID_set;
    }

    boolean isSameNamedAccountIDExists(String input_account_id) {
        accounts_search = account_set.iterator();
        while (accounts_search.hasNext()) {
            temp_account = accounts_search.next();
            if (temp_account.ID.equals(input_account_id))
                return true;
        }
        return false;
    }

    String notifyAccounts() {   //#2~#5 담당
        String file_content_in_string = "";
        accounts_search = account_set.iterator();
        while (accounts_search.hasNext()) {
            temp_account = accounts_search.next();
            file_content_in_string =
                    file_content_in_string
                            + File_IO_Protocol.OP_START_INPUT
                            + File_IO_Protocol.INPUT_NEW_ACC_ID
                            + temp_account.ID
                            + File_IO_Protocol.OP_FINISH_INPUT

                            + File_IO_Protocol.OP_START_INPUT
                            + File_IO_Protocol.INPUT_NEW_ACC_PW
                            + temp_account.PW
                            + File_IO_Protocol.OP_FINISH_INPUT

                            + File_IO_Protocol.OP_START_INPUT
                            + File_IO_Protocol.INPUT_NEW_ACC_UPDATED
                            + temp_account.Updated_date
                            + File_IO_Protocol.OP_FINISH_INPUT

                            + File_IO_Protocol.OP_START_INPUT
                            + File_IO_Protocol.INPUT_NEW_ACC_MEMO;
            if (temp_account.Memo.equals(File_IO_Protocol.empty_string))
                file_content_in_string =
                        file_content_in_string +
                                File_IO_Protocol.OP_FINISH_INPUT_NULL;  //메모는 적을 수도, 적지 않을 수도 있다.
            else
                file_content_in_string =
                        file_content_in_string +
                                temp_account.Memo +
                                File_IO_Protocol.OP_FINISH_INPUT;
        }
        temp_account = null;
        return file_content_in_string;
    }
    String getSiteName() {   //나중에 사용할 수 없는 오류 문자열이 입력됐을 때 에러를 리턴해주는 역할
        return this.site_name; //일단 0 리턴
    }
}
