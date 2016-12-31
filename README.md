# TCP/IP Socket

--------------------------------------------------

## Network Programming
![](http://dl.dropbox.com/s/3nire3a5rq6co3f/TCP.png)

**네트워크로 연결된 컴퓨터 (Host) 사이의 데이터를 주고받는 방식과 Application 설계를 의미.**  
Socket 기반으로 프로그래밍을 하므로 Socket 프로그래밍라고도 함.

--------------------------------------------------

## Network Connection Tool! Socket
![](http://dl.dropbox.com/s/uo8hwluo7lmmebe/Socket.jpg)

**네트워크로 서로 연결되고 데이터를 주고 받을 수 있는 Network Connection Tool.**

**운영체제가 제공하는 표준화 된 Software Module.**  
(데이터 통신의 물리적 Mechanism과 Protocol **Wrapping**)

Socket은 데이터 전송에 대한 물리, 소프트웨어적인 자세한 지식들을 신경쓰지 않도록 한다.  
그러므로 개발자는 Soket으로 데이터 전송만 하면 된다.

> **Socket**  
> Network로 접속을 하는 도구라는 의미

--------------------------------------------------

## TCP/IP Socket과 스마트폰

LTE망에 연결된 스마트폰이 있고, 스마트폰은 LTE망을 통해서 다른 스마트폰과 전화하거나 데이터 교환 가능.    
**LTE 통신망 접속으로 다른 스마트폰으로 연결을 요청하고 통신한다. (Direct Connection X)**

스마트폰은 LTE 통신망으로 접속하는 도구이으로 Socket이다.  
스마트폰 사용 시, 내부적으로 어떠한 방식으로 연결되고 어떤 기지국을 거치는지 User는 신경쓰지 않는다.

1. 스마트폰 구매 → **Socket 생성**
2. 전화번호 할당 → **IP Address 할당**
3. 전화 수/발신 → **Data 송/수신**

> Socket은 OS가 생성 및 관리하고 Application이 OS에 요청.

--------------------------------------------------

## IP Address

Java는 IP Address를 ```java.net.InetAddress``` Instance로 표현한다.  
Local 컴퓨터의 IP Address 만이 아니라, DNS에서 검색한 IP Address 반환

#### ```getLocalHost()```
Local 컴퓨터 IP Address의 InetAddress Instance 반환

#### ```getByName(String host)```
Host의 DomainName으로 DNS 상의 단 하나의 IP Address를 얻고 InetAddress Instance 반환

#### ```getByAllName(String Host)```
연결 Client가 많은 Server의 경우 과부하 차단 목적으로 다수의 IP Address를 등록하고 운영하므로    
Host의 DomainName으로 DNS 상의 등록된 모든 IP Address 반환

#### ```getHostAddress()```
InetAddress Instance로 부터 String으로 구성된 IP Address 반환

--------------------------------------------------

## TCP (Transmission Control Protocol)
**연결 지향적 서비스 지원 Protocol**  
**Client 그리고 Server가 연결되고 데이터를 주고받는 Protocol**

* Client가 연결을 요청하고 Server가 연결 요청을 허용하면 통신 선로가 고정
* 고정된 통신 선로로 데이터가 순차적으로 전송된다. 그러므로 TCP는 데이터를 정확히 전송한다.
* 그러나 반드시 연결이 형성되어야 하고, 연결 과정의 시간이 가장 많이 걸린다.   
  고정된 통신 선로가 최단선이 아니면 UDP (User Datagram Protocol) 보다 데이터 전송속도가 느릴 수 있다.

--------------------------------------------------

## ```ServerSocket``` vs ```Socket```

**TCP Server는 Client가 연결 요청 시 연결을 허용하고 연결된 Client로 데이터 통신을 담당한다.**      
Java는 이 두 담당을 별도의 Class로 설계.

#### ```ServerSocket```
**Client의 연결 요청을 기다리면서 연결 허용을 담당하는 Class**  
Client가 연결을 요청하면 ```ServerSocket```은 연결을 허용하고 ```Socket``` Instance 반환.

#### ```java.net.Socket```
**연결된 Client와 데이터 통신을 담당하는 Class**  
```IOStream```으로 데이터 통신

--------------------------------------------------

## ```ServerSocket``` 주소 할당 및 연결
스마트폰의 전화번호가 할당되듯이, Socket의 주소정보 할당.  
**Socket의 주소정보는 IP + Port로 구성**

```java
ServerSocket serverSocket = new ServerSocket(5001); // Constructor
```
```java
ServerSocket serverSocket = new ServerSocket(); // Default Constructor
serverSocket.bind(new InetSocketAddress(5001));
// serverSocket.bind(new InetSocketAddress("localhost", 5001));
```

* ```ServerSocket```의 Port 변호를 지정한다.
* Server는 Client가 접속할 Binding Port를 할당받아야 한다.
* Server 구동 시 Client는 Server의 IP Address와 Port 번호로 Socket을 생성해서 연결 요청.

> ServerSocket 생성 시 Port가 이미 다른 Application으로 사용 중이면 ```BindException```이 던져짐.

--------------------------------------------------

## 연결 요청이 가능한 상태의 ```ServerSocket```
**연결 요청이 가능한 상태는 데이터를 받을 수 있는 상태를 의미**

## 연결 요청의 허용
**연결 요청이 허용되어야 데이터 송/수신이 가능하다.**

```java
// Connection Wait for Multiple Client
while(true) {
    System.out.println("Connetion wait..");
    Socket socket = serverSocket.accept();
    InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
    System.out.println("Connection Accepted! " + isa.getHostName());
}
```

* ```accept()``` Method로 연결요청 허용 및 연결.  
  다만 연결요청이 있을 때만 ```accept()``` Method가 Client와 통신할 ```Socket``` Instance 반환.
* ```accept()``` Method는 Client가 연결 요청 하기 전까지 Thread가 Blocking으로 대기한다.  
  그러므로 UI나 Event 처리 Thread 상의 호출 금지.

--------------------------------------------------

## ```ServerSocket``` 닫기

```accept()``` Method로 Thread가 Blocking 시 ```ServerSocket``` 닫기의  
```close()``` Method를 호출하면 ```SocketException```이 던져지기 때문에 예외처리.

```java
try {
    Socket socket = serverSocket.accept();
    ...
} catch() {
    e.printStackTrace();
}

if(!serverSocket.isClosed()) {
    try {
    	serverSocket.close();
    } catch (IOException e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    }
}
```

--------------------------------------------------

## ```Socket``` 생성과 연결 요청

**Client가 Server로 연결 요청을 하려면 ```java.net.Socket``` Class로 연결한다.**    
```Socket``` Instance 생성과 동시에 연결을 요청하려면   
Constructor Parameter로 Server의 IP Address와 Binding Port 번호 전달.

```java
try {
    Socket socket = new Socket("localhost", 5001);
} catch (UnknownHostException e) {
    // TODO Auto-generated catch block
    // IP Address가 잘못되었을 경우
    e.printStackTrace();
} catch (IOException e) {
    // TODO Auto-generated catch block
    // 해당 Port의 Server로 연결할 수 없는 경우
    e.printStackTrace();
}
```

```Socket``` 생성과 동시로 연결 요청을 하지 않고, Dafault Constructor로 ```Socket```을 생성하고  
```connect()``` Method로 연결 요청이 가능하다.

```java
try {
    Socket socket = new Socket();
    socket.connect(new InetSocketAddress("localhost", 5001));
} catch (UnknownHostException e) {
	// TODO Auto-generated catch block
	// IP 주소가 잘못되었을 경우
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	// 해당 Port의 Server에 연결할 수 없는 경우
	e.printStackTrace();
}
```

```connet()``` Method나 ```Socket``` Constructor는 Server의 연결까지 Thread가 Blocking으로 대기한다.   
그러므로 UI나 Event 처리 Thread에서 호출 금지.

--------------------------------------------------

## ```Socket``` 닫기

Server 연결 이후 Client의 Application 종료나, 강제적으로 연결을 끊고 싶다면  ```close()``` Method 호출.   
다만 ```IOException```이 던져지기 때문에 예외처리.

```java
if(!socket.isClosed()) {
  try {
	   socket.close();
  } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
  }
}
```

--------------------------------------------------

## ```Socket``` Data Communication
**```Socket```의 데이터 송/수신은 양방향**  
서로의 ```Socket``` Instance로 부터 각각 ```InputSteam```과 ```OutputStream``` Instance 반환.  
(Client가 연결 요청, Server가 연결 요청 허용 시)   

```java
InputStream is = socket.getInputStream();
OutputStream os = socket.getOutputStream();    
```

#### Data Transmitted
```java
try {
    byte[] byteArr = null;
    String msg = "Hello";

    byteArr = msg.getBytes("UTF-8");
    os.write(byteArr);
    os.flush();
    System.out.println("Data Transmitted OK!");

    os.close();
} catch(IOException e) {
    e.printStackTrace();
}
```

#### Data Received
```java
try {
    byte[] byteArr = new byte[512];
    int readByteCount = is.read();

    String msg = new String(byteArr, 0, readByteCount, "UTF-8");
    System.out.println("Data Received OK!");
    System.out.println("Message : " + msg);

    is.close();
} catch(IOException e) {
    e.printStackTrace();
}
```
--------------------------------------------------

## Server

**Client의 연결 요청을 허용하고, 데이터 및 서비스를 제공하는 Application 또는 Host**  
**Server의 생성된 Socket을 가리켜 Server Socket 또는 Listening Socket이라고 한다.**

--------------------------------------------------

## Model

#### Server/Client Model
하나의 Server와 다수의 Client로 구성되는 Model

#### P2P (Peer To Peer) Model
Application이 Server 또는 Client로 구동되는 Model  ㄴ  
먼저 접속을 시도한 Host가 Client로 지정

--------------------------------------------------
