package com.example.knu_study.project_ips;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.example.knu_study.project_ips.Person_Management.fastToast;
import static com.example.knu_study.project_ips.Person_Management.no_error;

/**
 * Created by Kitty_700 on 2017-11-28.
 */

public class SecondActivity extends Activity implements Serializable {

    TextView site_name_state;
    EditText account_ID_input_in_dialog;
    EditText account_PW_input_in_dialog;
    EditText account_Memo_input_in_dialog;
    Button account_add;

    ExpandableListView listMain;
    SecondActivity secondActivity;
    ArrayList<String> arrayGroup = new ArrayList<String>();
    HashMap<String, ArrayList<String>> arrayChild = new HashMap<String, ArrayList<String>>();
    DialogInterface dialogInterface;

    Site each_site;
    int site_number;
    String site_name;

    /**MOST IMPORTANT INSTANCE**/
    /******/
    Person kitty;/******/
    /***************************/

    void initWidgets() {
        site_name_state = (TextView) findViewById(R.id.site_name_state);
        account_add = (Button) findViewById(R.id.account_add);
        listMain = (ExpandableListView) this.findViewById(R.id.expandableListView_accounts);
        secondActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);
        initWidgets();
        importPerson();
        Intent intent = getIntent();
        if (intent != null) {
            site_number = intent.getIntExtra("site_number", -1);
            site_name = intent.getStringExtra("site_name");
            site_name_state.setText(site_name);
            each_site = kitty.site_set.get(site_number);
            setArrayData();
            listMain.setAdapter(new Adapt_in_SecondActivity(this, arrayGroup, arrayChild));
            listMain.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    fillAccountChangeForm(groupPosition);
                    return false;
                }
            });
            account_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fillAccountAddForm();
                }
            });
        }
    }

    void fillAccountAddForm() {
        final View dialogView = (View) View.inflate(secondActivity, R.layout.account_add_form, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        account_ID_input_in_dialog = (EditText) dialogView.findViewById(R.id.account_form_ID);
        account_PW_input_in_dialog = (EditText) dialogView.findViewById(R.id.account_form_PW);
        account_Memo_input_in_dialog = (EditText) dialogView.findViewById(R.id.account_form_Memo);
        dlg.setView(dialogView);
        dlg.setPositiveButton("Cancel", null);
        dlg.setNegativeButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_ID = account_ID_input_in_dialog.getText().toString();
                        String new_PW = account_PW_input_in_dialog.getText().toString();
                        String new_Memo = account_Memo_input_in_dialog.getText().toString();
                        int error_code = each_site.addAccount(new_ID, new_PW, new_Memo);
                        if (error_code == no_error) {
                            exportPerson();
                            setArrayData();
                            listMain.setAdapter(new Adapt_in_SecondActivity(secondActivity, arrayGroup, arrayChild));
                        } else
                            Person_Management.checkAccountFailure(secondActivity, error_code);
                    }
                });
        dialogInterface = dlg.show();

        Button add_account = (Button) dialogView.findViewById(R.id.add_account_replace);
        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_ID = account_ID_input_in_dialog.getText().toString();
                String new_PW = account_PW_input_in_dialog.getText().toString();
                String new_Memo = account_Memo_input_in_dialog.getText().toString();
                int error_code = each_site.addAccount(new_ID, new_PW, new_Memo);
                if (error_code == no_error) {
                    exportPerson();
                    setArrayData();
                    listMain.setAdapter(new Adapt_in_SecondActivity(secondActivity, arrayGroup, arrayChild));
                } else
                    Person_Management.checkAccountFailure(secondActivity, error_code);
                //원래 performClick()으로 Add 눌리게 할랬는데 실패함. 그래서 그냥 위의 이벤트 복붙
                dialogInterface.dismiss();
            }
        });
        TextView Memo_text_add = (TextView) dialogView.findViewById(R.id.Memo_text_add);
        Memo_text_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_Memo_input_in_dialog.requestFocus();
            }
        });
    }

    void fillAccountChangeForm(final int position) {
        final View dialogView = (View) View.inflate(secondActivity, R.layout.account_change_form, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        dlg.setTitle("Account ID [ " + each_site.account_set.get(position).getID() + " ]");
        account_ID_input_in_dialog = (EditText) dialogView.findViewById(R.id.account_form_ID);
        account_ID_input_in_dialog.setText(each_site.account_set.get(position).getID());
        account_PW_input_in_dialog = (EditText) dialogView.findViewById(R.id.account_form_PW);
        account_PW_input_in_dialog.setText(each_site.account_set.get(position).getPW());
        account_PW_input_in_dialog.requestFocus();
        account_Memo_input_in_dialog = (EditText) dialogView.findViewById(R.id.account_form_Memo);
        account_Memo_input_in_dialog.setText(each_site.account_set.get(position).getMemo());
        dlg.setView(dialogView);
        dlg.setPositiveButton("취소", null);
        dlg.setNegativeButton("변경",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_ID = account_ID_input_in_dialog.getText().toString();
                        String new_PW = account_PW_input_in_dialog.getText().toString();
                        String new_Memo = account_Memo_input_in_dialog.getText().toString();
                        int error_code = each_site.account_set.get(position).setAccountIDPWMEMO
                                (each_site.account_set.get(position).getID(), new_ID, new_PW, new_Memo);
                        if (error_code == no_error) {
                            exportPerson();
                            setArrayData();
                            listMain.setAdapter(new Adapt_in_SecondActivity(secondActivity, arrayGroup, arrayChild));
                        } else
                            Person_Management.checkAccountFailure(secondActivity, error_code);
                    }
                });
        dialogInterface = dlg.show();

        Button delete_account = (Button) dialogView.findViewById(R.id.account_delete);
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                each_site.deleteAccount(position);
                exportPerson();
                setArrayData();
                listMain.setAdapter(new Adapt_in_SecondActivity(secondActivity, arrayGroup, arrayChild));
                dialogInterface.dismiss();
            }
        });
        TextView Memo_text_change = (TextView) dialogView.findViewById(R.id.Memo_text_change);
        Memo_text_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_Memo_input_in_dialog.requestFocus();
            }
        });
    }

    void setArrayData() {   //arrayGroup에 ID를, 그룹에 속한 arrayChild에는 PW,MEMO,UD를 추가한다.
        int i = 0;
        arrayGroup = new ArrayList<String>();
        arrayChild = new HashMap<String, ArrayList<String>>();
        Iterator<Account> accounts_search = each_site.account_set.iterator();
        while (accounts_search.hasNext()) {
            each_site.temp_account = accounts_search.next();
            arrayGroup.add("ID : " + each_site.temp_account.getID());
            ArrayList<String> information_in_each_ID = new ArrayList<>();
            information_in_each_ID.add("PW  :  " + each_site.temp_account.getPW());
            if (!each_site.temp_account.getMemo().equals("")&&!each_site.temp_account.getMemo().equals("-"))
                information_in_each_ID.add("Memo  :  " + each_site.temp_account.getMemo());
            information_in_each_ID.add(each_site.temp_account.getUpdateddate());
            arrayChild.put(arrayGroup.get(i++), information_in_each_ID);
        }
    }

    void importPerson() {
        try {
            String file_path = this.getFilesDir().toString() + "/";
            String file_name = getResources().getString(R.string.basic_file_name);
            File file = new File(file_path + file_name);
            FileInputStream import_stream = new FileInputStream(file);
            this.kitty = (new Import(import_stream, file, this)).getPerson();
            import_stream.close();
        } catch (Exception e) {   //위의 try문이 파일 있을 때를 가정한다면, 이 catch문에서는 파일이 없을 때를 가정한다. 이후 import 새로해줘야함
            fastToast(this, "파일 적재 실패!");
            this.kitty = new Person();
        }
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
