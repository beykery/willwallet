package org.beykery.eth;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Arrays;
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


//        String address = "0x8aCc161acB2626505755bBF36184841B8c099806";
//        String contract = "0x6f259637dcd74c767781e37bc6133cd6a68aa161";
//        String ret = erc20BalanceOf(address, contract);
//        System.out.println(ret);


//        String url = "https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
//        String address="0x847b4b242c47fe148d03e5e2d78ce9e1f6571bb3";
//        String contractAddress="0xe6824483e279d967ea6f8472ace7585862fa1185";
//        String ret=erc20BalanceOf(address,contractAddress);
//        System.out.println(ret);

        //String address="0x8aCc161acB2626505755bBF36184841B8c099806";
//         String address="0x847b4b242c47fe148d03e5e2d78ce9e1f6571bb3";
        //String address="0x536ccE7DDE227F2e65EC1FB2b49a5Fc3DA64d99b";
        // String url = "http://172.18.1.84:8334";
//        WillContract contract = new WillContract(url);
//        BigInteger nonce = contract.nonce(address);
//        System.out.println(nonce);

//刻点历史上去
//        String data = "2020年2月7日凌晨，文亮医生因感染新型冠状病毒，病重经抢救无效去世，享年34岁。为众人抱薪者，不可使其冻毙于风雪；为自由开道者，不可令其困厄于荆棘。秦人不暇自哀，而后人哀之；后人哀之而不鉴之，亦使后人而复哀后人也。";
//        String url = "https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
//        WillContract contract = new WillContract(url);
//        String pvk = "xxxxxxxx";
//        String address = "0x8aCc161acB2626505755bBF36184841B8c099806";
//        String to = "0x5ef1A0B8F3EEFBc31f363cfc3A3590B29c825688";
//        WillWallet wallet = new WillWallet(pvk);
//        BigInteger nonce = contract.nonce(address);
//        BigInteger price = new BigInteger("4000000000");
//        BigInteger limit = new BigInteger("1500000");
//        BigInteger value = new BigInteger("100000000000000");
//        RawTransaction tr = RawTransaction.createTransaction(nonce, price, limit, to, value, Numeric.toHexString(data.getBytes("utf-8")));
//        String hex = wallet.signContractTransaction(tr);
//        String ret = contract.sendTransaction(hex);
//        System.out.println(ret);

//        String tx = "0xf86b0385028c2d17a0825209949ea141cddd5c3ccde602f76b771cf4f154c95d1d865af3107a400080819ea077a9e1dd761fb2e4c132663fec01431f5f2014ec18a428c645df174d82c7b04aa065ffe1604c15e27c1b0aefeb58cf734e969fad41daedc48b7eee60670a299821";
//
//        //String url = "https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
//        //String url = "http://172.18.1.84:8334";
//        String url = "http://172.18.1.84:8334";
//        WillContract contract = new WillContract(url);
//        String ret = contract.sendTransaction(tx);
//        System.out.println(ret);


//        String url = "https://mainnet.infura.io/iaosV7jPAld2pmo3he27";
//        String pvk = "xxxxxxxxxxx";
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

        burn();

    }

    private static void burn() throws Exception {
        String pvk = "xxx";
        String address = "0x8aCc161acB2626505755bBF36184841B8c099806";
        String node = "https://bsc-dataseed.binance.org";
        String contractAddress = "0x84e087d4be5928c219b9dea8e087b8b737036f44";
        String mdx = "0x9c65ab58d8d978db963e63f2bfb7121627e3a739";
        BigInteger amount = one(18).multiply(BigInteger.valueOf(1));
        WillContract contract = new WillContract(node);
        String to = contractAddress;
        WillWallet wallet = new WillWallet(pvk);
        BigInteger nonce = contract.nonce(address);
        BigInteger price = new BigInteger("500000000000"); // 500gwei
        BigInteger limit = new BigInteger("123403");
        BigInteger value = new BigInteger("0");
        List<String> list = Arrays.asList("555", "33", "66", "88", "99");

        while (true) {
            long blockTime = contract.currentBlock().getBlock().getTimestamp().longValue();
            long diff = blockTime * 1000 + 3000 - System.currentTimeMillis();
            if (diff > 2000) {
                Function function = new org.web3j.abi.datatypes.Function(
                        "deposit",
                        Arrays.<Type>asList(
                                new Uint256(amount)),
                        Collections.<TypeReference<?>>emptyList());
                final String encode = FunctionEncoder.encode(function);
                RawTransaction tr = RawTransaction.createTransaction(nonce, price, limit, to, value, encode);
                String hex = wallet.signContractTransaction(tr);
                String hashLocal = Hash.sha3(hex);
                String sha = sha256(hashLocal, blockTime + 3, 0);
                if (contain(list, sha)) {
                    String hashRemote = contract.sendTransaction(hex);
                    System.out.println(hashRemote);
                    break;
                } else {
                    System.out.println("未命中 - " + hashLocal);
                    amount = amount.subtract(BigInteger.ONE);
                }
            }
        }
    }

    /**
     * 结尾
     *
     * @param list
     * @param hashLocal
     * @return
     */
    private static boolean contain(List<String> list, String hashLocal) {
        return list.stream().anyMatch(hashLocal::endsWith);
    }

    /**
     * hash
     *
     * @param hash
     * @param time
     * @param position
     * @return
     */
    public static String sha256(String hash, long time, int position) {
        String target = hash + time + position;
        byte[] content = Hash.sha256(target.getBytes());
        return Numeric.toHexString(content);
    }

    /**
     * 1
     *
     * @param decimals
     * @return
     */
    public static BigInteger one(int decimals) {
        BigInteger bi = BigInteger.valueOf(10);
        return bi.pow(decimals);
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
