package egov.main.dao;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("MainMapper")
public interface MainMapper {

	HashMap<String, Object> selectMain(HashMap<String, Object> paramMap)throws Exception;

	HashMap<String, Object> selectLogin(HashMap<String, Object> paramMap)throws Exception;

}
