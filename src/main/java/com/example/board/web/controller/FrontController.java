package com.example.board.web.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
//@WebServlet({"/board/free/*", "/boards/free/*"})
public class FrontController extends HttpServlet {

    /**
     * jsp 매핑 정보 (action, jsp file path)
     */
    private Map<String, String> actionMap = new HashMap<>();

    /**
     * actionMap 초기화
     */
    public FrontController() {
        actionMap.put("list", "/WEB-INF/view/list.jsp");
        actionMap.put("write", "/WEB-INF/view/write.jsp");
        actionMap.put("view", "/WEB-INF/view/view.jsp");
        actionMap.put("modify", "/WEB-INF/view/modify.jsp");
        actionMap.put("write.do", "/WEB-INF/service/write.do.jsp");
        actionMap.put("comment.do", "/WEB-INF/service/comment.do.jsp");
        actionMap.put("modify.do", "/WEB-INF/service/modify.do.jsp");
        actionMap.put("delete.do", "/WEB-INF/service/delete.do.jsp");
        actionMap.put("download.do", "/WEB-INF/service/download.do.jsp");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String action = pathParts[1];

        if(!actionMap.containsKey(action)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else if (action.equals("view")) {
            //["", "view", "{seq}"]
            String id = pathParts[2];
            request.setAttribute("id", id);
        }

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName ->
                        request.setAttribute(paramName, request.getParameter(paramName)));

        String viewPath = actionMap.get(action);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
        requestDispatcher.forward(request, response);
    }

}
