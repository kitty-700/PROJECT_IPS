package com.example.knu_study.project_ips;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

public class Export extends File_IO_Protocol//Import에 비해 초라하다.
{
    File file;
    FileOutputStream export_stream;
    Person kitty;
    Context context;

    Export(FileOutputStream file_stream, File file, Context context, Person person_to_export) {
        this.export_stream = file_stream;
        this.file = file;
        this.context = context;
        this.kitty = person_to_export;
    }

    void finallyExport() {
        try {
            byte bytes[] = kitty.notifySites().getBytes("KSC5601");
            if (do_encrypt)
                for (int i = 0; i < bytes.length; i++)
                    bytes[i] = encryptByte(bytes[i]);
            export_stream.write(bytes);
        } catch (Exception e) {
            Person_Management.fastToast(context,"Can't write!!");
        }

    }

    byte encryptByte(byte each_byte) {
        return (byte) ((~each_byte) ^ crypt_key);
    }
}
