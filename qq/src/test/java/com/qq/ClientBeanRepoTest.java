package com.qq;

import com.qq.bean.ClientBean;
import com.qq.repo.ClientBeanRepo;
import org.junit.Test;

public class ClientBeanRepoTest  {


	@Test
	public void testEqual() {
		ClientBeanRepo clientBeanRepo = new ClientBeanRepo();
		ClientBean clientBean = new ClientBean();
		clientBean.setId(2);
		clientBean.setGrantType("code");
		clientBean.setRedirectUrl("http://www.baidu.com");
		clientBean.setSecrectKey("IamSerect");
		boolean b = clientBeanRepo.ifValidate(clientBean);
		assert b;
	}
}
