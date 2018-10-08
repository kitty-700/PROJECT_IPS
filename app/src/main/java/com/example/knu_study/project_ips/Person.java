package com.example.knu_study.project_ips;

import java.util.ArrayList;
import java.util.Iterator;

public class Person extends Person_Management {
    boolean is_alive;

    ArrayList<Site> site_set;
    Iterator<Site> sites_search; // site_set 을 순회하기 직전에 site_set.iterator()로 초기화하여 사용한다.

    ArrayList<String> site_name_set;

    Site temp_site;  //Iterator로 순회를 돌 때 여러 함수에서 사용한다.

    Person() {
        site_set = new ArrayList<>();
    }

    /************************************GUI에 의해 수행될 부분************************************/
    int addSite(String new_site_name) {
        if (isInvalidStringInput(new_site_name))
            return invalid_character_included_in_string;
        else if (isSameNamedSiteExists(new_site_name))
            return already_exist_name;
        else if (isNullorEmptyString(new_site_name))
            return null_string;
            //에러가 발생하여 0보다 작은 수가 리턴되면 실패 팝업을 띄우고 아무것도 하지 않을 것.
        else {   //계정이 새로 추가될 때 자동으로 Export 후 다시 Import 하 는 것과 달리
            //사이트 내에 계정이 생성될 때까지 Person 인스턴스에만 사이트 이름를 추가해준다.
            site_set.add(new Site(new_site_name, this));
            return no_error;
        }
    }

    int deleteSite(int index) {
        site_set.remove(index);
        return no_error;
        /**RELOADING (계정에 영향이 가므로)**/
    }
    /************************************GUI에 의해 수행될 부분************************************/

    int current_index;

    boolean isSameNamedSiteExists(String site_name) {
        sites_search = site_set.iterator();
        current_index = 0;
        while (sites_search.hasNext()) {
            temp_site = sites_search.next();
            if (temp_site.site_name.equals(site_name))
                return true;
            current_index++;
        }
        return false;
    }

    ArrayList<String> getSitesNameSet() {
        site_name_set = new ArrayList<>();
        sites_search = site_set.iterator();
        while (sites_search.hasNext()) {
            temp_site = sites_search.next();
            site_name_set.add(temp_site.site_name);
        }
        return site_name_set;
    }

    String notifySites() {
        //Sites와 각 Site 내의 Accounts 에 포함된 모든 문자열과 구분자를 이어 붙여서 출력. (Export될 때 사용된다.)
        //#0 #1 #6 을 담당한다.
        String file_content_in_string = ""
                + File_IO_Protocol.OP_START_INPUT
                + File_IO_Protocol.START_LOADING;
        sites_search = site_set.iterator();
        while (sites_search.hasNext()) {
            temp_site = sites_search.next();
            file_content_in_string = file_content_in_string
                    + File_IO_Protocol.OP_START_INPUT
                    + File_IO_Protocol.INPUT_NEW_SITE_NAME

                    + temp_site.site_name

                    + File_IO_Protocol.OP_FINISH_INPUT

                    + temp_site.notifyAccounts()

                    + File_IO_Protocol.OP_START_INPUT
                    + File_IO_Protocol.INPUT_NEW_SITE_END;
        }
        temp_site = null;
        return file_content_in_string
                + File_IO_Protocol.OP_START_INPUT
                + File_IO_Protocol.FINISH_LOADING;
    }

    void giveLife() {
        this.is_alive = true;
    }

    void takeSites(ArrayList<Site> sites) {   //Import될 때 ArrayList<Site>를 붙이기 위해 사용된다.
        this.site_set = sites;
    }

    void sort() {   //ArrayList에서 swap을 어떻게 구현할지 몰라서 새 ArrayList에 추가하는 방식을 사용했다.
                    //삽입정렬의 형식과 유사하다.
        ArrayList<Site> new_site_set = new ArrayList<>();
        String debug_str = "";
        int repeat_count = site_set.size();
        for (int i = 0; i < repeat_count; i++) {
            int min_value = 0;
            int min_index = 0;
            int now_index = 0;
            sites_search = site_set.iterator();
            while (sites_search.hasNext()) {
                temp_site = sites_search.next();
                if (temp_site == site_set.get(0)) {
                    min_value = temp_site.site_name.getBytes()[0];
                    min_index = 0;
                } else {
                    if (temp_site.site_name.getBytes()[0] < min_value) {
                        min_value = temp_site.site_name.getBytes()[0];
                        min_index = now_index;
                    }
                }
                now_index++;
            }
            debug_str = debug_str + site_set.get(min_index).site_name + "\n";
            new_site_set.add(site_set.get(min_index));
            site_set.remove(min_index);
        }
        this.takeSites(new_site_set);
    }
}