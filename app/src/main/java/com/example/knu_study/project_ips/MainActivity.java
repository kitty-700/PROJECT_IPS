package com.example.knu_study.project_ips;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static com.example.knu_study.project_ips.Person_Management.already_exist_name;
import static com.example.knu_study.project_ips.Person_Management.fastToast;
import static com.example.knu_study.project_ips.Person_Management.invalid_character_included_in_string;
import static com.example.knu_study.project_ips.Person_Management.null_string;

public class MainActivity extends Activity {
    ListView site_list;
    EditText site_name_input;
    Button site_add;
    MainActivity mainActivity;
    boolean is_updating_accounts = false;

    ArrayAdapter<String> site_adapter;
    DialogInterface dialogInterface;

    /**MOST IMPORTANT INSTANCE**/
    /******/
    Person kitty;/******/
    /***************************/

    void initWidgets() {
        site_list = (ListView) findViewById(R.id.site_list);
        site_name_input = (EditText) findViewById(R.id.site_name_input);
        site_add = (Button) findViewById(R.id.site_add);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mainActivity = this; //new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kitty.getSitesNameSet()); 에서 this 대신.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        importPerson();
        site_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_site_name = site_name_input.getText().toString();
                new_site_name = new_site_name.trim();
                if (useSpecialFunctions(new_site_name))
                    ;
                else {
                    switch (kitty.addSite(new_site_name)) {
                        case invalid_character_included_in_string:
                            Person_Management.fastToast(mainActivity, "부적절한 문자를 포함시키지 마세요!");
                            break;
                        case null_string:
                            Person_Management.fastToast(mainActivity, "사이트 이름을 입력해주세요!");
                            break;
                        case already_exist_name:
                            site_list.setSelection(kitty.current_index);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(site_name_input.getWindowToken(), 0);
                            Person_Management.fastToast(mainActivity, "이미 존재하는 사이트 이름입니다!");
                            break;
                        default: //추가 성공
                            change_apply();
                    }
                }
                site_name_input.setText("");
            }
        });
        site_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fillSiteChangeForm(position);
                return true; //true 하면 이후의 예기치 못한 리스너를 생성하지 않는다.
            }
        });
        site_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                is_updating_accounts = true;
                viewAccountPage(position);
                importPerson();
                site_list.setSelection(position);
                //사이트 내의 계정이 아무리 엉망진창으로 변경된다고 해도 사이트 목록을 나타내는 창에는 전혀 영향을 주지 않으므로 화면 갱신할 필요가 없다.
            }
        });
    }

    boolean useSpecialFunctions(String new_site_name) {
        if (new_site_name.equals("#bring")) {
            bringTreasure();
        } else if (new_site_name.startsWith("#bring ")) { //파일 이름을 인자로 받는다.
            new_site_name = new_site_name.substring("#bring ".length()); //공백문자가 포함되므로
            bringTreasure(new_site_name);
        } else if (new_site_name.equals("#leave")) {
            leaveTreasure();
        } else if (new_site_name.startsWith("#leave ")) {
            new_site_name = new_site_name.substring("#leave ".length());
            leaveTreasure(new_site_name);
        } else if (new_site_name.equals("#help")) {
            helpDialog();
        } else if (new_site_name.equals("#sort")) {
            kitty.sort();
            change_apply();
        } else if (new_site_name.equals("#delete")) {
            kitty = new Person();
            fastToast(mainActivity, "파일이 삭제됐음.");
            change_apply();
        } else if (new_site_name.equals("#reload")) {
            fastToast(mainActivity, "다시 적재했음.");
            importPerson();
        } else
            return false;
        return true;
    }

    void helpDialog() {
        fastToast(this, "17년 말 버전.\n" +
                "다운로드 폴더에서 가져오려면 #bring\n" +
                "다운로드 폴더로 내보내려면 #leave\n" +
                "그 외 입력에 #,$,% 포함ㄴㄴ.");
    }

    void change_apply() {
        exportPerson();
        site_adapter = new ArrayAdapter<String>(mainActivity, R.layout.align_in_site_listview, kitty.getSitesNameSet());
        site_list.setAdapter(site_adapter);
        site_adapter.notifyDataSetChanged();
    }

    void viewAccountPage(final int position) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("site_number", position);
        intent.putExtra("site_name", kitty.site_set.get(position).site_name);
        startActivity(intent);
    }

    void fillSiteChangeForm(final int position) {
        final View dialogView = (View) View.inflate(mainActivity, R.layout.site_form, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        dlg.setTitle("Site [ " + kitty.site_set.get(position).getSiteName() + " ]");
        final EditText site_name_input_in_dialog = (EditText) dialogView.findViewById(R.id.site_name_input);
        site_name_input_in_dialog.setText(kitty.site_set.get(position).site_name);
        site_name_input_in_dialog.setSelection(site_name_input_in_dialog.length());
        dlg.setView(dialogView);
        dlg.setPositiveButton("취소", null);
        dlg.setNegativeButton("변경",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (kitty.site_set.get(position).setSiteName
                                (kitty.site_set.get(position).getSiteName(), site_name_input_in_dialog.getText().toString())) {
                            case invalid_character_included_in_string:
                                Person_Management.fastToast(mainActivity, "부적절한 문자를 포함시키지 마세요!");
                                break;
                            case null_string:
                                Person_Management.fastToast(mainActivity, "사이트 이름을 입력해주세요!");
                                break;
                            case already_exist_name:
                                Person_Management.fastToast(mainActivity, "이미 존재하는 사이트 이름입니다!");
                                break;
                            default://성공했다는 뜻이니까
                                change_apply();
                        }
                    }
                });
        dialogInterface = dlg.show();

        Button delete_site = (Button) dialogView.findViewById(R.id.site_delete);
        delete_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kitty.deleteSite(position);
                change_apply();
                dialogInterface.dismiss();
            }
        });
    }

    void leaveTreasure() {
        leaveTreasure(getResources().getString(R.string.basic_file_name));
    }

    void leaveTreasure(String destination_file_name) { //local 폴더 -> download 폴더
        //이미 WRITE_EXTERNAL_STORAGE 권한이 부여된 앱은 READ_EXTERNAL_STORAGE 권한이 암시적으로 부여됨.
        if (checkSelfPermission
                (Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
            fastToast(this, "권한 얻고 다시 실행해주세요.");
        } else {
            String destination_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/";
            String source_path = this.getFilesDir().toString() + "/";
            File source_file = new File(source_path + getResources().getString(R.string.basic_file_name));
            File destination_file = new File(destination_path + destination_file_name);
            try {
                int readcount = 0;
                FileInputStream source = new FileInputStream(source_file);
                FileOutputStream destination = new FileOutputStream(destination_file);
                byte[] buffer = new byte[1024];
                while ((readcount = source.read(buffer, 0, 1024)) != -1) {
                    destination.write(buffer, 0, readcount);
                }
                fastToast(this, "파일 전송 성공");
                destination.close();
                source.close();
            } catch (Exception e) {
                fastToast(this, "파일 전송 실패");
            }
        }
    }

    void bringTreasure() {
        bringTreasure(getResources().getString(R.string.basic_file_name));
    }

    void bringTreasure(String source_file_name) { //download 폴더 -> local 폴더
        //Download 폴더에 원하는 파일이 존재하면 내부저장소로 가져오되, 정상적이지 않은 파일이라면 폐기한다.
        String source_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/";

        File source_file = new File(source_path + source_file_name);
        Person untested_person;
        if (checkSelfPermission
                (Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
            fastToast(this, "권한 얻고 다시 실행해주세요.");
        } else {
            if (source_file.exists() && source_file.canRead() && source_file.canWrite()) {
                try {
                    FileInputStream source = new FileInputStream(source_file);
                    untested_person = (new Import(source, source_file, this)).getPerson();
                    source.close();
                    if (untested_person.is_alive == true) {
                        this.kitty = untested_person;
                        change_apply();
                        source_file.delete(); //성공적인 복사 이후엔 보안을 위해 원래 파일은 삭제된다. (내부저장소는 몰라도 download폴더는 위험하다.)
                        fastToast(this, "다운로드 폴더에 위치하던 파일을 정상적으로 가져왔으며 원래 파일은 삭제되었습니다");
                    } else {
                        fastToast(this, "다운로드 폴더에 파일이 있긴하지만 정상적인 파일이 아니라고 판단되기에 가져오진 않았습니다");
                    }
                } catch (Exception e) {
                    fastToast(this, "다운로드 폴더에 파일이 있긴하지만 읽고 쓸 권한이 없습니다");
                }
            } else {
                fastToast(this, "다운로드 폴더에 해당 파일이 없습니다");
            }
        }
    }

    void importPerson() {
        importPerson(getResources().getString(R.string.basic_file_name));
    }

    void importPerson(String file_name_arg) {
        try {
            Person untested_person;
            String file_path = this.getFilesDir().toString() + "/";
            String file_name = file_name_arg;
            File file = new File(file_path + file_name);
            FileInputStream import_stream = new FileInputStream(file);
            untested_person = (new Import(import_stream, file, this)).getPerson();
            if (untested_person.is_alive == true) //import된 person은 제대로 된 파일로부터 만들어진 정상 person일수도, 아닐 수도 있다.
                this.kitty = untested_person;
            else {
                this.kitty = new Person();
                //exportPerson();
                fastToast(this, kitty.notifySites());
            }
            import_stream.close();
        } catch (Exception e) {   //위의 try문이 파일 있을 때를 가정한다면, 이 catch문에서는 파일이 없을 때를 가정한다.
            this.kitty = new Person();
            exportPerson();
            helpDialog();
            fastToast(this, "처음 사용자?");
        }
        site_adapter = new ArrayAdapter<String>
                (mainActivity, R.layout.align_in_site_listview, kitty.getSitesNameSet());
        site_list.setAdapter(site_adapter);
        site_adapter.notifyDataSetChanged();
    }

    void exportPerson() {
        try {
            String file_path = this.getFilesDir().toString() + "/";
            String file_name = getResources().getString(R.string.basic_file_name);
            File file = new File(file_path + file_name);
            FileOutputStream export_stream = new FileOutputStream(file);
            (new Export(export_stream, file, this, this.kitty)).finallyExport();
            export_stream.close();
        } catch (Exception e) {
            fastToast(this, "파일 저장 실패");
        }
    }
}
