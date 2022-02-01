package com.test.mvc4test2.portlet.controller;

import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;
import com.liferay.portletmvc4spring.context.PortletApplicationContextUtils;
import com.test.share.SharedAppBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

@Controller
@RequestMapping("VIEW")
public class ViewController {

    @RenderMapping
    public String prepareView(RenderRequest renderRequest) {

        PortletSession portletSession = renderRequest.getPortletSession();
        PortletContext portletContext = portletSession.getPortletContext();
        ApplicationContext applicationContext = PortletApplicationContextUtils.getRequiredWebApplicationContext(portletContext);
        SharedAppBean sharedAppBean = applicationContext.getBean("sharedAppBean", SharedAppBean.class);

        renderRequest.setAttribute("sharedBeanRandomInt", sharedAppBean.getId());

        return "mvc4test2/view";
    }
}
