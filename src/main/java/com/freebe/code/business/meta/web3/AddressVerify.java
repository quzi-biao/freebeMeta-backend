package com.freebe.code.business.meta.web3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.KVStorage;
import com.freebe.code.util.JwtUtils;

/**
 * 地址验证
 * @author xiezhengbiao
 *
 */
@Component
public class AddressVerify {
	
	private Map<String, String> addressMap = new HashMap<>();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private KVStorage kv;
	
	/**
	 * 获取地址
	 * @param code
	 * @return
	 * @throws CustomException 
	 */
	public String getAddress(String code) throws CustomException {
		String ret = addressMap.get(code);
		if(null == ret) {
			throw new CustomException("token expired");
		}
		
		return ret;
	}
	
	/**
	 * 获取验证码
	 * @param address
	 * @return
	 */
	public String getVerifyCode(String address) {
		String code = addressMap.get(address);
		if(null == code) {
			code = UUID.randomUUID().toString().replaceAll("-", ""); 
			addressMap.put(address, code);
			addressMap.put(code, address);
		}
		return code;
	}
	
	/**
	 * 返回 token
	 * @param param
	 * @return
	 * @throws CustomException 
	 */
	public String auth(VerifyParam param) throws CustomException {
		if(param.getWalletType() != null && "unipass".equals(param.getWalletType().toLowerCase())) {
			try {
				if(!contractValidate(param)) {
					throw new CustomException("auth: sign verify failed");
				}
			} catch (Exception e) {
				throw new CustomException("auth: sign verify error:" + e.getMessage());
			}
		}else {
			if(!validate2(param)) {
				throw new CustomException("auth: sign verify failed");
			}
		}
		
		UserVO user = this.userService.getOrCreateUserByAddress(param.getAddress());
		String token = JwtUtils.getToken(user.getId().toString(), getVerifyCode(param.getAddress()));
        this.kv.save(token, System.currentTimeMillis());
		
		return token;
	}
	
	public static void main(String[] args) throws Exception {
		VerifyParam param = new VerifyParam();
		param.setSign("0x000001054997eebacc519818a78f81eaec95cc24f2b4e9b261a84629e19fe17a1c7ba27b8049fe42afc7de373741368bddbbe278ccc0a618b0f5ed4832dad51eab46f61b020000003c0000006400000000020077a6d26094da43d0ba25a8d9276693cc1ed5f3ae5ade3d73d90f797c8362fa410000003c000000000000003c0100a3a9debb6a7a1886c2d2ce1a859c51b84324b8dd000000280000000000000000");
		param.setAddress("0x381bde2164e7344c340f3cC7Fc7dF0ed75A37986");
		param.setWalletType("UniPass");
		param.setMessage("BountyGO is kindly requesting to Sign in with UniPass securely, with nonce: 85435b3910274cc58a14f2731c4737d9. Sign and login now, begin your journey to BountyGO!");
		
        System.out.println(contractValidate(param));
	}
	
