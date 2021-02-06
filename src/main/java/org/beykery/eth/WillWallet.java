package org.beykery.eth;

import java.io.File;
import java.math.BigInteger;
import java.security.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.beykery.util.bip44.Address;
import org.beykery.util.bip44.HdKeyNode;
import org.beykery.util.bip44.NetworkParameters;
import org.beykery.util.bip44.hdpath.HdKeyPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


/**
 * 钱包
 */
public class WillWallet
{
    /**
     * logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(WillWallet.class);
    /**
     * 随机
     */
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();
    /**
     * mapper
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static
    {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 身份凭证
     */
    private Credentials credentials;
    /**
     * 钱包文件密码
     */
    private String walletPass;
    /**
     * 钱包文件
     */
    private WalletFile walletFile;
    /**
     * 助记词
     */
    private String mnemonic;
    /**
     * 路径
     */
    private String path;

    /**
     * 不许调用
     */
    private WillWallet()
    {

    }

    /**
     * 构造(根据私钥构建)
     *
     * @param privateKey 私钥
     */
    public WillWallet(String privateKey)
    {
        credentials = Credentials.create(privateKey);
    }

    /**
     * @param passPhrase    密码
     * @param walletContent 钱包文件内容
     * @throws org.web3j.crypto.CipherException
     */
    public WillWallet(String passPhrase, byte[] walletContent) throws CipherException
    {
        walletFile = createWalletFile(walletContent);
        credentials = Credentials.create(Wallet.decrypt(passPhrase, walletFile));
        walletPass = passPhrase;
    }

    /**
     * @param password      钱包密码
     * @param walletContent 钱包文件内容
     * @throws org.web3j.crypto.CipherException
     */
    public WillWallet(String password, String walletContent) throws CipherException
    {
        walletFile = createWalletFile(walletContent);
        credentials = Credentials.create(Wallet.decrypt(password, walletFile));
        walletPass = password;
    }

    /**
     * 钱包构造
     *
     * @param passPhrase 密码
     * @param file       钱包文件
     * @throws org.web3j.crypto.CipherException
     */
    public WillWallet(String passPhrase, File file) throws CipherException
    {
        walletFile = createWalletFile(file);
        credentials = Credentials.create(Wallet.decrypt(passPhrase, walletFile));
        walletPass = passPhrase;
    }

    /**
     * 创建一个新的钱包(没有助记词的官方钱包)
     *
     * @param password wallet file的密码
     * @return
     * @throws Exception
     */
    public static WillWallet create(String password) throws Exception
    {
        WillWallet wa = new WillWallet();
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        wa.walletFile = Wallet.createStandard(password, ecKeyPair);
        wa.credentials = Credentials.create(Wallet.decrypt(password, wa.walletFile));
        wa.walletPass = password;
        return wa;
    }

    /**
     * 创建新钱包（生成助记词）
     *
     * @param password 即将生成的wallet file的密码；如果密码为空则不再生成wallet file
     *                 经查imtoken助记词密码为null，这里保持一致
     * @param path     路径（可以不按照bip44要求）
     * @return
     * @throws org.web3j.crypto.CipherException
     */
    public static WillWallet createWithMnemonic(String password, String path) throws CipherException
    {
        WillWallet wa = new WillWallet();
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        ECKeyPair ecKeyPair = createBip44NodeFromSeed(seed, path);
        if (password != null)
        {
            wa.walletFile = Wallet.createStandard(password, ecKeyPair);
        }
        wa.credentials = Credentials.create(ecKeyPair);
        wa.walletPass = password;
        wa.mnemonic = mnemonic;
        wa.path = path;
        return wa;
    }

    /**
     * 载入一个钱包
     *
     * @param password 即将生成的wallet file的密码；如果密码为空则不再生成wallet file
     *                 imtoken助记词密码为null
     * @param mnemonic 助记词
     * @param path     路径 m/44'/60'/0'/0/0  m/purpse'/coin_type'/account'/change/address_index
     * @return 钱包
     * @throws CipherException
     */
    public static WillWallet fromMnemonic(String password, String mnemonic, String path) throws CipherException
    {
        WillWallet wa = new WillWallet();
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        ECKeyPair ecKeyPair = createBip44NodeFromSeed(seed, path);
        if (password != null)
        {
            wa.walletFile = Wallet.createStandard(password, ecKeyPair);
        }
        wa.credentials = Credentials.create(ecKeyPair);
        wa.walletPass = password;
        wa.mnemonic = mnemonic;
        wa.path = path;
        return wa;
    }

    /**
     * m/purpse'/coin_type'/account'/change/address_index
     * m/44'/60'/0'/0/0
     *
     * @param seed 种子
     * @param path 路径 m/44'/60'/0'/0/0
     * @return key 秘钥
     */
    private static ECKeyPair createBip44NodeFromSeed(byte[] seed, String path)
    {
        HdKeyPath p = HdKeyPath.valueOf(path);
        HdKeyNode node = HdKeyNode.fromSeed(seed);
        node = node.createChildNode(p);
        Address add=node.getPublicKey().toAddress(NetworkParameters.productionNetwork);
        byte[] privateKeyByte = node.getPrivateKey().getPrivateKeyBytes();
        ECKeyPair ecKeyPair = ECKeyPair.create(privateKeyByte);
        return ecKeyPair;
    }

