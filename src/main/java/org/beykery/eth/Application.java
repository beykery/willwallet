package org.beykery.eth;

import org.web3j.abi.TypeDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Application {
    /**
     * 测试
     *
     * @param args
     */
    public static final void main(String... args) throws Exception {

//刻点历史上去
        String data = "历史上的今天\n马其顿亚历山大大帝诞生";
        String url = "https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
        WillContract contract = new WillContract(url);
        String pvk = "xxxxxxxxxxxxxx";
        String address = "0x8aCc161acB2626505755bBF36184841B8c099806";
        String to = "0x5ef1A0B8F3EEFBc31f363cfc3A3590B29c825688";
        WillWallet wallet = new WillWallet(pvk);
        BigInteger nonce = contract.nonce(address);
        BigInteger price = new BigInteger("4000000000");
        BigInteger limit = new BigInteger("1500000");
        BigInteger value = new BigInteger("100000000000000");
        RawTransaction tr = RawTransaction.createTransaction(nonce, price, limit, to, value, Numeric.toHexString(data.getBytes("utf-8")));
        String hex = wallet.signContractTransaction(tr);
        String ret = contract.sendTransaction(hex);
        System.out.println(ret);

//        String tx = "0xf86b0385028c2d17a0825209949ea141cddd5c3ccde602f76b771cf4f154c95d1d865af3107a400080819ea077a9e1dd761fb2e4c132663fec01431f5f2014ec18a428c645df174d82c7b04aa065ffe1604c15e27c1b0aefeb58cf734e969fad41daedc48b7eee60670a299821";
//
//        //String url = "https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
//        //String url = "http://172.18.1.84:8334";
//        String url = "http://172.18.1.84:8334";
//        WillContract contract = new WillContract(url);
//        String ret = contract.sendTransaction(tx);
//        System.out.println(ret);


//        String url = "https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
//        String pvk = "xxxxxxxxxxxxxxxx";
//        String to = "0x9Ae269f41b3922948877905731c9D25c070b1bfd";
//        String contractAddress = "0xcada12e8a6d51fdc7b824ee6ade22c853947875b";
//        BigInteger token = new BigInteger("10000");
//        BigInteger price = new BigInteger("1000000000");
//        BigInteger limit = new BigInteger("150000");
//
//        BigInteger value=BigInteger.ZERO;
//        WillWallet wallet = new WillWallet(pvk);
//        WillContract contract = new WillContract(url);
//        System.out.println(contract.nonce(to));
//        Function transfer = WillContract.transfer(to, token);
//        String ret = contract.execute(wallet.getCredentials(), transfer, contractAddress, price, limit, value);
//        System.out.println(ret);
//        System.out.println(contract.nonce(to));


//        String url="https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
//        String txhash="0xa7af9f0455d5944502367fac09dd9d83b4b54a1c23f7c323d50f67bdb576307a";
//        Web3j web3j=Web3j.build(new HttpService(url));
//       EthTransaction tr= web3j.ethGetTransactionByBlockHashAndIndex("0x0576d4933058950d5407591b741cf2ff246e9eccda2652bdec396519a731c1ac",new BigInteger("121")).send();


//        String inputData = "0xa9059cbb0000000000000000000000005c5212ed85cc957c6b656d209a7be8812ab00e330000000000000000000000000000000000000000000000008d8dadf544fc0000";
//        String method = inputData.substring(0,10);
//        System.out.println(method);
//        String to = inputData.substring(10,74);
//        String value = inputData.substring(74);
//        Method refMethod = TypeDecoder.class.getDeclaredMethod("decode",String.class,int.class,Class.class);
//        refMethod.setAccessible(true);
//        System.out.println(refMethod);
//        Address address = (Address)refMethod.invoke(null,to,0,Address.class);
//        System.out.println(address.toString());
//        Uint256 amount = (Uint256) refMethod.invoke(null,value,0,Uint256.class);
//        System.out.println(amount.getValue());


//        String url = "https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
//        String address = "0x8ACc161acB2626505755bBF36184841B8c099806";
//        String contractAddress = "0x25fe9c458Eb3f74C2c6B5398cF1aE0d302c84768";
//        String ret = erc20BalanceOf(address, contractAddress);
//        System.out.println(ret);

//        Erc20CommonContract commonContract = new Erc20CommonContract(contractAddress, url);
//        String name = commonContract.name().send();
//        System.out.println(name);
//        BigInteger decimal=commonContract.decimals().send();
//        System.out.println(decimal);
//        String symbol=commonContract.symbol().send();
//        System.out.println(symbol);
//        BigInteger total=commonContract.totalSupply().send();
//        System.out.println(total);
    }


    /**
     * 查询erc20代币数量
     *
     * @param address  地址
     * @param contract 合约地址
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static String erc20BalanceOf(String address, String contract) throws ExecutionException, InterruptedException {
        String url = "https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
        WillContract con = new WillContract(url);
        try {
            List<Type> ret = con.call(address, contract, "balanceOf", Collections.singletonList(new Address(address)),
                    Collections.singletonList(new TypeReference<Uint256>() {
                    }));
            if (!ret.isEmpty()) {
                Type t = ret.get(0);
                return t.getValue().toString();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
