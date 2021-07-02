package com.deltaqin.designPattern.d10_chainOfResponsibility.servletFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author deltaqin
 * @date 2021/3/27 12:39 下午
 */
public class ServletFilter {
    public static void main(String[] args) {
        Request request = new Request();
        request.str = "大家好:)，<script>，deltaqin ，大家都是996 ";
        Response response = new Response();
        response.str = "response";

        FilterChain chain = new FilterChain();
        chain.add(new HTMLFilter()).add(new SensitiveFilter());
        chain.doFilter(request, response);
        System.out.println(request.str);
        System.out.println(response.str);

    }
}

interface Filter {
    void doFilter(Request request, Response response, FilterChain chain);
}

class HTMLFilter implements Filter {
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        request.str = request.str.replaceAll("<", "[").replaceAll(">", "]") + "HTMLFilter()";
        chain.doFilter(request, response);
        response.str += "--HTMLFilter()";

    }
}

class Request {
    String str;
}

class Response {
    String str;
}

class SensitiveFilter implements Filter {
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        request.str = request.str.replaceAll("996", "955") + " SensitiveFilter()";
        chain.doFilter(request, response);
        response.str += "--SensitiveFilter()";

    }
}


class FilterChain {
    List<Filter> filters = new ArrayList<>();
    int index = 0;

    public FilterChain add(Filter f) {
        filters.add(f);
        return this;
    }

    public void doFilter(Request request, Response response) {
        // 这里相当于是递归结束的条件，调用链里面的所有的需要调用的都调用过了，就返回
        if(index == filters.size())
            return;

        // 否则继续调用下一个Filter
        Filter f = filters.get(index++);


        f.doFilter(request, response, this);
    }
}