    /**
     * 导出一个新的wallet file，并记录下当前的wallet file信息
     *
     * @param password 设定一个钱包文件密码
     * @return 钱包文件
     * @throws org.web3j.crypto.CipherException
     */
    public WalletFile exportWalletFile(String password) throws CipherException
    {
        ECKeyPair ekp = credentials.getEcKeyPair();
        WalletFile walletFile = Wallet.createStandard(password, ekp);
        this.walletFile = walletFile;
        this.walletPass = password;
        return walletFile;
    }

    /**
     * wallet file的json内容
     *
     * @param wf
     * @return json格式的内容
     */
    public static String walletFileJson(WalletFile wf)
    {
        try
        {
            return objectMapper.writeValueAsString(wf);
        } catch (JsonProcessingException e)
        {
            return null;
        }
    }

    /**
     * 离线签名一笔交易
     *
     * @param toAddress 目标地址
     * @param gasPrice  油价
     * @param gasLimit  汽油上限
     * @param amount    转账额度
     * @param nonce     nonce
     * @return
     */
    public String signOfflineTransaction(String toAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger amount, BigInteger nonce)
    {
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, amount);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    /**
     * 签名合约交互
     *
     * @param toAddress
     * @param gasPrice
     * @param gasLimit
     * @param nonce
     * @param data
     * @return
     */
    public String signContractTransaction(String toAddress, BigInteger gasPrice, BigInteger gasLimit, BigInteger nonce, String data)
    {
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddress, new BigInteger("0"), data);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    /**
     * 签名合约
     *
     * @param tr transaction
     * @return 签名结果
     */
    public String signContractTransaction(RawTransaction tr)
    {
        return signContractTransaction(tr, credentials);
    }

    /**
     * 签名
     *
     * @param tr transaction
     * @param c  身份
     * @return 签名结果
     */
    public static String signContractTransaction(RawTransaction tr, Credentials c)
    {
        byte[] signedMessage = TransactionEncoder.signMessage(tr, c);
        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    /**
     * 是否合法
     *
     * @return
     */
    public boolean valid()
    {
        ECKeyPair keyPair;
        try
        {
            keyPair = Wallet.decrypt(walletPass, walletFile);
        } catch (CipherException e)
        {
            return false;
        }
        return true;
    }

    /**
     * 反序列化一个钱包
     *
     * @param content
     * @return 钱包文件
     */
    public static WalletFile createWalletFile(byte[] content)
    {
        try
        {
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            WalletFile walletFile = objectMapper.readValue(content, WalletFile.class);
            return walletFile;
        } catch (IOException ex)
        {

        }
        return null;
    }

    /**
     * 反序列化钱包
     *
     * @param content
     * @return 钱包文件
     */
    public static WalletFile createWalletFile(String content)
    {
        try
        {
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            WalletFile walletFile = objectMapper.readValue(content, WalletFile.class);
            return walletFile;
        } catch (IOException ex)
        {

        }
        return null;
    }

    /**
     * 反序列化一个钱包
     *
     * @param file
     * @return 钱包文件
     */
    public static WalletFile createWalletFile(File file)
    {
        try
        {
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            WalletFile walletFile = objectMapper.readValue(file, WalletFile.class);
            return walletFile;
        } catch (IOException ex)
        {

        }
        return null;
    }

    /**
     * 地址转换
     *
     * @param number 公钥
     * @return 地址
     */
    public static String address(BigInteger number)
    {
        return Keys.toChecksumAddress(Keys.getAddress(number));
    }

    /**
     * 私钥转换
     *
     * @param prik 私钥
     * @return 私钥的hex格式
     */
    public static String privateKey(BigInteger prik)
    {
        return Numeric.toHexStringWithPrefixZeroPadded(prik, Keys.PRIVATE_KEY_LENGTH_IN_HEX);
    }

    /**
     * 地址
     *
     * @return 地址
     */
    public String getAddress()
    {
        return address(credentials.getEcKeyPair().getPublicKey());
    }

    /**
     * 私钥
     *
     * @return 私钥
     */
    public String getPrivateKey()
    {
        return privateKey(credentials.getEcKeyPair().getPrivateKey());
    }

    /**
     * 助记词
     *
     * @return 助记词
     */
    public String getMnemonic()
    {
        return mnemonic;
    }

    /**
     * 助记词密码
     *
     * @return 助记词密码(永远为null，跟imtoken保持一致)
     */
    public String getPassphrase()
    {
        return null;
    }

    /**
     * 当前的钱包文件密码
     *
     * @return 钱包文件的密码
     */
    public String getWalletPass()
    {
        return walletPass;
    }

    /**
     * 身份信息
     *
     * @return 身份
     */
    public Credentials getCredentials()
    {
        return credentials;
    }

    /**
     * 钱包文件
     *
     * @return 钱包文件
     */
    public WalletFile getWalletFile()
    {
        return walletFile;
    }

    /**
     * 路径
     *
     * @return
     */
    public String getPath()
    {
        return path;
    }

}
