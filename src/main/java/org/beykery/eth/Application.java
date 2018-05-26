package org.beykery.eth;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
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
        String address = "0x8aCc161acB2626505755bBF36184841B8c099806";
        String contractAddress = "0xCada12E8a6D51FDc7B824eE6ade22c853947875b";
        String ret = erc20BalanceOf(address, contractAddress);
        System.out.println(ret);
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
