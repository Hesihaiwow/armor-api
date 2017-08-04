package com.zhixinhuixue.armor.filter;

import com.alibaba.druid.util.PatternMatcher;
import com.alibaba.druid.util.ServletPathMatcher;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Akuma on 16/6/30.
 */
public abstract class ZSYAbstractFilter {

    protected static final String PARAM_NAME_EXCLUSIONS = "exclusions";
    protected PatternMatcher pathMatcher = new ServletPathMatcher();
    protected Set<String> excludesPattern;
    protected String contextPath;


    protected boolean isExclusion(String requestURI) {
        if(this.excludesPattern == null) {
            return false;
        } else {
            if(this.contextPath != null && requestURI.startsWith(this.contextPath)) {
                requestURI = requestURI.substring(this.contextPath.length());
                if(!requestURI.startsWith("/")) {
                    requestURI = "/" + requestURI;
                }
            }

            Iterator var2 = this.excludesPattern.iterator();

            String pattern;
            do {
                if(!var2.hasNext()) {
                    return false;
                }
                pattern = (String)var2.next();
            } while(!this.pathMatcher.matches(pattern, requestURI));

            return true;
        }
    }



}
