package egov.board.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lib.model.UserVO;
import com.lib.util.Validation_Form;

import egov.board.dao.BoardMapper;
import egov.board.service.BoardService;

@Service("BoardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService{

	@Resource(name="BoardMapper")
	BoardMapper boardMapper;
	
	@Resource(name="fileUploadProperty") // 파일저장
	Properties properties;

	@Override
	public void checkUser(HttpServletRequest request) throws Exception {
		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}		
	}
	
	@Override
	public void saveBoard(HttpServletRequest request) throws Exception {
		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		//사용자요청을 데이터베이스로 전달
		String title= request.getParameter("title");
		String content =request.getParameter("mytextarea");
		if(title.length()>25)
		{
			throw new Exception("제목을 다시 확인해주세요.");
		}
		
		// 게시판번호글을 먼저 데이터베이스에 저장
		HashMap<String,Object> paramMap= new HashMap<String,Object>();
		paramMap.put("in_title", title);
		paramMap.put("in_content", content);
		paramMap.put("in_userid", ((UserVO)request.getSession().getAttribute("uservo")).getUserid());
		paramMap.put("out_state", 0);
		boardMapper.saveBoard(paramMap);
		
		
		String uploadPath = properties.getProperty("file.ImgPath");
		String convertuid = "";	// 서버에 저장할 이름
		String originalEx="";	// 사용자가 업로드 요청한 파일의 확장자 저장
		String filePath="";		// 파일 저장 위치
		
		// 인코딩 타입을 multipart로 추가해둬서 파일을 제출시 if 문으로 이동하게 됨
		if(request instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
			final Map<String,MultipartFile> files = multiRequest.getFileMap();
			
			File saveFolder = new File(uploadPath); // 경로를 가지는 객체
			
			// 실제로 폴더 생성
			if(!saveFolder.exists() || saveFolder.isFile())
			{
				saveFolder.mkdirs();
			}
			// 하나씩 가져오기
			for(MultipartFile file:files.values()) 
			{
				// 공백체크
				if(!"".equals(file.getOriginalFilename()))
				{
					// 파일 사이즈 제한
					int maxSize = 1*1024*1024; // 1mb
					int fileSize = (int)file.getSize();
					if(fileSize>maxSize) 
					{
						throw new Exception("유효성검사 실패");
					}
					
					// 서버에 저장하기 위해서 새로운 파일이름 생성
					Calendar cal = Calendar.getInstance();
					int year = cal.get ( Calendar.YEAR );
					int month = cal.get ( Calendar.MONTH ) + 1 ;
					int date = cal.get ( Calendar.DATE ) ;
					int hour = cal.get ( Calendar.HOUR_OF_DAY ) ;

					convertuid = UUID.randomUUID().toString().replace("-", "")+year+month+date+hour;
					// 문자열을 substring 을 통해서 잘라줘서 확장자를 얻음 .의 위치를 숫자로 얻어서 함수에 사용
					originalEx= file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
					convertuid = convertuid + "." + originalEx;
					// 서버의 파일경로
					filePath = uploadPath + convertuid;	// 업로드 폴더와 생성한 파일이름 합쳐줌
					// 파일경로로 파일 전송
					file.transferTo(new File(filePath));
					
					// db에 파일 업로드 (파일테이블로)
					HashMap<String,Object> paramMap2= new HashMap<String,Object>();
					paramMap2.put("in_userid", ((UserVO)request.getSession().getAttribute("uservo")).getUserid());
					paramMap2.put("in_filename", convertuid);
					paramMap2.put("in_filetype", originalEx);
					paramMap2.put("in_fileurl", "http://localhost:9090/Egov_WEB/boardView/image.do?file="+convertuid); // 포트번호, 도메인 이름으로 교체
					paramMap2.put("out_state", 0);
					boardMapper.saveFile(paramMap2);
					
				}
			}
		}
		
	}

	@Override
	public HashMap<String, Object> showBoard(HttpServletRequest request) throws Exception {

		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		// 로그인 세션 가져오기 (세션에 저장되어있는 유저 정보 가져옴)
		String id = ((UserVO)request.getSession().getAttribute("uservo")).getUserid();
		// 사용자요청을 데이터베이스로 전달
		String brdid = request.getParameter("brdid"); // 게시판 번호
		
		// 게시판 번호 숫자인지 체크 
		boolean validNumber = false;
		validNumber = Validation_Form.validNum(brdid); // com.lib.util ~ Validation_Form 유효성 검증

		if(validNumber==false)
		{
			throw new Exception("유효성검사 실패");
		}
		HashMap<String,Object> paramMap= new HashMap<String,Object>();
		paramMap.put("in_brdid", brdid); // 게시글 요청
		paramMap.put("out_state", 0);
		
		HashMap<String,Object> rusultMap= new HashMap<String,Object>();
		rusultMap=boardMapper.showBoard(paramMap);
		
		if(rusultMap==null)
		{
			throw new Exception("페이지찾을수없음");
		}
		rusultMap.put("loginid",id);
		
		return rusultMap;
	}

	@Override
	public ArrayList<HashMap<String, Object>> showBoardList(HttpServletRequest request) throws Exception {
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		// 페이지 번호
		String pageNo = request.getParameter("pageNo");
		// 요청값 검증
		if(pageNo == null || pageNo.equals(""))
		{
			pageNo = "1";
		}
		else
		{
			pageNo = request.getParameter("pageNo");
		}
		// 페이징 관련 정보를 담는 class
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(Integer.parseInt(pageNo));
		paginationInfo.setPageSize(10);
		paginationInfo.setRecordCountPerPage(10);
		
		HashMap<String,Object> paramMap= new HashMap<String,Object>();
		paramMap.put("pi_offset", (paginationInfo.getCurrentPageNo()-1)*paginationInfo.getRecordCountPerPage());

		paramMap.put("pi_recordCountPerPage", paginationInfo.getRecordCountPerPage()); // 게시물 건수
		paramMap.put("out_listcount", 0);
		paramMap.put("out_state", 0);
		
		ArrayList<HashMap<String,Object>> list= new ArrayList<HashMap<String,Object>>();
		list=boardMapper.showBoardList(paramMap);
		int listCount = Integer.parseInt(paramMap.get("out_listcount").toString());
		paginationInfo.setTotalRecordCount(listCount);
		
		if(list==null)
		{
			throw new Exception("페이지찾을수없음");
		}
		
		HashMap<String,Object> resultMap= new HashMap<String,Object>();
		resultMap.put("paginationInfo", paginationInfo);
		resultMap.put("listCount", listCount);
		list.add(resultMap);
		
		return list;
	}
	
	// 답글
	@Override
	public String checkReply(HttpServletRequest request) throws Exception {
		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		String boardid =  request.getParameter("boardid");
		if(Validation_Form.validNum(boardid)==false)
				// 숫자인지 체크해주는 유효성 검사 함수, 숫자가 아니면 false를 반환
				// com.lib.util > Validation_Form 에서 생성해뒀음
		{
			throw new Exception("유효성검사실패");
		}
		
		return boardid;
	}
	
	@Override
	public void saveReply(HttpServletRequest request) throws Exception 
	{
		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		
		//사용자요청을 데이터베이스로 전달
		String title = request.getParameter("title");
		String content = request.getParameter("mytextarea");
		String originalid =  request.getParameter("originalid");
		
		System.out.println("title : "+ title);
		System.out.println("content : "+ content);
		System.out.println("originalid :"+originalid);
		
		if(Validation_Form.validNum(originalid)==false||content.length()>10000)
		{
			throw new Exception("유효성검사실패");
		}
		if(title.length()>25)
		{
			throw new Exception("제목을 다시 확인해주세요.");
		}
		
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("in_originalid", Integer.parseInt(originalid));
		paramMap.put("in_title", title);
		paramMap.put("in_content", content);
		paramMap.put("in_userid", ((UserVO)request.getSession().getAttribute("uservo")).getUserid());
		paramMap.put("out_state", 0);
		boardMapper.saveReply(paramMap);
		
		if(Integer.parseInt(paramMap.get("out_state").toString())!=1)
		{
			throw new Exception("DB작업실패~!");
		}
	}
}
