package egov.main.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egov.main.dao.MainMapper;
import egov.main.service.MainService;

@Service("MainService")
public class MainServiceImpl extends EgovAbstractServiceImpl implements MainService{

	@Resource(name="MainMapper")
	MainMapper mainMapper;
	
	@Override
	public HashMap<String, Object> selectMain(HttpServletRequest request) throws Exception {
		HashMap<String,Object> paramMap = new HashMap();
		//request욫엉을 paramMap에 담아주기
		
		return mainMapper.selectMain(paramMap);
	}

	@Override
	public HashMap<String, Object> selectLogin(HttpServletRequest request) throws Exception {
		
		String userid= request.getParameter("id");
		
		if(userid.length()>10)
		{
			throw new Exception("validError_userId");
		}
		HashMap<String,Object> paramMap = new HashMap();
		//request요청을 paramMap에 담아주기
		paramMap.put("myid", userid);
		paramMap.put("out_state", -1);
		HashMap<String,Object> resultMap = mainMapper.selectLogin(paramMap);
		//if
		if(resultMap ==null)
		{
			throw new Exception("resultError_idnotFound");
		}
		
		return resultMap;
	}

}
