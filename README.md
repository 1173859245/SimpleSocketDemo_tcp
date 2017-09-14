# SimpleSocketDemo_tcp
Socket通讯基于tcp协议，实行服务端与客户端互相通讯demo的简单实现
//服务端代码如下------------------------------------------MyEclipse
public class SoketTest implements Runnable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       Thread th=new Thread(new SoketTest());
       th.start();
       //192.168.0.119
	}
   public void Socket() {
	   try {  
           System.out.println("S: Connecting...");  
            //指定端口为8888
           ServerSocket serverSocket = new ServerSocket(8888);  
           while (true) {  
               // 等待接受客户端请求   
               Socket client = serverSocket.accept();  
                               
               try {  
                   // 接受客户端信息  
                   BufferedReader in = new BufferedReader(  
                           new InputStreamReader(client.getInputStream(),"UTF-8"));  
                     
                   // 发送给客户端的消息   
                   PrintWriter out = new PrintWriter(new BufferedWriter(  
                     new OutputStreamWriter(client.getOutputStream(),"UTF-8")),true);  
                                      
                   String str = in.readLine(); // 读取客户端的信息  
                  
                   if (str != null ) {  
                       // 设置输入信息，接收客户端接收的信息再输入给客户端  
                	   System.out.println("客户端对你说"+str);  
                	   Scanner sc=new Scanner(System.in);
                	   System.out.println("请输入对客户端想说的话");
                	   String msg=sc.nextLine();
                       out.println("服务端说:" + msg);  
                       out.flush();  
                         
                       // 把客户端发送的信息保存到文件中  
//                       java.io.File file = new java.io.File("C://android.txt");  
//                       FileOutputStream fops = new FileOutputStream(file);   
//                       byte [] b = str.getBytes();  
//                       for ( int i = 0 ; i < b.length; i++ )  
//                       {  
//                           fops.write(b[i]);  
//                       }  
                       
                   } else {  
                       System.out.println("Not receiver anything from client!");  
                   }  
               } catch (Exception e) {  
                   System.out.println("S: Error 1");  
                   e.printStackTrace();  
               } finally {  
                   client.close();  
                   System.out.println("S: Done.");  
               }  
           }  
       } catch (Exception e) {  
           System.out.println("S: Error 2");  
           e.printStackTrace();  
		    
	}
   }
	public void run() {
		// TODO Auto-generated method stub
		Socket();
	}

}

//安卓端主要代码如下------------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private TextView textView;
    private String getServerMsg;
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message message) {

            switch(message.what)
            {
                case 1:
                    textView.setText(getServerMsg);
                    break;
                default:
                    break;
            }
            super.handleMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info=editText.getText().toString();
                if (TextUtils.isEmpty(info)){
                    Toast.makeText(MainActivity.this,"输入内容不能为空",Toast.LENGTH_LONG).show();
                }else {
                    socketTest();

                }
            }
        });


    }
    private void socketTest(){

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                    /* 指定Server的IP地址，此地址为局域网地址，如果是使用WIFI上网，则为PC机的WIFI IP地址
                     * 在ipconfig查看到的IP地址如下：
                     * Ethernet adapter 无线网络连接:
                     * Connection-specific DNS Suffix  . : IP Address. . . . . . . . . . . . : 192.168.1.100
                     */
                try {
                    InetAddress serverAddr=InetAddress.getByName("192.168.0.119");// TCPServer.SERVERIP

                    socket = new Socket(serverAddr,8888);
                    // String message="安卓socket测试";
                    //将信息通过PrintWrite发送给服务
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),
                            true);
                    //用户输入内容发送给server
                    String message = editText.getText().toString();
                    out.println(message);
                    out.flush();
                    //-----开始接收服务端的消息
                    BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                     getServerMsg=in.readLine();
                    Message msg=new Message();
                    msg.what=1;
                    mHandler.sendMessage(msg);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();



    }

    private void initView() {
        editText=(EditText)findViewById(R.id.edtext);
        button=(Button)findViewById(R.id.send);
        textView=(TextView)findViewById(R.id.textView);


    }
}


