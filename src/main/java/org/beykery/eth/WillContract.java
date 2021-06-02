package org.beykery.eth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.request.Transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 * 合约的交互
 */
public class WillContract {
    /**
     * logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(WillContract.class);
    /**
     * web3j
     */
    private final Web3j web3j;
    /**
     * 当前缓存的油价
     */
    private static volatile BigInteger cachedGasPrice;

    /**
     * 连接到一个rpc节点
     *
     * @param url
     * @throws java.util.concurrent.ExecutionException
     * @throws java.lang.InterruptedException
     */
    public WillContract(String url) throws ExecutionException, InterruptedException {
        web3j = Web3j.build(new HttpService(url));
        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        LOG.info(clientVersion);
    }

    /**
     * nonce
     *
     * @param address 地址
     * @return
     */
    public BigInteger nonce(String address) {
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            return nonce;
        } catch (InterruptedException | ExecutionException ex) {
            LOG.error("获取nonce失败：" + ex);
        }
        return null;
    }

    /**
     * 刷新汽油价格
     *
     * @return
     * @throws java.io.IOException
     */
    public BigInteger refreshGasPrice() throws IOException {
        EthGasPrice price = web3j.ethGasPrice().send();
        BigInteger cur = price.getGasPrice();
        cachedGasPrice = cur;
        return cur;
    }

    /**
     * 缓存的价格
     *
     * @return
     */
    public static BigInteger cachedGasPrice() {
        return cachedGasPrice;
    }

    /**
     * 账户余额(以太币)
     *
     * @param address 地址
     * @return
     * @throws java.io.IOException
     */
    public BigInteger banlance(String address) throws IOException {
        EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        return balance.getBalance();
    }

    /**
     * 当前块
     *
     * @return
     * @throws IOException
     */
    public EthBlock currentBlock() throws IOException {
        EthBlock block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
        return block;
    }

    /**
     * balanceOf函数
     *
     * @param address
     * @return
     */
    public static Function balanceOf(String address) {
        return new Function(
                "balanceOf",
                Collections.singletonList(new Address(address)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
    }

    /**
     * transfer函数
     *
     * @return
     */
    public static Function transfer(String to, BigInteger value) {
        List inputs = new ArrayList();
        inputs.add(new Address(to));
        inputs.add(new Uint256(value));
        List outputs = new ArrayList();
        outputs.add(new TypeReference<Bool>() {
        });
        return new Function("transfer", inputs, outputs);
    }

    /**
     * 合约函数call
     *
     * @param from            from的地址
     * @param function        合约方法
     * @param contractAddress 合约地址
     * @return 函数结果
     * @throws Exception
     */
    public List<Type> callSmartContractFunction(String from, Function function, String contractAddress) throws Exception {
        String encodedFunction = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(from, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST)
                .sendAsync().get();
        String res = response.getValue();
        List<Type> ts = FunctionReturnDecoder.decode(res, function.getOutputParameters());
        return ts;
    }


    /**
     * 查看交易状态
     *
     * @param hash
     * @return
     * @throws java.io.IOException
     */
    public EthGetTransactionReceipt transactionReceipt(String hash) throws IOException {
        EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(hash).send();
        return receipt;
    }

    /**
     * 查询合约状态
     * org.web3j.abi.datatypes 中的类型
     *
     * @param from            from
     * @param contractAddress 合约地址
     * @param funname         方法名
     * @param inputs          输入
     * @param outputs         输出
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Type> call(String from, String contractAddress, String funname, List<Type> inputs, List<TypeReference<?>> outputs) throws ExecutionException, InterruptedException {
        Function function = new Function(funname, inputs, outputs);
        String encodedFunction = FunctionEncoder.encode(function);
        Transaction tx = Transaction.createEthCallTransaction(from, contractAddress, encodedFunction);
        EthCall response = web3j.ethCall(tx, DefaultBlockParameterName.LATEST).sendAsync().get();
        List<Type> result = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        return result;
    }

    /**
     * 执行合约方法（修改状态）
     *
     * @param credentials     身份
     * @param function        函数
     * @param contractAddress 合约地址
     * @param gasPrice
     * @param gasLimit
     * @return 交易的hash值
     * @throws Exception
     */
    public String execute(Credentials credentials, Function function, String contractAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger value) throws Exception {
        BigInteger nonce = nonce(credentials.getAddress());
        return execute(credentials, nonce, function, contractAddress, gasPrice, gasLimit, value);
    }

    /**
     * 执行合约方法（修改状态）
     *
     * @param credentials     身份
     * @param function        函数
     * @param contractAddress 合约地址
     * @param gasPrice
     * @param gasLimit
     * @param nonce           nonce
     * @return 交易的hash值
     * @throws Exception
     */
    public String execute(Credentials credentials, BigInteger nonce, Function function, String contractAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger value) throws Exception {
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                contractAddress,
                value,
                encodedFunction);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction transactionResponse = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        return transactionResponse.getTransactionHash();
    }

    /**
     * @param credentials     身份
     * @param function        函数
     * @param contractAddress 合约地址
     * @param gasPrice
     * @param gasLimit
     * @return 签名结果
     */
    public String sign(Credentials credentials, Function function, String contractAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger value) {
        BigInteger nonce = this.nonce(credentials.getAddress());
        return sign(credentials, nonce, function, contractAddress, gasPrice, gasLimit, value);
    }

    /**
     * @param credentials     身份
     * @param function        函数
     * @param contractAddress 合约地址
     * @param gasPrice
     * @param gasLimit
     * @param nonce           nonce
     * @return 签名结果
     */
    public String sign(Credentials credentials, BigInteger nonce, Function function, String contractAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger value) {
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                contractAddress,
                value,
                encodedFunction);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    /**
     * 发送签名过的交易
     *
     * @param hexValue 签名过的交易
     * @return 交易hash
     * @throws java.util.concurrent.ExecutionException
     * @throws java.lang.InterruptedException
     */
    public String sendTransaction(String hexValue) throws ExecutionException, InterruptedException {
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }

    /**
     * 发送ether
     *
     * @param to       目的地
     * @param ether    数量
     * @param nonce    nonce
     * @param gasPrice 油价
     * @param gasLimit 限量
     * @return rawtransaction
     */
    public static RawTransaction etherTransaction(String to, double ether, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit) {
        BigInteger value = Convert.toWei(String.valueOf(ether), Convert.Unit.ETHER).toBigInteger();
        RawTransaction rt = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value);
        return rt;
    }

    /**
     * 智能合约对象的构建
     *
     * @param nonce    nonce
     * @param bin      合约二进制代码
     * @param gasPrice 油价
     * @param gasLimit 限量
     * @param ps       合约对象构造方法参数列表
     * @return
     */
    public static RawTransaction smartContractTransaction(BigInteger nonce, String bin, BigInteger gasPrice, BigInteger gasLimit, List<Type> ps) {
        String src = bin;
        if (ps != null) {
            String encodedConstructor = FunctionEncoder.encodeConstructor(ps);
            src = src + encodedConstructor;
        }
        RawTransaction rt = RawTransaction.createContractTransaction(nonce, gasPrice, gasLimit, BigInteger.ZERO, src);
        return rt;
    }

    /**
     * 部署智能合约
     *
     * @param credentials 身份信息
     * @param bin         合约二进制
     * @param gasPrice    油价
     * @param gasLimit    限量
     * @param ps          合约对象构造方法参数列表
     * @return 交易的hash值
     * @throws java.lang.Exception
     */
    public String deploySmartContract(Credentials credentials, String bin, BigInteger gasPrice, BigInteger gasLimit, List<Type> ps) throws Exception {
        BigInteger nonce = nonce(credentials.getAddress());
        RawTransaction rt = smartContractTransaction(nonce, bin, gasPrice, gasLimit, ps);
        byte[] signedMessage = TransactionEncoder.signMessage(rt, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction transactionResponse = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        return transactionResponse.getTransactionHash();
    }
}
