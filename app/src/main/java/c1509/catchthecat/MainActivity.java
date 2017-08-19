package c1509.catchthecat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import c1509.catchthecat.Download;

import static android.content.ContentValues.TAG;
import static android.os.Environment.getExternalStorageDirectory;
import static c1509.catchthecat.R.layout.activity_main;

public class MainActivity extends Activity {
    private long exitTime = 0;
    XCRoundImageView[][] qipan=new XCRoundImageView[5+18][5+18] ;
    int width;
    int axx=11;
    int ayy=11;
    int s[][]=new int[5+axx+5][5+ayy+5];
    int step=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public int toCan(int qb,int bp){
        if(qb>=0&&bp>=0)
            return s[5+qb][5+bp];
        return 1247;
    }
    public static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public void catWin(){
        s[5+cx][5+cy]=0;
        out();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("猫跑出去了！！！");
        builder.setTitle("你输了");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
             System.exit(0);
          }
        });
        builder.setNegativeButton("重来", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              cch();
          }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }
    public void showBuilder(String Title,String Text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Title);
        builder.setMessage(Text);
    }
    public void perWin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String tishi=new String();
        if(step<5) tishi="你是神吗？";
        else if(step<10) tishi="厉害了我的哥！";
        else if(step<20) tishi="可以，很强！";
        else if(step<30) tishi="不错！";
        else if(step<40) tishi="继续努力！";
        else if(step<50) tishi="再加把劲啊！";
        else tishi="你一定没用心玩吧！";
        builder.setMessage("你用"+step+"步把猫围住了，"+tishi);
        builder.setTitle("你赢了");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.setNegativeButton("重来", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cch();
            }
        });

        builder.create();
        builder.setCancelable(false);
        builder.show();
    }
    public void cch(){
        win=0;
        for(int i=0;i<axx;i++)
            for(int j=0;j<ayy;j++)
                s[5+i][5+j]=0;
        cx=axx/2;
        cy=ayy/2;
        s[5+cx][5+cy]=2;
        int startshu=(int)(Math.random()*6+5);
        int addx,addy;
        for(int i=0;i<startshu;i++){
            addx=(int)(Math.random()*axx);
            addy=(int)(Math.random()*ayy);
            while(s[5+addx][5+addy]!=0){
                addx=(int)(Math.random()*axx);
                addy=(int)(Math.random()*ayy);
            }
            s[5+addx][5+addy]=1;
        }
        step=0;
        out();
    }
    public void show(CharSequence a){
        Toast.makeText(getApplicationContext(),a,Toast.LENGTH_LONG).show();
    }
    public void setWidth(ImageView v,int width){
        ViewGroup.LayoutParams para = v.getLayoutParams();
        para.width = width;
        v.setLayoutParams(para);
    }
    public void setHeight(ImageView v,int height){
        ViewGroup.LayoutParams para = v.getLayoutParams();
        para.width = height;
        v.setLayoutParams(para);
    }
    public static String ReadTxtFile(String strFilePath)
    {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            Log.d("TestFile", "The File doesn't not exist.");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                Log.d("TestFile", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }
    public int getversion(){
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);

            // 当前应用的版本名称
            String versionName = info.versionName;

            // 当前版本的版本号
            int versionCode = info.versionCode;

            // 当前版本的包名
            String packageNames = info.packageName;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/
        setContentView(activity_main);
        //EditText html = (EditText) findViewById(R.id.html);
        //http://open23c.boswatch.cn/2017081516bb/2017/08/15/b6ec3e2b5e51339c2c5a5167daf89839.txt?st=WQFVh0gIdGvCM_xjYjvLsg&q=updata.txt&e=1502787685
        File SD=getExternalStorageDirectory();
        final File DOWNLOAD=new File(SD.toString()+"/CatchTheCat");
        final File UpdataTxt=new File(DOWNLOAD+"/updata.txt");
        if(!DOWNLOAD.exists()) DOWNLOAD.mkdirs();
        if(UpdataTxt.exists()) ;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://open23c.boswatch.cn/2017081819bb/2017/08/15/b6ec3e2b5e51339c2c5a5167daf89839.txt?st=VrSOSfRKzBWr_108I7TOrw&q=updata.txt&e=1503056444"));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("正在更新");
        request.setDescription("正在更新");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        //request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,DOWNLOAD+"/CatchTheCat/updata.txt");
        request.setDestinationInExternalPublicDir("CatchTheCat","updata.txt");
        DownloadManager downManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        long id= downManager.enqueue(request);
        Toast.makeText(getApplicationContext(),id+"xs",Toast.LENGTH_LONG);
        String updata=ReadTxtFile(DOWNLOAD+"/CatchTheCat");
        int Enter,newversion=0,len=updata.length();
        char Updata[]=updata.toCharArray();
        for(Enter=0;Enter<len;Enter++){
            if(Updata[Enter]=='\n')
                break;
            newversion=newversion*10+Updata[Enter]-'0';
        }
        int nowversion=getversion();
        if(newversion>nowversion){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("检测到新版本，最新版本为"+newversion+".0"+"请立即更新！\n"+updata.substring(Enter+1,len-1));
            builder.setTitle("更新");
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    DownloadManager.Request r = new DownloadManager.Request(Uri.parse("https://pro-app-qn.fir.im/62781476b5efdaacb1640d93c30e4437077d2b79.apk?attname=%E5%9B%B4%E4%BD%8F%E7%A5%9E%E7%BB%8F%E7%8C%AB.apk_1.0.apk&e=1503065305&token=LOvmia8oXF4xnLh0IdH05XMYpH6ENHNpARlmPc-T:xEixJbSLx2Jmbtm1cW0Njx8meA0="));
                    r.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                    //设置通知栏标题
                    r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    r.setTitle("正在更新");
                    r.setDescription("正在更新");
                    r.setAllowedOverRoaming(false);
                    //设置文件存放目录
                    //r.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS,DOWNLOAD+"/CatchTheCat/updata.txt");
                    r.setDestinationInExternalPublicDir("CatchTheCat","Cat.apk");
                    DownloadManager downManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                    long Apkid=downManager.enqueue(r);
                }
            });
            builder.create();
            builder.show();
        }
        /*DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.heightPixels;     // 屏幕宽度（像素）
        width/=axx;
        LinearLayout q=(LinearLayout)findViewById(R.id.background);
        XCRoundImageView fanhui=(XCRoundImageView) findViewById(R.id.fanhui);
        //RelativeLayout xxbj=(RelativeLayout)findViewById(R.id.xxbj);
        //xxbj.setBackgroundResource(R.drawable.grass);
        setWidth(fanhui,width);
        setHeight(fanhui,width*2);
        fanhui.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cch();
            }
        });
        //params.height=width;
        //params.weight=width;
        cx=axx/2;
        cy=ayy/2;
        s[5+cx][5+cy]=2;
        int startshu=(int)(Math.random()*6+5);
        int addx,addy;
        for(int i=0;i<startshu;i++){
            addx=(int)(Math.random()*axx);
            addy=(int)(Math.random()*ayy);
            while(s[5+addx][5+addy]!=0){
                addx=(int)(Math.random()*axx);
                addy=(int)(Math.random()*ayy);
            }
            s[5+addx][5+addy]=1;
        }
        lu[5+cx][5+cy]++;
        for( int i=0 ; i<axx; i++ ){
            LinearLayout p=new LinearLayout(this);
            p.setGravity(Gravity.CENTER);
            q.addView(p);
            if(i%2==1){
                TextView outside=new TextView(this);
                outside.setWidth(width);
                p.addView(outside);
            }
            cx=axx/2;
            cy=ayy/2;
            for(int j=0;j<ayy;j++) {
                qipan[5+i][5+j] = new XCRoundImageView(this);
                //qipan[5+i][5+j].setText(" ");
                //qipan[5+i][5+j].setWidth(width);
                //qipan[5+i][5+j].setHeight(width);
                //setWidth(qipan[i][j],width);
                //setHeight(qipan[i][j],width);
                ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(width,width);
                qipan[5+i][5+j].setLayoutParams(para);
                //para.width = width;
                //qipan[i][j].setLayoutParams(para);
                qipan[5+i][5+j].setId(i*axx+j);
                //qipan[5+i][5+j].setGravity(Gravity.CENTER);
                qipan[5+i][5+j].setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        int id=view.getId();
                        int x=id/axx;
                        int y=id%ayy;
                        //show("cx:"+cx+"cy:"+cy);
                        if(s[5+x][5+y]==1||(cx==x&&cy==y)) return;
                        step++;
                        if(x>=0&&y>=0&&x<axx&&y<ayy){
                            if(s[5+x][5+y]==0){
                                s[5+x][5+y]=1;
                                whoo=true;
                                if(cx!=0&&cy!=0&&cx!=axx-1&&cy!=ayy-1)
                                    if((
                                            cx%2==0&&
                                                    toCan(cx-1,cy-1)==1&&
                                                    toCan(cx,cy-1)==1&&
                                                    toCan(cx+1,cy-1)==1&&
                                                    toCan(cx-1,cy)==1&&
                                                    s[5+cx][5+cy+1]==1&&
                                                    s[5+cx+1][5+cy]==1
                                    )||
                                            (
                                                    cx%2==1&&
                                                            toCan(cx-1,cy)==1&&
                                                            toCan(cx,cy-1)==1&&
                                                            s[5+cx+1][5+cy]==1&&
                                                            toCan(cx-1,cy+1)==1&&
                                                            s[5+cx][5+cy+1]==1&&
                                                            s[5+cx+1][5+cy+1]==1
                                            )
                                            )
                                    {
                                        perWin();
                                    }
                            }
                            out();
                        }
                        int aa;
                        boolean yess=true;
                        int bfx=cx,bfy=cy;
                        aa=ren_gong_zhi_neng();
                        last=aa;
                        if(win!=0) {
                            return;
                        }
                        if(cx%2==0){
                            if(aa==7&&toCan(cx-1,cy-1)==0){
                                cx--;
                                cy--;
                            }
                            else
                            if(aa==4&&toCan(cx,cy-1)==0)
                                cy--;
                            else
                            if(aa==1&&toCan(cx+1,cy-1)==0){
                                cy--;
                                cx++;
                            }
                            else
                            if(aa==9&&toCan(cx-1,cy)==0)
                                cx--;
                            else
                            if(aa==6&&s[5+cx][5+cy+1]==0)
                                cy++;
                            else
                            if(aa==3&&s[5+cx+1][5+cy]==0)
                                cx++;
                            else
                                yess=false;
                        }
                        else{
                            if(aa==7&&toCan(cx-1,cy)==0){
                                cx--;
                            }
                            else
                            if(aa==4&&toCan(cx,cy-1)==0)
                                cy--;
                            else
                            if(aa==1&&s[5+cx+1][5+cy]==0){
                                cx++;
                            }
                            else
                            if(aa==9&&toCan(cx-1,cy+1)==0){
                                cx--;
                                cy++;
                            }
                            else
                            if(aa==6&&s[5+cx][5+cy+1]==0)
                                cy++;
                            else
                            if(aa==3&&s[5+cx+1][5+cy+1]==0){
                                cx++;
                                cy++;
                            }
                            else
                                yess=false;
                        }
                        if(cx==-1||cy==-1||cx==axx||cy==ayy){
                            win=1;
                            catWin();
                        }
                        if(yess){
                            whoo=false;
                            s[5+bfx][5+bfy]=0;
                            s[5+cx][5+cy]=2;
                            if(cx%2==0){
                                if(toCan(cx-1,cy-1)==1||toCan(cx,cy-1)==1||toCan(cx+1,cy-1)==1||toCan(cx-1,cy)==1||s[5+cx][5+cy+1]==1||s[5+cx+1][5+cy]==1)
                                    lu[5+cx][5+cy]++;
                                lu[5+cx][5+cy]++;
                            }
                            else{
                                if(toCan(cx-1,cy)==1||toCan(cx,cy-1)==1||s[5+cx+1][5+cy]==1||toCan(cx-1,cy+1)==1||s[5+cx][5+cy+1]==1||s[5+cx+1][5+cy+1]==1)
                                    lu[5+cx][5+cy]++;
                                lu[5+cx][5+cy]++;
                            }
                        }
                        out();
                    }
                });
                p.addView(qipan[5+i][5+j]);
            }
        }
        out();*/
    }
    boolean whoo=false;
    int perx,pery;
    int last;
    boolean xs(int a,int b,int c,int d,int e,int f){
        if(a!=2147483647)
            if(a==b||a==c||a==d||a==e||a==f)
                return false;
        if(b!=2147483647)
            if(b==c||b==d||b==e||b==f)
                return false;
        if(c!=2147483647)
            if(c==d||c==e||c==f)
                return false;
        if(d!=2147483647)
            if(d==e||d==f)
                return false;
        if(e!=2147483647)
            if(e==f)
                return false;
        return true;
    }
    int win=0;//0:no 1:cat 2:player
    int cx,cy;

    //0:nothing 1:can't move 2:cat
