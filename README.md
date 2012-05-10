broadcasting-IP                                                                                                                         
===============

this code is used to push the ip to MIRACLE master server.

Usage:
  Server:
    InfoSocket.java
    cluster.conf
  Client:
    InfoSocketClient.java
    master.conf

Flow:
  This code is used to push client-side IP to server.
  
  First, InfoSocketClient will retrieve property-"masterIP" from 
  master.conf, and InfoSocketClient will transfer IP-address to 
  server.
  
  Second, InfoSocket will get the IP information, and save it 
  into cluster.conf.

Step:
  0. git clone these file.

  1. modify property.
    edit master.conf and set the appropriate setting.
  
  2. move InfoSocket.java and cluster.conf to server side, also move 
    InfoSocketClient.java and master.conf to client side.

  3. compile all file and check whether you have the authority to read/write file.

  4. run InfoSocket first, and run InfoSocketClient second.