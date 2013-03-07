package pl.itcrowd.richfaces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Piotr Kozlowski
 * Date: 3/6/13
 * Company: IT Crowd
 */
@ManagedBean
@ViewScoped
public class PollBean implements Serializable {

    public Date getDate() {
        Date date = new Date();
        return date;
    }

}
