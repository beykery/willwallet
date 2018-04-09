package org.beykery.eth;

import org.web3j.crypto.WalletFile;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

public class Application
{
    /**
     * 测试
     *
     * @param args
     */
    public static final void main(String... args) throws Exception
    {

        while (true)
        {
            WillWallet wa = WillWallet.createWithMnemonic(null, "m/44'/60'/0'/0/0");
            String mnemonic = wa.getMnemonic();
            String pk = wa.getPrivateKey();
            String addr = wa.getAddress();

            System.out.println(mnemonic);
            System.out.println(pk);
            System.out.println(addr);
        }

    }


    //导入钱包
//    String mn="comfort library salmon rocket outer measure sugar media museum still anchor spare";
//    WillWallet wa = WillWallet.fromMnemonic(null, mn, "m/44'/60'/0'/0/0");
//    System.out.println(wa.getMnemonic());
//    System.out.println(wa.getPrivateKey());
//    System.out.println(wa.getAddress());

}
