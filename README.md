# willwallet
以太坊钱包

###坐标


`  
<dependency>
     <groupId>org.beykery</groupId>
     <artifactId>willwallet</artifactId>
     <version>1.0.5</version>
   </dependency>
 `


###使用

             WillWallet wa = WillWallet.createWithMnemonic(null, "m/44'/60'/0'/0/0");
             String mnemonic = wa.getMnemonic();
             String pk = wa.getPrivateKey();
             String addr = wa.getAddress();
 
             System.out.println(mnemonic);
             System.out.println(pk);
             System.out.println(addr);
             


