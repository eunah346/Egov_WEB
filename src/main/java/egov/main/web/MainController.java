package egov.main.web;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
			request.getSession().setAttribute("myid",resultMap.get("userid").toString());
			model.addAttribute("serverId", resultMap.get("userid").toString());
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
	
}
