package com.testing.simplesp.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Created by admin on 2016/2/18.
 */
public class StreamUtils {
    public static boolean judge = false;
    private static BufferedReader br;

    public static String readFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];

        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        String result = outputStream.toString();
        inputStream.close();
        outputStream.close();
        return result;
    }

    private static String line;

    public static FileReader createFileReader(File file) throws FileNotFoundException {
        return new FileReader(file);
    }

    public static FileWriter createFileWriter(File file) throws IOException {
        return new FileWriter(file);
    }

    public static InputStreamReader createInputStreamReader(Object obj) {
        if (obj instanceof File)
            if (!((File) obj).exists())
                ((File) obj).getParentFile().mkdirs();
        try {
            return new InputStreamReader(new FileInputStream((File) obj), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OutputStreamWriter createOutputStreamWriter(Object obj) {
        if (obj instanceof File)
            if (!((File) obj).exists())
                ((File) obj).getParentFile().mkdirs();
        try {
            return new OutputStreamWriter(new FileOutputStream((File) obj, true), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedReader createBufferedReader(Object obj, String cd) throws IOException {
        if (obj instanceof String)
            return new BufferedReader(createInputStreamReader(new File((String) obj)));
        if (obj instanceof InputStream) {
            if (cd == null)
                return new BufferedReader(new InputStreamReader((InputStream) obj));
            else
                return new BufferedReader(new InputStreamReader((InputStream) obj, cd));
        }
        if (obj instanceof File) {
            if (!((File) obj).exists())
                ((File) obj).createNewFile();
            return new BufferedReader(createFileReader((File) obj));
        }
        if (obj instanceof Reader)
            return new BufferedReader((Reader) obj);
        if (obj instanceof BufferedReader)
            return (BufferedReader) obj;
        return null;
    }

    public static BufferedWriter createBufferedWriter(Object obj) throws IOException {
        if (obj instanceof String)
            return new BufferedWriter(createOutputStreamWriter(new File((String) obj)));
        if (obj instanceof OutputStream)
            return new BufferedWriter(new OutputStreamWriter((OutputStream) obj, "utf-8"));
        if (obj instanceof File)
            return new BufferedWriter(createOutputStreamWriter(obj));
        if (obj instanceof Writer)
            return new BufferedWriter((Writer) obj);
        if (obj instanceof BufferedWriter)
            return (BufferedWriter) obj;
        return null;
    }

    public interface OnGetStringListener {
        void onGetString(String line);

        void onGeted();
    }

    public interface OnHandleStringListener {
        void onHandleString(String line);
        void onHandled(String str);
        boolean onJudgeAfter(String line);
        boolean onJudgeBefore(String line);
    }


    public static void getString(Object obj, OnGetStringListener listener) {
        BufferedReader br;
        try {
            br = createBufferedReader(obj, null);
            if (br != null) {
                while ((line = br.readLine()) != null) {
                    listener.onGetString(line);
                }
                listener.onGeted();
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readOneMoreLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String handleString(Object obj, OnHandleStringListener listener) {
        String str = "";
        try {
            br = createBufferedReader(obj, null);
            if (br != null) {
                while ((line = br.readLine()) != null) {
                    if(listener.onJudgeBefore(line)){
                        break;
                    }
                    listener.onHandleString(line);
                    if(listener.onJudgeAfter(line)){
                        break;
                    }
                }
                listener.onHandled(str);
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String getString(Object obj) {
        BufferedReader br;
        String str = "";
        try {
            br = createBufferedReader(obj, "utf-8");
            if (br != null) {
                while ((line = br.readLine()) != null) {
                    str += line + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }



    public static void writeString(Object obj, String str) {
        BufferedWriter bw;
        try {
            bw = createBufferedWriter(obj);
            if (bw != null) {
                bw.write(str);
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
