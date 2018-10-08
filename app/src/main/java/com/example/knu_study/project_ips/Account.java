package com.example.knu_study.project_ips;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Account extends Person_Management {
    Site parent;
    String ID;
    String PW = "";
    String Memo = "";
    String Updated_date = "";

    Account(String ID, Site parent) { //import될 때 사용되는 생성자.
        this.ID = ID;
        this.parent = parent;

    }

    Account(String ID, String PW, String Updated_date, String Memo, Site parent) {   //import 된 이후 새로 만들어지는 계정에 대한 생성자
        this.ID = ID;
        this.PW = PW;
        this.Updated_date = Updated_date;
        this.Memo = Memo;
        this.parent = parent;
    }

    /************************************GUI에 의해 수행될 부분************************************/
    //add 는 parent 에서 담당
    int setAccountIDPWMEMO(String exception_ID, String new_ID, String new_PW, String new_Memo) {
        int error_code = 0;

        if (isInvalidStringInput(new_ID))
            error_code += 400;
        if (isNullorEmptyString(new_ID))
            error_code += 200;
        if (parent.isSameNamedAccountIDExists(new_ID) && !new_ID.equals(exception_ID))
            //계정이름을 Change할 때 기존 ID가 검사당하면 반드시 ID가 중복되는 문제가 발생하므로 예외처리.
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
                -> 가장 큰 수부터 하나씩 뺐을 때 자연수이면 해당 에러인 것.
                   error_code 731-400 == 331 이므로 400에 해당하는 에러 존재
                   error_code 20 - 20 == 0 이므로 20에 해당하는 에러 존재
                   error_code 1 - 400 == -399 이므로 400에 해당하는 에러는 발생하지 않음.
                   error_code 1 - 20  == -19 이므로 20에 해당하는 에러는 발생하지 않음.
                   error_code 1 - 1   == 0 이므로 1에 해당하는 에러 존재.

            모든 에러 조건을 만족시키지 못했을 때 error_code 값은 000이고 다음과 같은 변경코드를 따른다.
        */
        setID(new_ID);
        setPW(new_PW);
        setMemo(new_Memo);
        setUpdateddate(new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss").format(new Date()).toString() + " (in mobile)");
        return no_error;
        /**RELOADING**/
    }

    /************************************GUI에 의해 수행될 부분************************************/

    void setID(String new_ID) {
        this.ID = new_ID;
    }

    void setPW(String new_PW) {
        this.PW = new_PW;
    }

    void setUpdateddate(String Updated_date) {
        this.Updated_date = Updated_date;
    }

    void setMemo(String new_Memo) { this.Memo = new_Memo;  }

    String getID() {
        return this.ID;
    }

    String getPW() {
        return this.PW;
    }

    String getUpdateddate() {
        return this.Updated_date;
    }

    String getMemo() {
        return this.Memo;
    }

}
