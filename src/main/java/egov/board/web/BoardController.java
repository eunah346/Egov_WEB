package egov.board.web;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egov.board.service.BoardService;

@Controller
public class BoardController {

	@Resource(name="BoardService")BoardService boardService;
	
	@RequestMapping(value="/boardWrite.do")
	public String boardWrite(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			boardService.checkUser(request);
		} catch (Exception e) {

			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("로그인안했음"))
			{
				return "redirect:/login.do";
			}
			else
			{
				//일반예외페이지
			}
			
			return "error/error";
		}
		return "board/boardwrite";
	}
	
	@RequestMapping(value="/boardInsert.do")
	public String boardInsert(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			boardService.saveBoard(request);
		} catch (Exception e) {

			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("로그인안했음"))
			{
				return "redirect:/login.do";
			}
			else if(error.equals("제목을 다시 확인해주세요.")) 
			{
				return "redirect:/boardWrite.do";
			}
			else if(error.equals("유효성검사실패"))
			{
				return "redirect:/boardWrite.do";
			}
			else
			{
				//일반예외페이지
			}
			
			return "error/error";
		}
		return "board/boardwrite";
	}
	

	@RequestMapping(value="/boardView.do")
	public String boardView(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			resultMap=boardService.showBoard(request);
		} catch (Exception e) {

			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("로그인안했음"))
			{
				return "redirect:/login.do";
			}
			else if(error.equals("유효성검사실패"))
			{
				
			}
			else if(error.equals("페이지찾을수없음"))
			{
				
			}
			else
			{
				//일반예외페이지
			}
			
			return "error/error";
		}
		
		model.addAllAttributes(resultMap); // 자동으로 HashMap에서 ModelMap으로 값을 담음
		
		/*
		 * model.addAttribute("userid", resultMap.get("userid").toString());
		 * model.addAttribute("title", resultMap.get("title").toString());
		 * model.addAttribute("boardcontents",
		 * resultMap.get("boardcontents").toString());
		 */
		return "board/boardview";
	}
	
	@RequestMapping(value="/boardList.do")
	public String boardList(HttpServletRequest request,ModelMap model)
	{
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		try {
			list=boardService.showBoardList(request);
		} catch (Exception e) {

			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("로그인안했음"))
			{
				return "redirect:/login.do";
			}
			else if(error.equals("유효성검사실패"))
			{
				
			}
			else if(error.equals("페이지찾을수없음"))
			{
				
			}
			else
			{
				//일반예외페이지
			}
			
			return "error/error";
		}
		
		model.addAllAttributes(list.get(list.size()-1)); // 페이징 객체
		list.remove(list.size()-1);
		model.addAttribute("boardlist",list);
		
		return "board/boardlist2";
	}
	
	// 답글
	@RequestMapping(value="/boardReply.do")
	public String boardReply(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		String boardid = null;
		try {
			boardid = boardService.checkReply(request);
		} catch (Exception e) {

			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("로그인안했음"))
			{
				return "redirect:/login.do";
			}
			else if(error.equals("유효성검사실패"))
			{
				return "redirect:/boardList.do";
			}
			else
			{
				//일반예외페이지
			}
			
			return "error/error";
		}
		
		model.addAttribute("boardid",boardid);
		return "board/boardreply";
	}
	// 답글 제출
	@RequestMapping(value="/boardReplyReq.do")
	public String boardReplyReq(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			boardService.saveReply(request);
		} catch (Exception e) {

			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("로그인안했음"))
			{
				return "redirect:/login.do";
			}
			else if(error.equals("제목을 다시 확인해주세요.")||error.equals("유효성검사실패")) 
			{
				return "redirect:/boardWrite.do";
			}
			else
			{
				//일반예외페이지
				
			}
			
			return "error/error";
		}
		return "redirect:/boardList.do";
	}
}
	