	/**
	 * 合约校验
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public static boolean contractValidate(VerifyParam param) throws Exception {
		final String EIP1271_MAGICVALUE = "0x1626ba7e";
		
		Web3j web3 = Web3j.build(new HttpService("https://rpc.ankr.com/polygon"));
		byte[] msgHash = Sign.getEthereumMessageHash(param.getMessage().getBytes());

        byte[] signatureBytes = Numeric.hexStringToByteArray(param.getSign());
        // 生成需要调用函数的data
		List<TypeReference<?>> outs = Arrays.asList(TypeReference.makeTypeReference("bytes4"));
		List<Type> inputs = new ArrayList<>();
		Type v1 = TypeDecoder.instantiateType("bytes32", msgHash);
		inputs.add(v1);
		Type v2 = TypeDecoder.instantiateType("bytes", signatureBytes);
		inputs.add(v2);
        Function function = new Function("isValidSignature", 
        		inputs, 
        		outs);
        String data = FunctionEncoder.encode(function);
        
        String retValue = "";
        // 组建请求的参数
    	EthCall response = web3.ethCall(
                Transaction.createEthCallTransaction(param.getAddress(), param.getAddress(), data),
                DefaultBlockParameterName.LATEST)
                .send();
    	retValue = response.getValue();
        
        // 解析返回结果
        List<Type> ret = FunctionReturnDecoder.decode(retValue, Arrays.asList(TypeReference.makeTypeReference("bytes4")));
        
        List<String> v =  ret.stream().map(item -> {
        	return Numeric.toHexString((byte[])item.getValue());
        }).collect(Collectors.toList());
        
        if(v == null || v.size() == 0) {
        	return false;
        }
        
        return v.get(0).equals(EIP1271_MAGICVALUE);
	}
	
	
	/**
     * 新的签名校验方式
     * @param signature
     * @param message
     * @param address
     * @return
	 * @throws CustomException 
     */
    public static boolean validate2(VerifyParam param) throws CustomException {
        try {
        	byte[] msgHash = Sign.getEthereumMessageHash(param.getMessage().getBytes());

            byte[] signatureBytes = Numeric.hexStringToByteArray(param.getSign());
            byte v = signatureBytes[64];
            if (v < 27) {
                v += 27;
            }

            SignatureData sd = new SignatureData(
                    v,
                    Arrays.copyOfRange(signatureBytes, 0, 32),
                    Arrays.copyOfRange(signatureBytes, 32, 64));

            String addressRecovered = null;
            boolean match = false;

            // Iterate for each possible key to recover
            for (int i = 0; i < 4; i++) {
                BigInteger publicKey = Sign.recoverFromSignature(
                        (byte) i,
                        new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                        msgHash);

                if (publicKey != null) {
                    addressRecovered = "0x" + Keys.getAddress(publicKey);
                    if (addressRecovered.toLowerCase().equals(param.getAddress().toLowerCase())) {
                        match = true;
                        break;
                    }
                }
            }
            return match;
		} catch (Exception e) {
			throw new CustomException("auth failed: " + e.getMessage());
		}
    }
	
	/**
     * 对签名消息，原始消息，账号地址三项信息进行认证，判断签名是否有效
     * @param signature
     * @param message
     * @param address
     * @return
	 * @throws CustomException 
     */
    public static boolean validate(VerifyParam param) throws CustomException {
        //参考 eth_sign in https://github.com/ethereum/wiki/wiki/JSON-RPC
        // eth_sign
        // The sign method calculates an Ethereum specific signature with:
        //    sign(keccak256("\x19Ethereum Signed Message:\n" + len(message) + message))).
        //
        // By adding a prefix to the message makes the calculated signature recognisable as an Ethereum specific signature.
        // This prevents misuse where a malicious DApp can sign arbitrary data (e.g. transaction) and use the signature to
        // impersonate the victim.
    	
        try {
			byte[] msgHash = Sign.getEthereumMessageHash(param.getMessage().getBytes());
 
			SignatureData sd = new SignatureData(
					Numeric.hexStringToByteArray(param.getV()), 
					Numeric.hexStringToByteArray(param.getR()), 
					Numeric.hexStringToByteArray(param.getS()));
 
			String addressRecovered = null;
			boolean match = false;
 
			// Iterate for each possible key to recover
			for (int i = 0; i < 4; i++) {
			    BigInteger publicKey = Sign.recoverFromSignature(
			            (byte) i,
			            new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
			            msgHash);
 
			    if (publicKey != null) {
			        addressRecovered = "0x" + Keys.getAddress(publicKey);
			        if (addressRecovered.toLowerCase().equals(param.getAddress().toLowerCase())) {
			            match = true;
			            break;
			        }
			    }
			}
			return match;
		} catch (Exception e) {
			throw new CustomException("auth failed: " + e.getMessage());
		}
    }


}
