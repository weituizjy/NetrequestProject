import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Netrequests {
    public void sendGetNetRequest(String mUrl,Netresult netresult){
        new Thread(
                ()->{
                    try{
                        URL url=new URL(mUrl);
                        HttpURLConnection connection=(HttpURLConnection)
                                url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setRequestProperty("Accept-Language",
                                "zh-cn,zh;q=0.9");
                        connection.setRequestProperty("Accept-Encoding",
                                "gzip,deflate");
                        connection.connect();

                        InputStream in=connection.getInputStream();
                        String responseDate=StreamToString(in);
                        netresult.onSuccess(responseDate);
                    }catch(Exception e){
                        netresult.onError(e);
                    }
                }
        ).start();
    }
    public void sendPostNetrequest(String mUrl, HashMap<String,String> params,
                                   Netresult netresult){
        new Thread(
                ()->{
                    try{
                        URL url=new URL(mUrl);
                        HttpURLConnection connection=(HttpURLConnection)
                                url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        StringBuilder dateToWrite=new StringBuilder();
                        for(String key:params.keySet()){
                            dateToWrite.append(key).append("=")
                                    .append(params.get(key)).append("&");
                        }
                        connection.connect();
                        OutputStream outputStream=connection.getOutputStream();
                        outputStream.write(dateToWrite.substring(0,
                                dateToWrite.length()-1).getBytes());
                        InputStream in=connection.getInputStream();
                        String responseDate=StreamToString(in);
                        netresult.onSuccess(responseDate);
                    }catch (Exception e){
                        netresult.onError(e);
                    }
                }
        ).start();
    }


    private String StreamToString(InputStream in){
        StringBuilder sb=new StringBuilder();
        String oneLine;
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        try{
            while ((oneLine=reader.readLine())!=null){
                sb.append(oneLine).append('\n');
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                in.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public abstract class Netresult{
        public abstract void onSuccess(String str);
        public abstract void onError(Throwable e);
    }
}

