package kr.co.bootpay.model.request;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class SMS {
    public String sp;
    public List<String> rps = new ArrayList();
    public String msg;
}
