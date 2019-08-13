package com.qq;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ClientBeanRepo {

	Map<Integer, ClientBean> clientBeans = new HashMap<>();

	{
		ClientBean clientBean = new ClientBean();
		clientBean.setGrantType("authorization_code");
		clientBean.setId(2);
		clientBean.setRedirectUrl("http://localhost:8081/callback");
		clientBean.setSecrectKey("IamSerect");
		clientBeans.put(2, clientBean);
	}

	public ClientBean  addClientBean(ClientBean clientBean) {
		if (clientBean.getRedirectUrl()!=null  && clientBean.getGrantType()!=null) {
			clientBean.setId(clientBeans.size()+2);
			String secrect = RandomStringUtils.randomAlphabetic(30);
			clientBean.setSecrectKey(secrect);
			clientBeans.put(clientBean.getId(), clientBean);
			return clientBean;

		} else {
			throw new RuntimeException("ClientBean is not good");
		}
	}
	public ClientBean getById(Integer i){
		return clientBeans.get(i);
	}
	public boolean ifValidate(ClientBean clientBean ) {
		ClientBean clientBeanInRepo = clientBeans.get(clientBean.getId());
		if (clientBean.equals(clientBeanInRepo)) {
			return true;
		}
		return false;
	}

}
