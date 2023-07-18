package egov.main.web;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lib.model.UserVO;

import egov.main.service.MainService;

@Controller
public class MainController {

	@Resource(name="MainService")
	MainService mainService;

	
	@RequestMapping(value="/main.do")
	public String main(HttpServletRequest request,ModelMap model)
	{
		
		return "main/main";
	}
	
	@RequestMapping(value="/main2.do")
	public String mai2(HttpServletRequest request,ModelMap model)
	{
		
		return "main/main2";
	}
	
	

	@RequestMapping(value="/main3.do")
	public String main3(@RequestParam("pw")String pw,HttpServletRequest request,ModelMap model)
	{
		String id = request.getParameter("id");
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		
		if(id.equals("asdqwe"))
		{
			model.addAttribute("userid", pw);
		}
		else
		{
			model.addAttribute("userid", pw);	
		}
		
		userNo = userNo+5;
		model.addAttribute("userNo", userNo);	
		
		return "main/main3";
	}
	
	@RequestMapping(value="/main4/{userNo}.do")
	public String main4(@PathVariable String userNo,HttpServletRequest request,ModelMap model)
	{
		model.addAttribute("userNo", userNo);
		return "main/main3";
	}
	
	@RequestMapping(value="/main5.do")
	public String main5(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			resultMap= mainService.selectMain(request);
			model.addAttribute("serverId", resultMap.get("userid").toString());
		} catch (Exception e) {
			//로그기록,상태코드반환 또는 에러페이지 전달
			return "error/error";
		}
		return "main/main";
	}
	
	@RequestMapping(value="/login.do")
	public String login(HttpServletRequest request,ModelMap model)
	{
		
		return "login/login";
	}
	
	@RequestMapping(value="/loginSubmission.do")
	public String loginSubmission(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			resultMap= mainService.selectLogin(request);
			request.getSession().setAttribute("uservo",resultMap.get("uservo"));
			model.addAttribute("serverId", ((UserVO)resultMap.get("uservo")).getUserid());
		} catch (Exception e) {
			
			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("resultError_idnotFound"))
			{
				return "redirect:/login.do";
			}
			else
			{
				//일반예외페이지
			}
			
			return "error/error";
		}
		return "main/main";
	}
	
	@RequestMapping(value="/logout.do")
	public String logout(HttpServletRequest request,ModelMap model)
	{
		System.out.println(request.getSession().getAttribute("myid").toString());
		request.getSession().invalidate();
		System.out.println(",");
		
		return "login/login";
	}
	
	//HttpServletRequest : 파라미터에서 요청정보 또 는 사용자정보를 파악할 수 있음
	//HttpServletResponse : 사용자에게 응답을 하기 위한 데이터를 담아서 결과를 돌려줌
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/testjson.do")
	public void testjson(HttpServletRequest request,HttpServletResponse response)
	{
		// 들어오는 데이터 처리
		try {
			request.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Name", "abc123");
			jsonObject.put("Text", "물고기");
			
	
			JSONObject jsonObject2 = new JSONObject();
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("column1", "100");
			resultMap.put("column2", 101);
			resultMap.put("column3", 102);
			
			// putAll : map 타입을 자동으로 json 형태로 변환 (결과값을 담은 HashMap을 json으로 변경)
			jsonObject2.putAll(resultMap);
			
			JSONObject jsonObject3 = new JSONObject();
			jsonObject.put("Name", "abc123");
			jsonObject.put("TEXT","물고기"); 
			
			// JSONArray(json List형식)  jsonObject2 와 jsonObject3을 묶어줌
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(jsonObject2);
			jsonArray.add(jsonObject3); // 대괄호로 리스트 형식으로 저장
			
			jsonObject.put("mylist", jsonArray);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");

			response.getWriter().print(jsonObject);
			} catch (IOException e) {
			// TODO Auto-generated catch block
			//response.setStatus(0, null) http 상태코드와 메시지를 전달할 수 있는 함수
				e.printStackTrace();
		}
	}	
		
		
	// jsp 페이지 
	@RequestMapping(value="/showtestpage.do")
	public String showtestpage(HttpServletRequest request,ModelMap model)
	{
		return "common/test";
	}
	
	// xml	
	@RequestMapping(value="/testxml.do")
	public void testxml(HttpServletRequest request,HttpServletResponse response)
	{
		try {
			Document doc = new Document(); // org.jdom2
			Element root = new Element("MYAnimal"); // org.jdom2
			root.setAttribute("myName2","abc2");
			
			Element item1 = new Element("animal1");
			item1.setText("코끼리");
			Element item2 = new Element("animal2");
			item2.setText("토끼");
			Element item3 = new Element("animal3");
			item3.setText("고양이");
			Element item4 = new Element("animal4");
			Element item4_sub = new Element("animal5");
			item4_sub.setText("물개");
			
			root.addContent(item1);
			root.addContent(item2);
			root.addContent(item3);
			item4.addContent(item4_sub);
			root.addContent(item4);
			
			doc.addContent(root); // 하위 태그들도 자동으로 추가
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");

			response.getWriter().print(new XMLOutputter().outputString(doc));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