//11*11
//(0~10)
    int lu[][]=new int[5+axx+5][5+ayy+5];
    int junk[][]=new int[5+axx+5][5+ayy+5];
    boolean hui(int i,int j){
        if(junk[5+i][5+j]==1)
            return false;
        if(i==0||j==0||i==axx-1||j==ayy-1){
            return true;
        }
        junk[5+i][5+j]=1;
        if(i%2==0){
            if(junk[5+i][5+j+1]==0&&j+1<ayy)
                if(hui(i,j+1))
                    return true;
            if(j-1>=0&&junk[5+i][5+j-1]==0)
                if(hui(i,j-1))
                    return true;
            if(j-1>=0&&i-1>=0&&junk[5+i-1][5+j-1]==0)
                if(hui(i-1,j-1))
                    return true;
            if(i-1>=0&&junk[5+i-1][5+j]==0)
                if(hui(i-1,j))
                    return true;
            if(junk[5+i+1][5+j]==0&&i+1<axx)
                if(hui(i+1,j))
                    return true;
            if(j-1>=0&&junk[5+i+1][5+j-1]==0&&i+1<axx)
                if(hui(i+1,j-1))
                    return true;
        }
        else{
            if(junk[5+i][5+j+1]==0&&j+1<ayy)
                if(hui(i,j+1))
                    return true;
            if(j-1>=0&&junk[5+i][5+j-1]==0)
                if(hui(i,j-1))
                    return true;
            if(i-1>=0&&junk[5+i-1][5+j+1]==0&&j+1<ayy)
                if(hui(i-1,j+1))
                    return true;
            if(i-1>=0&&junk[5+i-1][5+j]==0)
                if(hui(i-1,j))
                    return true;
            if(junk[5+i+1][5+j]==0&&i+1<axx)
                if(hui(i+1,j))
                    return true;
            if(junk[5+i+1][5+j+1]==0&&i+1<axx&&j+1<ayy)
                if(hui(i+1,j+1))
                    return true;
        }
        return false;
    }
    boolean ciwo(int a){//Can I Walk Out?
        for(int i=0;i<axx;i++)
            for(int j=0;j<ayy;j++)
                junk[5+i][5+j]=s[5+i][5+j];
        if(cx%2==0){
            if(a==1){
                for(int i=0;i<=cx;i++)
                    junk[5+i][5+cy+(cx-i)/2]=1;
                for(int j=0;j<=cy;j++)
                    junk[5+cx][5+j]=1;
                return hui(cx-1,cy-1);
            }
            if(a==2){
                for(int i=0;i<=cx;i++)
                    junk[5+i][5+cy-(cx-i+1)/2]=1;
                for(int j=cy;j<ayy;j++)
                    junk[5+cx][5+j]=1;
                return hui(cx-1,cy);
            }
            if(a==3){
                for(int i=0;i<=cx;i++)
                    junk[5+i][5+cy+(cx-i)/2]=1;
                for(int i=cx+1;i<axx;i++)
                    junk[5+i][5+cy+(i-cx)/2]=1;
                return hui(cx,cy+1);
            }
            if(a==4){
                for(int i=cx;i<axx;i++)
                    junk[5+i][5+cy-(i-cx+1)/2]=1;
                for(int j=cy;j<ayy;j++)
                    junk[5+cx][5+j]=1;
                return hui(cx+1,cy);
            }
            if(a==5){
                for(int i=cx;i<axx;i++)
                    junk[5+i][5+cy+(i-cx)/2]=1;
                for(int j=0;j<=cy;j++)
                    junk[5+cx][5+j]=1;
                return hui(cx+1,cy-1);
            }
            if(a==6){
                for(int i=0;i<cx;i++)
                    junk[5+i][5+cy-(cx-i+1)/2]=1;
                for(int i=cx;i<axx;i++)
                    junk[5+i][5+cy-(i-cx+1)/2]=1;
                return hui(cx,cy-1);
            }
        }
        else{
            if(a==1){
                for(int i=0;i<=cx;i++)
                    junk[5+i][5+cy+(cx-i+1)/2]=1;
                for(int j=0;j<=cy;j++)
                    junk[5+cx][5+j]=1;
                return hui(cx-1,cy);
            }
            if(a==2){
                for(int i=0;i<=cx;i++)
                    junk[5+i][5+cy-(cx-i)/2]=1;
                for(int j=cy;j<ayy;j++)
                    junk[5+cx][5+j]=1;
                return hui(cx-1,cy+1);
            }
            if(a==3){
                for(int i=0;i<=cx;i++)
                    junk[5+i][5+cy+(cx-i+1)/2]=1;
                for(int i=cx+1;i<axx;i++)
                    junk[5+i][5+cy+(i-cx+1)/2]=1;
                return hui(cx,cy+1);
            }
            if(a==4){
                for(int i=cx;i<axx;i++)
                    junk[5+i][5+cy-(i-cx)/2]=1;
                for(int j=cy;j<ayy;j++)
                    junk[5+cx][5+j]=1;
                return hui(cx+1,cy+1);
            }
            if(a==5){
                for(int i=cx;i<axx;i++)
                    junk[5+i][5+cy+(i-cx+1)/2]=1;
                for(int j=0;j<=cy;j++)
                    junk[5+cx][5+j]=1;
                return hui(cx+1,cy);
            }
            if(a==6){
                for(int i=0;i<cx;i++)
                    junk[5+i][5+cy-(cx-i)/2]=1;
                for(int i=cx;i<axx;i++)
                    junk[5+i][5+cy-(i-cx)/2]=1;
                return hui(cx,cy-1);
            }
        }
        return false;
    }
    int ren_gong_zhi_neng(){
        int zt[]=new int[5+10];
        for(int i=0;i<10;i++) zt[5+i]=0;
        if(cx==0){
            win=1;
            catWin();
            return 7;
        }
        if(cx==axx-1){
            win=1;
            catWin();
            return 1;
        }
        if(cy==0){
            win=1;
            catWin();
            return 4;
        }
        if(cy==ayy-1){
            win=1;
            catWin();
            return 6;
        }
        int a[]=new int[5+10];
        int a2[]=new int[5+10];
        for(int i=0;i<10;i++) a2[5+i]=0;
        int ba[]=new int[5+10];
        a[5+1]=2147483647;
        a[5+2]=2147483647;
        a[5+3]=2147483647;
        a[5+4]=2147483647;
        a[5+5]=2147483647;
        a[5+6]=2147483647;
        if(cx%2==0){
            if(perx==cx-1&&pery==cy-1){
                if(cx>cy&&toCan(cx-1,cy-1)!=0&&(toCan(cx-1,cy-2)==0||cy-2<0)&&ciwo(6)==true)
                    a2[5+1]=4;
                else
                if(toCan(cx-1,cy-1)!=0&&(toCan(cx-2,cy)==0||cx-2<0)&&ciwo(2))
                    a2[5+1]=9;
                else
                if(toCan(cx-1,cy-1)!=0&&(toCan(cx-1,cy-2)==0||cy-2<0)&&ciwo(6))
                    a2[5+1]=4;
            }
            if(cx==0||cy==0||toCan(cx-1,cy-1)==0){
                a[5+1]=0;
                for(int i=0;i<=cx;i++)
                    for(int j=0;j<=cy+(cx-i)/2;j++)
                        a[5+1]+=s[5+i][5+j];
            }
            if(perx==cx-1&&pery==cy){
                if(cx<(ayy-cy)&&toCan(cx-1,cy)!=0&&(toCan(cx-2,cy)==0||cx-2<0)&&ciwo(1))
                    a2[5+2]=7;
                else
                if(toCan(cx-1,cy)!=0&&toCan(cx-1,cy+1)==0&&ciwo(3))
                    a2[5+2]=6;
                else
                if(toCan(cx-1,cy)!=0&&(toCan(cx-2,cy)==0||cx-2<0)&&ciwo(1))
                    a2[5+2]=7;
            }
            if(cx==0||cy==ayy-1||toCan(cx-1,cy)==0){
                a[5+2]=0;
                for(int i=0;i<=cx;i++)
                    for(int j=cy-(cx-i+1)/2;j<ayy;j++)
                        a[5+2]+=s[5+i][5+j];
            }
            if(perx==cx&&pery==cy+1){
                if(axx/2>cx&&s[5+cx][5+cy+1]!=0&&s[5+cx+1][5+cy+1]==0&&ciwo(4))
                    a2[5+3]=3;
                else
                if(s[5+cx][5+cy+1]!=0&&toCan(cx-1,cy+1)==0&&ciwo(2))
                    a2[5+3]=9;
                else
                if(s[5+cx][5+cy+1]!=0&&s[5+cx+1][5+cy+1]==0&&ciwo(4))
                    a2[5+3]=3;
            }
            if(cy==ayy-1||s[5+cx][5+cy+1]==0){
                a[5+3]=0;
                for(int i=0;i<axx;i++)
                    for(int j=((i>cx)?(cy+(i-cx)/2):(cy+(cx-i)/2));j<ayy;j++)
                        a[5+3]+=s[5+i][5+j];
            }
            if(perx==cx+1&&pery==cy+1){
                if(axx-cx>ayy-cy&&s[5+cx+1][5+cy]!=0&&(s[5+cx+2][5+cy]==0||cx+2>=axx)&&ciwo(5))
                    a2[5+4]=1;
                else
                if(s[5+cx+1][5+cy]!=0&&s[5+cx+1][5+cy+1]==0&&ciwo(3))
                    a2[5+4]=6;
                else
                if(s[5+cx+1][5+cy]!=0&&(s[5+cx+2][5+cy]==0||cx+2>=axx)&&ciwo(5))
                    a2[5+4]=1;
            }
            if(cx==axx-1||cy==ayy-1||s[5+cx+1][5+cy]==0){
                a[5+4]=0;
                for(int i=cx;i<axx;i++)
                    for(int j=cy-(i-cx+1)/2;j<ayy;j++)
                        a[5+4]+=s[5+i][5+j];
            }
            if(perx==cx+1&&pery==cy-1){
                if(axx-cx>cy&&toCan(cx+1,cy-1)!=0&&(toCan(cx+1,cy-2)==0||cy-2<0)&&ciwo(6))
                    a2[5+5]=4;
                else
                if(toCan(cx+1,cy-1)!=0&&(s[5+cx+2][5+cy]==0||cx+2>=axx)&&ciwo(4))
                    a2[5+5]=3;
                else
                if(toCan(cx+1,cy-1)!=0&&(toCan(cx+1,cy-2)==0||cy-2<0)&&ciwo(6))
                    a2[5+5]=4;
            }
            if(cx==axx-1||cy==0||toCan(cx+1,cy-1)==0){
                a[5+5]=0;
                for(int i=cx;i<axx;i++)
                    for(int j=0;j<=cy+(i-cx)/2;j++)
                        a[5+5]+=s[5+i][5+j];
            }
            if(perx==cx&&pery==cy-1){
                if(cx<axx/2&&toCan(cx,cy-1)!=0&&(toCan(cx-1,cy-2)==0||cy-2<0)&&ciwo(1))
                    a2[5+6]=7;
                else
                if(toCan(cx,cy-1)!=0&&(toCan(cx+1,cy-2)==0||cy-2<0)&&ciwo(5))
                    a2[5+6]=1;
                else
                if(toCan(cx,cy-1)!=0&&(toCan(cx-1,cy-2)==0||cy-2<0)&&ciwo(1))
                    a2[5+6]=7;
            }
            if(cy==0||toCan(cx,cy-1)==0){
                a[5+6]=0;
                for(int i=0;i<axx;i++)
                    for(int j=0;j<=((cx>i)?(cy-(cx-i+1)/2):(cy-(i-cx+1)/2));j++)
                        a[5+6]+=s[5+i][5+j];
            }
        }
        else{
            if(perx==cx-1&&pery==cy){
                if(cx>cy&&toCan(cx-1,cy)!=0&&toCan(cx-1,cy-1)==0&&ciwo(6))
                    a2[5+1]=4;
                else
                if(toCan(cx-1,cy)!=0&&(toCan(cx-2,cy)==0||cx-2<0)&&ciwo(2))
                    a2[5+1]=9;
                else
                if(toCan(cx-1,cy)!=0&&toCan(cx-1,cy-1)==0&&ciwo(6))
                    a2[5+1]=4;
            }
            if(cx==0||cy==0||toCan(cx-1,cy)==0){
                a[5+1]=0;
                for(int i=0;i<=cx;i++)
                    for(int j=0;j<=cy+(cx-i+1)/2;j++)
                        a[5+1]+=s[5+i][5+j];
            }
            if(perx==cx-1&&pery==cy+1){
                if(cx<(ayy-cy)&&toCan(cx-1,cy+1)!=0&&(toCan(cx-1,cy+2)==0||cy+2>=ayy)&&ciwo(3))
                    a2[5+2]=6;
                else
                if(toCan(cx-1,cy+1)!=0&&(toCan(cx-2,cy)==0||cx-2<0)&&ciwo(1))
                    a2[5+2]=7;
                else
                if(toCan(cx-1,cy+1)!=0&&(toCan(cx-1,cy+2)==0||cy+2>=ayy)&&ciwo(3))
                    a2[5+2]=6;
            }
            if(cx==0||cy==ayy-1||toCan(cx-1,cy+1)==0){
                a[5+2]=0;
                for(int i=0;i<=cx;i++)
                    for(int j=cy-(cx-i)/2;j<ayy;j++)
                        a[5+2]+=s[5+i][5+j];
            }
            if(perx==cx&&pery==cy+1){
                if(axx/2>cx&&s[5+cx][5+cy+1]!=0&&toCan(cx-1,cy+1)==0&&ciwo(2))
                    a2[5+3]=9;
                else
                if(s[5+cx][5+cy+1]!=0&&s[5+cx+1][5+cy+1]==0&&ciwo(4))
                    a2[5+3]=3;
                else
                if(s[5+cx][5+cy+1]!=0&&toCan(cx-1,cy+1)==0&&ciwo(2))
                    a2[5+3]=9;
            }
            if(cy==ayy-1||s[5+cx][5+cy+1]==0){
                a[5+3]=0;
                for(int i=0;i<axx;i++)
                    for(int j=((i>cx)?(cy+(i-cx+1)/2):(cy+(cx-i+1)/2));j<ayy;j++)
                        a[5+3]+=s[5+i][5+j];
            }
            if(perx==cx+1&&pery==cy+1){
                if(axx-cx>ayy-cy&&s[5+cx+1][5+cy+1]!=0&&(s[5+cx+1][5+cy+2]==0||cy+2>=ayy)&&ciwo(3))
                    a2[5+4]=6;
                else
                if(s[5+cx+1][5+cy+1]!=0&&(s[5+cx+2][5+cy]==0||cx+2>=axx)&&ciwo(5))
                    a2[5+4]=1;
                else
                if(s[5+cx+1][5+cy+1]!=0&&(s[5+cx+1][5+cy+2]==0||cy+2>=ayy)&&ciwo(3))
                    a2[5+4]=6;
            }
            if(cx==axx-1||cy==ayy-1||s[5+cx+1][5+cy+1]==0){
                a[5+4]=0;
                for(int i=cx;i<axx;i++)
                    for(int j=cy-(i-cx)/2;j<ayy;j++)
                        a[5+4]+=s[5+i][5+j];
            }
            if(perx==cx-1&&pery==cy-1){
                if(axx-cx>cy&&s[5+cx+1][5+cy]!=0&&toCan(cx+1,cy-1)==0&&ciwo(6))
                    a2[5+5]=4;
                else
                if(s[5+cx+1][5+cy]!=0&&(s[5+cx+2][5+cy]==0||cx+2>=axx)&&ciwo(4))
                    a2[5+5]=3;
                else
                if(s[5+cx+1][5+cy]!=0&&toCan(cx+1,cy-1)==0&&ciwo(6))
                    a2[5+5]=4;
            }
            if(cx==axx-1||cy==0||s[5+cx+1][5+cy]==0){
                a[5+5]=0;
                for(int i=cx;i<axx;i++)
                    for(int j=0;j<=cy+(i-cx+1)/2;j++)
                        a[5+5]+=s[5+i][5+j];
            }
            if(perx==cx&&pery==cy-1){
                if(cx<axx/2&&toCan(cx,cy-1)!=0&&toCan(cx-1,cy-1)==0&&ciwo(1))
                    a2[5+6]=7;
                else
                if(toCan(cx,cy-1)!=0&&toCan(cx+1,cy-1)==0&&ciwo(5))
                    a2[5+6]=1;
                else
                if(toCan(cx,cy-1)!=0&&toCan(cx-1,cy-1)==0&&ciwo(1))
                    a2[5+6]=7;
            }
            if(cy==0||toCan(cx,cy-1)==0){
                a[5+6]=0;
                for(int i=0;i<axx;i++)
                    for(int j=0;j<=((cx>i)?(cy-(cx-i)/2):(cy-(i-cx)/2));j++)
                        a[5+6]+=s[5+i][5+j];
            }
        }
        for(int i=1;i<7;i++)
            if(a2[5+i]!=0)
                return a2[5+i];
        for(int i=1;i<7;i++)
            if(a[5+i]==2147483647)
                zt[5+i]=100;
        int minn=2147483647;
        ba[5+1]=a[5+1];
        ba[5+2]=a[5+2];
        ba[5+3]=a[5+3];
        ba[5+4]=a[5+4];
        ba[5+5]=a[5+5];
        ba[5+6]=a[5+6];
        if(ciwo(1)==false){
            ba[5+1]=2147483647;
        }
        if(ciwo(2)==false){
            ba[5+2]=2147483647;
        }
        if(ciwo(3)==false){
            ba[5+3]=2147483647;
        }
        if(ciwo(4)==false){
            ba[5+4]=2147483647;
        }
        if(ciwo(5)==false){
            ba[5+5]=2147483647;
        }
        if(ciwo(6)==false){
            ba[5+6]=2147483647;
        }
        for(int i=1;i<7;i++)
            if(ba[5+i]==2147483647)
                zt[5+i]+=30;
        if(last==7)
            zt[5+4]+=10;
        if(last==9)
            zt[5+5]+=10;
        if(last==6)
            zt[5+6]+=10;
        if(last==3)
            zt[5+1]+=10;
        if(last==1)
            zt[5+2]+=10;
        if(last==4)
            zt[5+3]+=10;
        int minnn=2147483647,minwei=0,same=0;
        for(int i=1;i<7;i++)
            if(zt[5+i]<100){
                if(zt[5+i]<minnn){
                    minnn=zt[5+i];
                    minwei=i;
                    same=0;
                }
                if(zt[5+i]==minnn)
                    same++;
            }
        if(same==0){
            if(minwei==1)
                return 7;
            if(minwei==2)
                return 9;
            if(minwei==3)
                return 6;
            if(minwei==4)
                return 3;
            if(minwei==5)
                return 1;
            if(minwei==6)
                return 4;
        }
        for(int i=1;i<7;i++)
            if(zt[5+i]!=minnn)
                a[5+i]=2147483647;
        minnn=2147483647;
        same=0;
        for(int i=1;i<7;i++){
            if(a[5+i]<minnn){
                minnn=a[5+i];
                minwei=i;
                same=0;
            }
            if(a[5+i]==minnn)
                same++;
        }
        if(minwei==1)
            return 7;
        if(minwei==2)
            return 9;
        if(minwei==3)
            return 6;
        if(minwei==4)
            return 3;
        if(minwei==5)
            return 1;
        if(minwei==6)
            return 4;
        return (int)Math.random()*6+1;
    }
    /*
     * 获取控件宽
     */
    public static int getWidth(View view)
    {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }
    /*
     * 获取控件高
     */
    public static int getHeight(View view)
    {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());
    }

    /*
     * 设置控件所在的位置X，并且不改变宽高，
     * X为绝对位置，此时Y可能归0
     */
    public static void setLayoutX(View view,int x) {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,margin.topMargin, x+margin.width, margin.bottomMargin);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }
    /*
     * 设置控件所在的位置Y，并且不改变宽高，
     * Y为绝对位置，此时X可能归0
     */
    public static void setLayoutY(View view,int y) {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }
    /*
     * 设置控件所在的位置YY，并且不改变宽高，
     * XY为绝对位置
     */
    public static void setLayout(View view,int x,int y) {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, x+margin.width, y+margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }
    public void out(){
        int last=7;
        for(int i=0;i<axx;i++){
            for(int j=0;j<ayy;j++){
                //qipan[5+i][5+j].setTextSize((float)(width/2.6));
                if(s[5+i][5+j]==0){
                    if(last!=10+7*16){
                        last=10+7*16;
                    }
                    qipan[5+i][5+j].setBackgroundResource(R.drawable.kong);
                    //qipan[5+i][5+j].getBackground().setAlpha(100);
                }
                else
                if(s[5+i][5+j]==1){
                    if(last!=3+7*16){
                        last=3+7*16;
                    }
                    qipan[5+i][5+j].setBackgroundResource(R.drawable.zhuzi);
                    //qipan[5+i][5+j].getBackground().setAlpha(100);
                }
                else{
                    if(last!=0+7*16){
                        last=0+7*16;
                    }
                    qipan[5+i][5+j].setBackgroundResource(R.drawable.cat);
                    //qipan[5+i][5+j].getBackground().setAlpha(100);
                }
            }
        }
    }
}
